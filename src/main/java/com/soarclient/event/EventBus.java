package com.soarclient.event;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.Validate;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

public final class EventBus {

    private static final EventBus instance = new EventBus();

    private final Map<Class<?>, List<MethodData>> handlers = new ConcurrentHashMap<>();

    private final Map<Class<?>, Set<MethodData>> methodsCache = new Object2ObjectOpenHashMap<>();

    private EventBus() {}

    public static EventBus getInstance() {
        return instance;
    }

    public void register(Object obj) {
    	
        Set<MethodData> cachedMethods = methodsCache.computeIfAbsent(obj.getClass(), cls -> {
        	
            Set<MethodData> mdSet = new ObjectOpenHashSet<>();
            for (Method method : cls.getDeclaredMethods()) {
                if (method.isAnnotationPresent(EventHandler.class)) {
                    Validate.isTrue(method.getParameterCount() == 1,
                            "Method %s has %d parameters; expected 1.",
                            method.getName(), method.getParameterCount());

                    method.setAccessible(true);
                    try {
                        mdSet.add(new MethodData(obj, method));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            return mdSet;
        });

        for (MethodData md : cachedMethods) {
            Class<?> eventType = md.eventType;
            handlers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(md);
        }
    }

    public void registers(Object... objects) {
        for (Object obj : objects) {
            register(obj);
        }
    }

    public void unregister(Object obj) {
    	
        methodsCache.remove(obj.getClass());

        for (Map.Entry<Class<?>, List<MethodData>> entry : handlers.entrySet()) {
            List<MethodData> list = entry.getValue();
            list.removeIf(md -> md.instance == obj);
            if (list.isEmpty()) {
                handlers.remove(entry.getKey());
            }
        }
    }

    public <T> T post(T event) {
        List<MethodData> list = handlers.get(event.getClass());
        if (list != null) {
            for (MethodData md : list) {
                md.invoke(event);
            }
        }
        return event;
    }

    private static final class MethodData {
        final Object instance;
        final MethodHandle target;
        final Class<?> eventType;

        MethodData(Object instance, Method method) throws IllegalAccessException {
            this.instance = instance;
            this.eventType = method.getParameterTypes()[0];
            this.target = MethodHandles.lookup().unreflect(method);
        }

        void invoke(Object event) {
            try {
                target.invoke(instance, event);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}