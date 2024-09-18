package com.soarclient.libs.sodium.client.gui.frame.tab;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.Validate;

import com.soarclient.libs.sodium.client.gui.frame.AbstractFrame;
import com.soarclient.libs.sodium.client.gui.frame.components.ScrollBarComponent;
import com.soarclient.libs.sodium.client.gui.frame.components.ScrollBarComponent.Mode;
import com.soarclient.libs.sodium.client.gui.widgets.AbstractWidget;
import com.soarclient.libs.sodium.client.gui.widgets.FlatButtonWidget;
import com.soarclient.libs.sodium.client.util.Dim2i;

public class TabFrame extends AbstractFrame {
	private final boolean tabSectionCanScroll;
	private final Dim2i tabSection;
	private final Dim2i frameSection;
	private final List<Tab<?>> tabs = new ArrayList();
	private final Runnable onSetTab;
	private final AtomicReference<IChatComponent> tabSectionSelectedTab;
	private ScrollBarComponent tabSectionScrollBar = null;
	private Tab<?> selectedTab;
	private AbstractFrame selectedFrame;

	public TabFrame(
		Dim2i dim,
		boolean renderOutline,
		List<Tab<?>> tabs,
		Runnable onSetTab,
		AtomicReference<IChatComponent> tabSectionSelectedTab,
		AtomicReference<Integer> tabSectionScrollBarOffset
	) {
		super(dim, renderOutline);
		this.tabs.addAll(tabs);
		int tabSectionY = this.tabs.size() * 18;
		this.tabSectionCanScroll = tabSectionY > this.dim.getHeight();
		Optional<Integer> result = tabs.stream().map(tab -> this.getTextWidth(tab.getTitle())).max(Integer::compareTo);
		this.tabSection = new Dim2i(
			this.dim.getOriginX(),
			this.dim.getOriginY(),
			(Integer)result.map(integer -> integer + (this.tabSectionCanScroll ? 32 : 24)).orElseGet(() -> (int)((double)this.dim.getWidth() * 0.35)),
			this.dim.getHeight()
		);
		this.frameSection = new Dim2i(this.tabSection.getLimitX(), this.dim.getOriginY(), this.dim.getWidth() - this.tabSection.getWidth(), this.dim.getHeight());
		this.onSetTab = onSetTab;
		if (this.tabSectionCanScroll) {
			this.tabSectionScrollBar = new ScrollBarComponent(
				new Dim2i(this.tabSection.getLimitX() - 11, this.tabSection.getOriginY(), 10, this.tabSection.getHeight()),
				Mode.VERTICAL,
				tabSectionY,
				this.dim.getHeight(),
				offset -> {
					this.buildFrame();
					tabSectionScrollBarOffset.set(offset);
				},
				this.dim
			);
			this.tabSectionScrollBar.setOffset((Integer)tabSectionScrollBarOffset.get());
		}

		this.tabSectionSelectedTab = tabSectionSelectedTab;
		if (this.tabSectionSelectedTab.get() != null) {
			this.selectedTab = (Tab<?>)this.tabs.stream().filter(tab -> tab.getTitle().equals(this.tabSectionSelectedTab.get())).findAny().get();
		}

		this.buildFrame();
		this.tabs.stream().filter(tab -> this.selectedTab != tab).forEach(tab -> tab.getFrameFunction().apply(this.frameSection));
	}

	public static TabFrame.Builder createBuilder() {
		return new TabFrame.Builder();
	}

	public void setTab(Tab<?> tab) {
		this.selectedTab = tab;
		this.tabSectionSelectedTab.set(this.selectedTab.getTitle());
		if (this.onSetTab != null) {
			this.onSetTab.run();
		}

		this.buildFrame();
	}

	@Override
	public void buildFrame() {
		this.children.clear();
		this.drawable.clear();
		this.controlElements.clear();
		if (this.selectedTab == null && !this.tabs.isEmpty()) {
			this.selectedTab = (Tab<?>)this.tabs.get(0);
		}

		this.rebuildTabs();
		this.rebuildTabFrame();
		if (this.tabSectionCanScroll) {
			this.tabSectionScrollBar.updateThumbPosition();
		}

		super.buildFrame();
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		this.applyScissor(this.dim.getOriginX(), this.dim.getOriginY(), this.dim.getWidth(), this.dim.getHeight(), () -> {
			for (AbstractWidget widget : this.children) {
				if (widget != this.selectedFrame) {
					widget.render(mouseX, mouseY, delta);
				}
			}
		});
		this.selectedFrame.render(mouseX, mouseY, delta);
		if (this.tabSectionCanScroll) {
			this.tabSectionScrollBar.render(mouseX, mouseY, delta);
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return this.dim.containsCursor(mouseX, mouseY) && super.mouseClicked(mouseX, mouseY, button)
			|| this.tabSectionCanScroll && this.tabSectionScrollBar.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button) {
		return super.mouseDragged(mouseX, mouseY, button) || this.tabSectionCanScroll && this.tabSectionScrollBar.mouseDragged(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		return super.mouseReleased(mouseX, mouseY, button) || this.tabSectionCanScroll && this.tabSectionScrollBar.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		return super.mouseScrolled(mouseX, mouseY, amount) || this.tabSectionCanScroll && this.tabSectionScrollBar.mouseScrolled(mouseX, mouseY, amount);
	}

	private void rebuildTabs() {
		int offsetY = 0;

		for (Tab<?> tab : this.tabs) {
			int x = this.tabSection.getOriginX();
			int y = this.tabSection.getOriginY() + offsetY - (this.tabSectionCanScroll ? this.tabSectionScrollBar.getOffset() : 0);
			int width = this.tabSection.getWidth() - (this.tabSectionCanScroll ? 12 : 4);
			int height = 18;
			Dim2i tabDim = new Dim2i(x, y, width, 18);
			FlatButtonWidget button = new FlatButtonWidget(tabDim, tab.getTitle(), () -> this.setTab(tab));
			button.setSelected(this.selectedTab == tab);
			this.children.add(button);
			offsetY += 18;
		}
	}

	private void rebuildTabFrame() {
		if (this.selectedTab != null) {
			AbstractFrame frame = (AbstractFrame)this.selectedTab.getFrameFunction().apply(this.frameSection);
			if (frame != null) {
				this.selectedFrame = frame;
				frame.buildFrame();
				this.children.add(frame);
			}
		}
	}

	public static class Builder {
		private final List<Tab<?>> functions = new ArrayList();
		private Dim2i dim;
		private boolean renderOutline;
		private Runnable onSetTab;
		private AtomicReference<IChatComponent> tabSectionSelectedTab = new AtomicReference(null);
		private AtomicReference<Integer> tabSectionScrollBarOffset = new AtomicReference(0);

		public TabFrame.Builder setDimension(Dim2i dim) {
			this.dim = dim;
			return this;
		}

		public TabFrame.Builder shouldRenderOutline(boolean renderOutline) {
			this.renderOutline = renderOutline;
			return this;
		}

		public TabFrame.Builder addTabs(Consumer<List<Tab<?>>> tabs) {
			tabs.accept(this.functions);
			return this;
		}

		public TabFrame.Builder onSetTab(Runnable onSetTab) {
			this.onSetTab = onSetTab;
			return this;
		}

		public TabFrame.Builder setTabSectionSelectedTab(AtomicReference<IChatComponent> tabSectionSelectedTab) {
			this.tabSectionSelectedTab = tabSectionSelectedTab;
			return this;
		}

		public TabFrame.Builder setTabSectionScrollBarOffset(AtomicReference<Integer> tabSectionScrollBarOffset) {
			this.tabSectionScrollBarOffset = tabSectionScrollBarOffset;
			return this;
		}

		public TabFrame build() {
			Validate.notNull(this.dim, "Dimension must be specified", new Object[0]);
			return new TabFrame(this.dim, this.renderOutline, this.functions, this.onSetTab, this.tabSectionSelectedTab, this.tabSectionScrollBarOffset);
		}
	}
}
