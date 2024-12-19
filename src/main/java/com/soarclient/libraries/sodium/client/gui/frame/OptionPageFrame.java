package com.soarclient.libraries.sodium.client.gui.frame;

import com.google.common.collect.UnmodifiableIterator;
import com.soarclient.libraries.sodium.client.gui.OptionExtended;
import com.soarclient.libraries.sodium.client.gui.options.Option;
import com.soarclient.libraries.sodium.client.gui.options.OptionGroup;
import com.soarclient.libraries.sodium.client.gui.options.OptionImpact;
import com.soarclient.libraries.sodium.client.gui.options.OptionPage;
import com.soarclient.libraries.sodium.client.gui.options.control.Control;
import com.soarclient.libraries.sodium.client.gui.options.control.ControlElement;
import com.soarclient.libraries.sodium.client.util.Dim2i;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.Validate;

public class OptionPageFrame extends AbstractFrame {
	protected final Dim2i originalDim;
	protected final OptionPage page;
	private long lastTime = 0L;
	private ControlElement<?> lastHoveredElement = null;

	public OptionPageFrame(Dim2i dim, boolean renderOutline, OptionPage page) {
		super(dim, renderOutline);
		this.originalDim = new Dim2i(dim.getOriginX(), dim.getOriginY(), dim.getWidth(), dim.getHeight());
		this.page = page;
		this.setupFrame();
		this.buildFrame();
	}

	public static OptionPageFrame.Builder createBuilder() {
		return new OptionPageFrame.Builder();
	}

	public void setupFrame() {
		this.children.clear();
		this.drawable.clear();
		this.controlElements.clear();
		int y = 0;
		if (!this.page.getGroups().isEmpty()) {
			OptionGroup lastGroup = (OptionGroup) this.page.getGroups().get(this.page.getGroups().size() - 1);
			UnmodifiableIterator var3 = this.page.getGroups().iterator();

			while (var3.hasNext()) {
				OptionGroup group = (OptionGroup) var3.next();
				y += group.getOptions().size() * 18;
				if (group != lastGroup) {
					y += 4;
				}
			}
		}

		this.dim.setHeight(y);
		this.page.getGroups().forEach(groupx -> groupx.getOptions().forEach(option -> {
			if (option instanceof OptionExtended<?> optionExtended) {
				optionExtended.setParentDimension(this.dim);
			}
		}));
	}

	@Override
	public void buildFrame() {
		if (this.page != null) {
			this.children.clear();
			this.drawable.clear();
			this.controlElements.clear();
			int y = 0;

			for (UnmodifiableIterator var2 = this.page.getGroups().iterator(); var2.hasNext(); y += 4) {
				OptionGroup group = (OptionGroup) var2.next();

				for (UnmodifiableIterator var4 = group.getOptions().iterator(); var4.hasNext(); y += 18) {
					Option<?> option = (Option<?>) var4.next();
					Control<?> control = option.getControl();
					ControlElement<?> element = control.createElement(
							new Dim2i(this.dim.getOriginX(), this.dim.getOriginY() + y, this.dim.getWidth(), 18));
					this.children.add(element);
				}
			}

			super.buildFrame();
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		ControlElement<?> hoveredElement = (ControlElement<?>) this.controlElements.stream()
				.filter(controlElement -> controlElement.getDimensions().overlapWith(this.originalDim))
				.filter(ControlElement::isHovered).findFirst().orElse(null);
		super.render(mouseX, mouseY, delta);
		if (hoveredElement != null && this.lastHoveredElement == hoveredElement
				&& this.originalDim.containsCursor((double) mouseX, (double) mouseY) && hoveredElement.isHovered()) {
			if (this.lastTime == 0L) {
				this.lastTime = System.currentTimeMillis();
			}

			this.renderOptionTooltip(hoveredElement);
		} else {
			this.lastTime = 0L;
			this.lastHoveredElement = hoveredElement;
		}
	}

	private void renderOptionTooltip(ControlElement<?> element) {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
		if (this.lastTime + 500L <= System.currentTimeMillis()) {
			Dim2i dim = element.getDimensions();
			int textPadding = 3;
			int boxPadding = 3;
			int boxWidth = dim.getWidth();
			int boxY = dim.getLimitY();
			int boxX = dim.getOriginX();
			Option<?> option = element.getOption();
			List<String> tooltip = new ArrayList(
					fontRenderer.listFormattedStringToWidth(option.getTooltip().getFormattedText(), boxWidth - 6));
			OptionImpact impact = option.getImpact();
			if (impact != null) {
				tooltip.add(EnumChatFormatting.GRAY + I18n.format("sodium.options.performance_impact_string",
						new Object[] { impact.toDisplayString() }));
			}

			int boxHeight = tooltip.size() * 12 + 3;
			int boxYLimit = boxY + boxHeight;
			int boxYCutoff = this.originalDim.getLimitY();
			if (boxYLimit > boxYCutoff) {
				boxY -= boxHeight + dim.getHeight();
			}

			if (boxY < 0) {
				boxY = dim.getLimitY();
			}

			this.drawRect((double) boxX, (double) boxY, (double) (boxX + boxWidth), (double) (boxY + boxHeight),
					-536870912);
			this.drawRectOutline((double) boxX, (double) boxY, (double) (boxX + boxWidth), (double) (boxY + boxHeight),
					-7019309);

			for (int i = 0; i < tooltip.size(); i++) {
				fontRenderer.drawString((String) tooltip.get(i), boxX + 3, boxY + 3 + i * 12, -1);
			}
		}
	}

	public static class Builder {
		private Dim2i dim;
		private boolean renderOutline;
		private OptionPage page;

		public OptionPageFrame.Builder setDimension(Dim2i dim) {
			this.dim = dim;
			return this;
		}

		public OptionPageFrame.Builder shouldRenderOutline(boolean renderOutline) {
			this.renderOutline = renderOutline;
			return this;
		}

		public OptionPageFrame.Builder setOptionPage(OptionPage page) {
			this.page = page;
			return this;
		}

		public OptionPageFrame build() {
			Validate.notNull(this.dim, "Dimension must be specified", new Object[0]);
			Validate.notNull(this.page, "Option Page must be specified", new Object[0]);
			return new OptionPageFrame(this.dim, this.renderOutline, this.page);
		}
	}
}
