package com.soarclient.event;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.Validate;

public final class EventBus {

	private static final EventBus instance = new EventBus();

	private boolean inPost;
	private final List<Object> toRemove = new LinkedList<>();
	private final Map<Class<?>, List<MethodData>> handlers = new HashMap<Class<?>, List<MethodData>>();

	private Set<Method> getMethods(Class<?> clazz) {

		Set<Method> methods = new HashSet<>();

		Collections.addAll(methods, clazz.getMethods());
		Collections.addAll(methods, clazz.getDeclaredMethods());
		methods.forEach((method) -> method.setAccessible(true));

		return methods;
	}

	public void register(Object obj) {
		for (Method method : getMethods(obj.getClass())) {
			if (validate(method)) {
				try {
					handlers.computeIfAbsent(method.getParameters()[0].getType(), (ignore) -> new ArrayList<>())
							.add(new MethodData(obj, method));
				} catch (IllegalAccessException error) {
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

		for (List<MethodData> methods : handlers.values()) {
			methods.removeIf((method) -> method.instance == obj);
		}
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

		public MethodData(Object instance, Method method) throws IllegalAccessException {
			this.instance = instance;
			target = MethodHandles.lookup().unreflect(method);
		}
	}
}