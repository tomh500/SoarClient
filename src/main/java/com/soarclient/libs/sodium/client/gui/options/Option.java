package com.soarclient.libs.sodium.client.gui.options;

import java.util.Collection;

import com.soarclient.libs.sodium.client.gui.options.control.Control;
import com.soarclient.libs.sodium.client.gui.options.storage.OptionStorage;

import net.minecraft.util.IChatComponent;

public interface Option<T> {
	IChatComponent getNewName();

	String getName();

	IChatComponent getTooltip();

	OptionImpact getImpact();

	Control<T> getControl();

	T getValue();

	void setValue(T object);

	void reset();

	OptionStorage<?> getStorage();

	boolean isAvailable();

	boolean hasChanged();

	void applyChanges();

	Collection<OptionFlag> getFlags();
}
