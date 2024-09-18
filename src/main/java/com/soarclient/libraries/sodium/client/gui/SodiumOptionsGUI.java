package com.soarclient.libraries.sodium.client.gui;

import com.google.common.collect.UnmodifiableIterator;
import com.soarclient.libraries.sodium.client.gui.options.Option;
import com.soarclient.libraries.sodium.client.gui.options.OptionFlag;
import com.soarclient.libraries.sodium.client.gui.options.OptionGroup;
import com.soarclient.libraries.sodium.client.gui.options.OptionImpact;
import com.soarclient.libraries.sodium.client.gui.options.OptionPage;
import com.soarclient.libraries.sodium.client.gui.options.control.Control;
import com.soarclient.libraries.sodium.client.gui.options.control.ControlElement;
import com.soarclient.libraries.sodium.client.gui.options.storage.OptionStorage;
import com.soarclient.libraries.sodium.client.gui.utils.Drawable;
import com.soarclient.libraries.sodium.client.gui.utils.Element;
import com.soarclient.libraries.sodium.client.gui.utils.ScrollableGuiScreen;
import com.soarclient.libraries.sodium.client.gui.widgets.FlatButtonWidget;
import com.soarclient.libraries.sodium.client.util.Dim2i;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

public class SodiumOptionsGUI extends ScrollableGuiScreen {
	protected final List<Element> children = new CopyOnWriteArrayList();
	protected final List<OptionPage> pages = new ArrayList();
	protected final List<ControlElement<?>> controls = new ArrayList();
	protected final List<Drawable> drawable = new ArrayList();
	public final GuiScreen prevScreen;
	protected OptionPage currentPage;
	protected FlatButtonWidget applyButton;
	protected FlatButtonWidget closeButton;
	protected FlatButtonWidget undoButton;
	protected boolean hasPendingChanges;
	protected ControlElement<?> hoveredElement;

	public SodiumOptionsGUI(GuiScreen prevScreen) {
		this.prevScreen = prevScreen;
		this.pages.add(SodiumGameOptionPages.general());
		this.pages.add(SodiumGameOptionPages.quality());
		this.pages.add(SodiumGameOptionPages.advanced());
		this.pages.add(SodiumGameOptionPages.performance());
	}

	public void setPage(OptionPage page) {
		this.currentPage = page;
		this.rebuildGUI();
	}

	public void initGui() {
		super.initGui();
		this.rebuildGUI();
	}

	protected void rebuildGUI() {
		this.controls.clear();
		this.children.clear();
		this.drawable.clear();
		if (this.currentPage == null) {
			if (this.pages.isEmpty()) {
				throw new IllegalStateException("No pages are available?!");
			}

			this.currentPage = (OptionPage)this.pages.get(0);
		}

		this.rebuildGUIPages();
		this.rebuildGUIOptions();
		this.undoButton = new FlatButtonWidget(
			new Dim2i(this.width - 211, this.height - 26, 65, 20),
			new ChatComponentTranslation("sodium.options.buttons.undo", new Object[0]).getFormattedText(),
			this::undoChanges
		);
		this.applyButton = new FlatButtonWidget(
			new Dim2i(this.width - 142, this.height - 26, 65, 20),
			new ChatComponentTranslation("sodium.options.buttons.apply", new Object[0]).getFormattedText(),
			this::applyChanges
		);
		this.closeButton = new FlatButtonWidget(
			new Dim2i(this.width - 73, this.height - 26, 65, 20), new ChatComponentTranslation("gui.done", new Object[0]).getFormattedText(), this::onClose
		);
		this.children.add(this.undoButton);
		this.children.add(this.applyButton);
		this.children.add(this.closeButton);

		for (Element element : this.children) {
			if (element instanceof Drawable) {
				this.drawable.add((Drawable)element);
			}
		}
	}

	private void rebuildGUIPages() {
		int x = 6;
		int y = 6;

		for (OptionPage page : this.pages) {
			int width = 12 + this.fontRendererObj.getStringWidth(page.getNewName().getFormattedText());
			FlatButtonWidget button = new FlatButtonWidget(new Dim2i(x, y, width, 18), page.getNewName(), () -> this.setPage(page));
			button.setSelected(this.currentPage == page);
			x += width + 6;
			this.children.add(button);
		}
	}

	private void rebuildGUIOptions() {
		int x = 6;
		int y = 28;

		for (UnmodifiableIterator var3 = this.currentPage.getGroups().iterator(); var3.hasNext(); y += 4) {
			OptionGroup group = (OptionGroup)var3.next();

			for (UnmodifiableIterator var5 = group.getOptions().iterator(); var5.hasNext(); y += 18) {
				Option<?> option = (Option<?>)var5.next();
				Control<?> control = option.getControl();
				ControlElement<?> element = control.createElement(new Dim2i(x, y, 200, 18));
				this.controls.add(element);
				this.children.add(element);
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float delta) {
		super.drawDefaultBackground();
		this.updateControls();

		for (Drawable drawable : this.drawable) {
			drawable.render(mouseX, mouseY, delta);
		}

		if (this.hoveredElement != null) {
			this.renderOptionTooltip(this.hoveredElement);
		}
	}

	@Override
	public List<? extends Element> children() {
		return this.children;
	}

	private void updateControls() {
		ControlElement<?> hovered = (ControlElement<?>)this.getActiveControls().filter(ControlElement::isHovered).findFirst().orElse(null);
		boolean hasChanges = this.getAllOptions().anyMatch(Option::hasChanged);

		for (OptionPage page : this.pages) {
			UnmodifiableIterator var5 = page.getOptions().iterator();

			while (var5.hasNext()) {
				Option<?> option = (Option<?>)var5.next();
				if (option.hasChanged()) {
					hasChanges = true;
				}
			}
		}

		this.applyButton.setEnabled(hasChanges);
		this.undoButton.setVisible(hasChanges);
		this.closeButton.setEnabled(!hasChanges);
		this.hasPendingChanges = hasChanges;
		this.hoveredElement = hovered;
	}

	private Stream<Option<?>> getAllOptions() {
		return this.pages.stream().flatMap(s -> s.getOptions().stream());
	}

	private Stream<ControlElement<?>> getActiveControls() {
		return this.controls.stream();
	}

	private void renderOptionTooltip(ControlElement<?> element) {
		Dim2i dim = element.getDimensions();
		int textPadding = 3;
		int boxPadding = 3;
		int boxWidth = 200;
		int boxY = dim.getOriginY();
		int boxX = dim.getLimitX() + boxPadding;
		Option<?> option = element.getOption();
		List<String> tooltip = new ArrayList(this.fontRendererObj.listFormattedStringToWidth(option.getTooltip().getFormattedText(), boxWidth - textPadding * 2));
		OptionImpact impact = option.getImpact();
		if (impact != null) {
			tooltip.add(EnumChatFormatting.GRAY + I18n.format("sodium.options.performance_impact_string", new Object[]{impact.toDisplayString()}));
		}

		int boxHeight = tooltip.size() * 12 + boxPadding;
		int boxYLimit = boxY + boxHeight;
		int boxYCutoff = this.height - 40;
		if (boxYLimit > boxYCutoff) {
			boxY -= boxYLimit - boxYCutoff;
		}

		this.drawGradientRect(boxX, boxY, boxX + boxWidth, boxY + boxHeight, -536870912, -536870912);

		for (int i = 0; i < tooltip.size(); i++) {
			this.fontRendererObj.drawString((String)tooltip.get(i), boxX + textPadding, boxY + textPadding + i * 12, -1);
		}
	}

	private void applyChanges() {
		HashSet<OptionStorage<?>> dirtyStorages = new HashSet();
		EnumSet<OptionFlag> flags = EnumSet.noneOf(OptionFlag.class);
		this.getAllOptions().forEach(option -> {
			if (option.hasChanged()) {
				option.applyChanges();
				flags.addAll(option.getFlags());
				dirtyStorages.add(option.getStorage());
			}
		});
		if (flags.contains(OptionFlag.REQUIRES_RENDERER_RELOAD)) {
			this.mc.renderGlobal.loadRenderers();
		}

		if (flags.contains(OptionFlag.REQUIRES_ASSET_RELOAD)) {
			this.mc.getTextureMapBlocks().setMipmapLevels(this.mc.gameSettings.mipmapLevels);
			this.mc.refreshResources();
		}

		for (OptionStorage<?> storage : dirtyStorages) {
			storage.save();
		}
	}

	private void undoChanges() {
		this.getAllOptions().forEach(Option::reset);
	}

	private void openDonationPage() {
		URLUtils.open("https://caffeinemc.net/donate");
	}

	public void keyTyped(char typedChar, int keyCode) {
		if (keyCode != 1 || this.shouldCloseOnEsc()) {
			if (keyCode == 1) {
				this.onClose();
			} else {
				if (keyCode == 25 && isShiftKeyDown()) {
					this.mc.displayGuiScreen(new GuiVideoSettings(this.prevScreen, this.mc.gameSettings));
				}
			}
		}
	}

	public boolean shouldCloseOnEsc() {
		return !this.hasPendingChanges;
	}

	public void onClose() {
		this.mc.displayGuiScreen(this.prevScreen);
		super.onGuiClosed();
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		try {
			super.mouseClicked(mouseX, mouseY, mouseButton);
		} catch (IOException e) {}
		this.children.forEach(element -> element.mouseClicked((double)mouseX, (double)mouseY, mouseButton));
	}

	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		this.children.forEach(element -> element.mouseDragged((double)mouseX, (double)mouseY, clickedMouseButton));
	}
}
