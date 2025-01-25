package com.soarclient.skia.context;

import java.util.Stack;

public class States {

	private static final States INSTANCE = new States();

	public final Stack<Integer> textures = new Stack<>();

	private final Stack<State> states = new Stack<>();

	private States() {
	}

	public static States getInstance() {
		return INSTANCE;
	}

	public void push() {
		State currentState = new State();
		currentState.push();
		states.push(currentState);
	}

	public void pop() {
		if (states.isEmpty()) {
			throw new IllegalStateException("No state to restore.");
		}
		State state = states.pop();
		state.pop();
	}

	public Stack<Integer> getTextures() {
		return textures;
	}

	public Stack<State> getStates() {
		return states;
	}
}