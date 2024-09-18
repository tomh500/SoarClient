package com.soarclient.libraries.sodium.client.gui.frame.tab;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import com.soarclient.libraries.sodium.client.gui.frame.AbstractFrame;
import com.soarclient.libraries.sodium.client.gui.frame.OptionPageFrame;
import com.soarclient.libraries.sodium.client.gui.frame.ScrollableFrame;
import com.soarclient.libraries.sodium.client.gui.options.OptionPage;
import com.soarclient.libraries.sodium.client.util.Dim2i;

import net.minecraft.util.IChatComponent;

public class Tab<T extends AbstractFrame> {
	private final IChatComponent title;
	private final Function<Dim2i, T> frameFunction;

	public Tab(IChatComponent title, Function<Dim2i, T> frameFunction) {
		this.title = title;
		this.frameFunction = frameFunction;
	}

	public static Tab.Builder<?> createBuilder() {
		return new Tab.Builder();
	}

	public IChatComponent getTitle() {
		return this.title;
	}

	public Function<Dim2i, T> getFrameFunction() {
		return this.frameFunction;
	}

	public static class Builder<T extends AbstractFrame> {
		private IChatComponent title;
		private Function<Dim2i, T> frameFunction;

		public Tab.Builder<T> setTitle(IChatComponent title) {
			this.title = title;
			return this;
		}

		public Tab.Builder<T> setFrameFunction(Function<Dim2i, T> frameFunction) {
			this.frameFunction = frameFunction;
			return this;
		}

		public Tab<T> build() {
			return new Tab<>(this.title, this.frameFunction);
		}

		public Tab<ScrollableFrame> from(OptionPage page, AtomicReference<Integer> verticalScrollBarOffset) {
			return new Tab<>(
				page.getName(),
				dim2i -> ScrollableFrame.createBuilder()
						.setDimension(dim2i)
						.setFrame(
							OptionPageFrame.createBuilder()
								.setDimension(new Dim2i(dim2i.getOriginX(), dim2i.getOriginY(), dim2i.getWidth(), dim2i.getHeight()))
								.setOptionPage(page)
								.build()
						)
						.setVerticalScrollBarOffset(verticalScrollBarOffset)
						.build()
			);
		}
	}
}
