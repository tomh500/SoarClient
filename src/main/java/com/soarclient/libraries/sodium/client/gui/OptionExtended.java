package com.soarclient.libraries.sodium.client.gui;

import com.soarclient.libraries.sodium.client.gui.options.Option;
import com.soarclient.libraries.sodium.client.util.Dim2i;

public interface OptionExtended<T> extends Option<T> {
	boolean isHighlight();

	void setHighlight(boolean boolean1);

	Dim2i getDim2i();

	void setDim2i(Dim2i dim2i);

	Dim2i getParentDimension();

	void setParentDimension(Dim2i dim2i);

	boolean isSelected();

	void setSelected(boolean boolean1);
}
