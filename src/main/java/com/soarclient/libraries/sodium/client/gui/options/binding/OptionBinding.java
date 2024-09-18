package com.soarclient.libraries.sodium.client.gui.options.binding;

public interface OptionBinding<S, T> {
	void setValue(S object1, T object2);

	T getValue(S object);
}
