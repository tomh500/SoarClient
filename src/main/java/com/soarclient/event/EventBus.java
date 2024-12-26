package com.soarclient.event;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.lang3.Validate;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

public final class EventBus {

	private static final EventBus instance = new EventBus();
	private static final Logger logger = Logger.getLogger(EventBus.class.getName());

	private volatile Map<Class<?>, List<MethodData>> handlers = Collections.emptyMap();
	private final Map<Class<?>, Set<Method>> methodsCache = new Object2ObjectOpenHashMap<>();

	public static EventBus getInstance() {
		return instance;
	}

	public void register(Object obj) {

		Class<?> clazz = obj.getClass();
		Map<Class<?>, List<MethodData>> oldHandlers = this.handlers;
		Map<Class<?>, List<MethodData>> newHandlers = new Object2ObjectOpenHashMap<>(oldHandlers);

		for (Method method : getMethods(clazz)) {
			if (validate(method)) {
				Class<?> paramType = method.getParameterTypes()[0];
				try {
					List<MethodData> list = newHandlers.get(paramType);
					if (list == null) {
						list = new ObjectArrayList<>();
						newHandlers.put(paramType, list);
					}
					list.add(new MethodData(obj, method));
				} catch (IllegalAccessException e) {
					logger.severe("Error creating MethodData: " + e.getMessage());
				}
			}
		}

		for (Map.Entry<Class<?>, List<MethodData>> entry : newHandlers.entrySet()) {
			List<MethodData> unmodifiableList = Collections.unmodifiableList(new ObjectArrayList<>(entry.getValue()));
			entry.setValue(unmodifiableList);
		}

		this.handlers = Collections.unmodifiableMap(newHandlers);
	}

	public void registers(Object... objects) {
		for (Object obj : objects) {
			register(obj);
		}
	}

	public void unregister(Object obj) {

		Map<Class<?>, List<MethodData>> oldHandlers = this.handlers;
		Map<Class<?>, List<MethodData>> newHandlers = new Object2ObjectOpenHashMap<>();

		for (Map.Entry<Class<?>, List<MethodData>> entry : oldHandlers.entrySet()) {
			List<MethodData> originalList = entry.getValue();
			List<MethodData> copiedList = new ObjectArrayList<>(originalList);
			boolean changed = copiedList.removeIf(md -> md.instance == obj);

			if (changed) {
				if (!copiedList.isEmpty()) {
					newHandlers.put(entry.getKey(), copiedList);
				}
			} else {
				newHandlers.put(entry.getKey(), originalList);
			}
		}

		for (Map.Entry<Class<?>, List<MethodData>> entry : newHandlers.entrySet()) {
			if (!entry.getValue().equals(oldHandlers.get(entry.getKey()))) {
				List<MethodData> immutableList = Collections.unmodifiableList(new ObjectArrayList<>(entry.getValue()));
				entry.setValue(immutableList);
			}
		}

		this.handlers = Collections.unmodifiableMap(newHandlers);
	}

	public <T> T post(T event) {
		List<MethodData> list = handlers.get(event.getClass());
		if (list == null || list.isEmpty()) {
			return event;
		}
		for (MethodData md : list) {
			try {
				md.target.invoke(md.instance, event);
			} catch (Throwable error) {
				logger.severe("Error invoking event handler: " + error.getMessage());
			}
		}
		return event;
	}

	private Set<Method> getMethods(Class<?> clazz) {
		return methodsCache.computeIfAbsent(clazz, key -> {
			Set<Method> methods = new ObjectOpenHashSet<>();
			for (Method m : key.getDeclaredMethods()) {
				m.setAccessible(true);
				methods.add(m);
			}
			for (Method m : key.getMethods()) {
				if (!methods.contains(m)) {
					m.setAccessible(true);
					methods.add(m);
				}
			}
			return methods;
		});
	}

	private boolean validate(Method method) {
		if (method.isAnnotationPresent(EventHandler.class)) {
			Validate.isTrue(method.getParameterCount() == 1, "Method %s has %d parameters; expected 1.",
					method.getName(), method.getParameterCount());
			return true;
		}
		return false;
	}

	private static final class MethodData {
		final Object instance;
		final MethodHandle target;

		MethodData(Object instance, Method method) throws IllegalAccessException {
			this.instance = instance;
			this.target = MethodHandles.lookup().unreflect(method);
		}
	}
}