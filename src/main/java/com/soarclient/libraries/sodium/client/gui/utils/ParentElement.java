package com.soarclient.libraries.sodium.client.gui.utils;

import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

public interface ParentElement extends Element {
	List<? extends Element> children();

	default Optional<Element> hoveredElement(double mouseX, double mouseY) {
		for (Element element : this.children()) {
			if (element.isMouseOver(mouseX, mouseY)) {
				return Optional.of(element);
			}
		}

		return Optional.empty();
	}

	@Override
	default boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (Element element : this.children()) {
			if (element.mouseClicked(mouseX, mouseY, button)) {
				this.setFocused(element);
				if (button == 0) {
					this.setDragging(true);
				}

				return true;
			}
		}

		return false;
	}

	@Override
	default boolean mouseReleased(double mouseX, double mouseY, int button) {
		this.setDragging(false);
		return this.hoveredElement(mouseX, mouseY).filter(element -> element.mouseReleased(mouseX, mouseY, button))
				.isPresent();
	}

	@Override
	default boolean mouseDragged(double mouseX, double mouseY, int button) {
		return this.getFocused() != null && this.isDragging() && button == 0
				&& this.getFocused().mouseDragged(mouseX, mouseY, button);
	}

	boolean isDragging();

	void setDragging(boolean boolean1);

	@Override
	default boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		return this.hoveredElement(mouseX, mouseY).filter(element -> element.mouseScrolled(mouseX, mouseY, amount))
				.isPresent();
	}

	@Override
	default boolean keyTyped(char typedChar, int keyCode) {
		return this.getFocused() != null && this.getFocused().keyTyped(typedChar, keyCode);
	}

	@Nullable
	Element getFocused();

	void setFocused(@Nullable Element element);

	@Override
	default boolean changeFocus(boolean lookForwards) {
		Element element = this.getFocused();
		if (element != null && element.changeFocus(lookForwards)) {
			return true;
		} else {
			List<? extends Element> list = this.children();
			int i = list.indexOf(element);
			int j;
			if (element != null && i >= 0) {
				j = i + (lookForwards ? 1 : 0);
			} else if (lookForwards) {
				j = 0;
			} else {
				j = list.size();
			}

			ListIterator<? extends Element> listIterator = list.listIterator(j);
			BooleanSupplier booleanSupplier = lookForwards ? listIterator::hasNext : listIterator::hasPrevious;
			Supplier<? extends Element> supplier = lookForwards ? listIterator::next : listIterator::previous;

			while (booleanSupplier.getAsBoolean()) {
				Element element2 = (Element) supplier.get();
				if (element2.changeFocus(lookForwards)) {
					this.setFocused(element2);
					return true;
				}
			}

			this.setFocused(null);
			return false;
		}
	}
}
