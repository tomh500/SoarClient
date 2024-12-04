package com.soarclient.libraries.sodium.client.gui.options.control;

import com.soarclient.libraries.sodium.client.gui.options.Option;
import com.soarclient.libraries.sodium.client.util.Dim2i;

public interface Control<T> {
	Option<T> getOption();

	ControlElement<T> createElement(Dim2i dim2i);

	int getMaxWidth();
}
