package com.soarclient.event;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.Validate;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public final class EventBus {

    private static final EventBus instance = new EventBus();

    private boolean inPost;
    private final Set<Object> toRemove = ConcurrentHashMap.newKeySet();
    private final Object2ObjectOpenHashMap<Class<?>, CopyOnWriteArrayList<MethodData>> handlers = new Object2ObjectOpenHashMap<>();
    private final LoadingCache<Method, MethodHandle> methodHandleCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .build(method -> {
                try {
                    return MethodHandles.lookup().unreflect(method);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to create MethodHandle", e);
                }
            });

    private Set<Method> getMethods(Class<?> clazz) {
        Set<Method> methods = ConcurrentHashMap.newKeySet();
        Collections.addAll(methods, clazz.getMethods());
        Collections.addAll(methods, clazz.getDeclaredMethods());
        methods.forEach(method -> method.setAccessible(true));
        return methods;
    }

    public void register(Object obj) {
        for (Method method : getMethods(obj.getClass())) {
            if (validate(method)) {
                try {
                    handlers.computeIfAbsent(method.getParameters()[0].getType(), ignore -> new CopyOnWriteArrayList<>())
                            .add(new MethodData(obj, methodHandleCache.get(method)));
                } catch (Exception error) {
                }
            }
        }
    }

    public void registers(Object... objects) {
        for (Object obj : objects) {
            register(obj);
        }
    }

    public void unregister(Object obj) {
        if (inPost) {
            toRemove.add(obj);
            return;
        }

        handlers.values().forEach(methods -> methods.removeIf(method -> method.instance == obj));
    }

    public <T> T post(T event) {
    	
        if (!handlers.containsKey(event.getClass())) {
            return event;
        }

        try {
            toRemove.clear();
            inPost = true;
            for (MethodData method : handlers.get(event.getClass())) {
                try {
                    method.target.invoke(method.instance, event);
                } catch (Throwable error) {
                }
            }
            inPost = false;
            toRemove.forEach(this::unregister);
        } catch (ConcurrentModificationException ignored) {
        }

        return event;
    }

    private boolean validate(Method method) {
        if (method.isAnnotationPresent(EventHandler.class)) {
            Validate.isTrue(method.getParameterCount() == 1,
                    "Method " + method.getName() + " has " + method.getParameterCount() + " parameters; expected 1.");
            return true;
        }
        return false;
    }

    public static EventBus getInstance() {
        return instance;
    }

    private static final class MethodData {
        public final Object instance;
        public final MethodHandle target;

        public MethodData(Object instance, MethodHandle target) {
            this.instance = instance;
            this.target = target;
        }
    }
}