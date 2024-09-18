package com.soarclient.libs.sodium.client.gui;

import com.soarclient.libs.sodium.client.gui.options.Option;
import com.soarclient.libs.sodium.client.util.Dim2i;

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
