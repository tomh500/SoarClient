package com.soarclient.libs.sodium.client.gui.options.control;

import com.soarclient.libs.sodium.client.gui.options.Option;
import com.soarclient.libs.sodium.client.util.Dim2i;

public interface Control<T> {
	Option<T> getOption();

	ControlElement<T> createElement(Dim2i dim2i);

	int getMaxWidth();
}
