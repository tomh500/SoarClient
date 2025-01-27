package com.soarclient.skia.context;

import java.util.Stack;

import org.lwjgl.opengl.GL30;

public class States {

	private static final int glVersion;
	private static final Stack<State> states = new Stack<>();

	static {
		int[] major = new int[1];
		int[] minor = new int[1];
		GL30.glGetIntegerv(GL30.GL_MAJOR_VERSION, major);
		GL30.glGetIntegerv(GL30.GL_MINOR_VERSION, minor);
		glVersion = major[0] * 100 + minor[0] * 10;
	}

	public static void push() {
		State currentState = new State(glVersion);
		currentState.push();
		states.push(currentState);
	}

	public static void pop() {
		if (states.isEmpty()) {
			throw new IllegalStateException("No state to restore.");
		}
		State state = states.pop();
		state.pop();
	}
}