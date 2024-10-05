package com.soarclient.libraries.sodium.client.gui.frame.components;

import com.google.common.collect.UnmodifiableIterator;
import com.soarclient.libraries.sodium.client.gui.OptionExtended;
import com.soarclient.libraries.sodium.client.gui.ReeseSodiumVideoOptionsScreen;
import com.soarclient.libraries.sodium.client.gui.options.Option;
import com.soarclient.libraries.sodium.client.gui.options.OptionPage;
import com.soarclient.libraries.sodium.client.gui.widgets.AbstractWidget;
import com.soarclient.libraries.sodium.client.util.Dim2i;
import com.soarclient.libraries.sodium.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class SearchTextFieldComponent extends AbstractWidget {
	protected final Dim2i dim;
	protected final List<OptionPage> pages;
	private final FontRenderer textRenderer = Minecraft.getMinecraft().fontRendererObj;
	private final Predicate<String> textPredicate = Objects::nonNull;
	private final AtomicReference<IChatComponent> tabFrameSelectedTab;
	private final AtomicReference<Integer> tabFrameScrollBarOffset;
	private final AtomicReference<Integer> optionPageScrollBarOffset;
	private final int tabDimHeight;
	private final ReeseSodiumVideoOptionsScreen sodiumVideoOptionsScreen;
	private final AtomicReference<String> lastSearch;
	private final AtomicReference<Integer> lastSearchIndex;
	protected boolean selecting;
	protected String text = "";
	protected int maxLength = 100;
	protected boolean visible = true;
	protected boolean editable = true;
	private int firstCharacterIndex;
	private int selectionStart;
	private int selectionEnd;
	private boolean focused;
	private int lastCursorPosition = this.getCursor();

	public SearchTextFieldComponent(Dim2i dim, List<OptionPage> pages,
			AtomicReference<IChatComponent> tabFrameSelectedTab, AtomicReference<Integer> tabFrameScrollBarOffset,
			AtomicReference<Integer> optionPageScrollBarOffset, int tabDimHeight,
			ReeseSodiumVideoOptionsScreen sodiumVideoOptionsScreen, AtomicReference<String> lastSearch,
			AtomicReference<Integer> lastSearchIndex) {
		this.dim = dim;
		this.pages = pages;
		this.tabFrameSelectedTab = tabFrameSelectedTab;
		this.tabFrameScrollBarOffset = tabFrameScrollBarOffset;
		this.optionPageScrollBarOffset = optionPageScrollBarOffset;
		this.tabDimHeight = tabDimHeight;
		this.sodiumVideoOptionsScreen = sodiumVideoOptionsScreen;
		this.lastSearch = lastSearch;
		this.lastSearchIndex = lastSearchIndex;
		if (!((String) lastSearch.get()).trim().isEmpty()) {
			this.write((String) lastSearch.get());
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		if (this.isVisible()) {
			if (!this.isFocused() && this.text.isEmpty()) {
				String key = "rso.search_bar_empty";
				String text = I18n.format("rso.search_bar_empty", new Object[0]);
				if (text.equals("rso.search_bar_empty")) {
					text = "Search options...";
				}

				this.textRenderer.drawString(text, this.dim.getOriginX() + 6, this.dim.getOriginY() + 6, -5592406);
			}

			this.drawRect((double) this.dim.getOriginX(), (double) this.dim.getOriginY(), (double) this.dim.getLimitX(),
					(double) this.dim.getLimitY(), this.isFocused() ? -536870912 : -1879048192);
			int j = this.selectionStart - this.firstCharacterIndex;
			int k = this.selectionEnd - this.firstCharacterIndex;
			String string = this.textRenderer.trimStringToWidth(this.text.substring(this.firstCharacterIndex),
					this.getInnerWidth());
			boolean bl = j >= 0 && j <= string.length();
			int l = this.dim.getOriginX() + 6;
			int m = this.dim.getOriginY() + 6;
			int n = l;
			if (k > string.length()) {
				k = string.length();
			}

			if (!string.isEmpty()) {
				String string2 = bl ? string.substring(0, j) : string;
				n = this.textRenderer.drawStringWithShadow(string2, (float) l, (float) m, 14737632);
			}

			boolean bl3 = this.selectionStart < this.text.length() || this.text.length() >= this.getMaxLength();
			int o = n;
			if (!bl) {
				o = j > 0 ? l + this.dim.getWidth() - 12 : l;
			} else if (bl3) {
				o = n - 1;
				n--;
			}

			if (!string.isEmpty() && bl && j < string.length()) {
				this.textRenderer.drawStringWithShadow(string.substring(j), (float) n, (float) m, 14737632);
			}

			if (this.isFocused()) {
				this.drawRect((double) o, (double) (m - 1), (double) (o + 1),
						(double) (m + 1 + this.textRenderer.FONT_HEIGHT), -3092272);
			}

			if (k != j) {
				int p = l + this.textRenderer.getStringWidth(string.substring(0, k));
				this.drawSelectionHighlight(o, m - 1, p - 1, m + 1 + this.textRenderer.FONT_HEIGHT);
			}
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		int i = MathHelper.floor_double(mouseX) - this.dim.getOriginX() - 6;
		String string = this.textRenderer.trimStringToWidth(this.text.substring(this.firstCharacterIndex),
				this.getInnerWidth());
		this.setCursor(this.textRenderer.trimStringToWidth(string, i).length() + this.firstCharacterIndex);
		this.setFocused(this.dim.containsCursor(mouseX, mouseY));
		this.pages.forEach(page -> page.getOptions().stream().filter(OptionExtended.class::isInstance)
				.map(OptionExtended.class::cast).forEach(optionExtended -> optionExtended.setSelected(false)));
		return this.isFocused();
	}

	private void drawSelectionHighlight(int x1, int y1, int x2, int y2) {
		if (x1 < x2) {
			int i = x1;
			x1 = x2;
			x2 = i;
		}

		if (y1 < y2) {
			int i = y1;
			y1 = y2;
			y2 = i;
		}

		if (x2 > this.dim.getOriginX() + this.dim.getWidth()) {
			x2 = this.dim.getOriginX() + this.dim.getWidth();
		}

		if (x1 > this.dim.getOriginX() + this.dim.getWidth()) {
			x1 = this.dim.getOriginX() + this.dim.getWidth();
		}

		GL11.glEnable(3058);
		GL11.glLogicOp(5387);
		this.drawRect((double) x1, (double) y1, (double) x2, (double) y2, -16776961);
		GL11.glDisable(3058);
	}

	private int getMaxLength() {
		return this.maxLength;
	}

	public String getSelectedText() {
		int i = Math.min(this.selectionStart, this.selectionEnd);
		int j = Math.max(this.selectionStart, this.selectionEnd);
		return this.text.substring(i, j);
	}

	public void write(String text) {
		int i = Math.min(this.selectionStart, this.selectionEnd);
		int j = Math.max(this.selectionStart, this.selectionEnd);
		int k = this.maxLength - this.text.length() - (i - j);
		String string = text;
		int l = text.length();
		if (k < l) {
			string = text.substring(0, k);
			l = k;
		}

		String string2 = new StringBuilder(this.text).replace(i, j, string).toString();
		if (this.textPredicate.test(string2)) {
			this.text = string2;
			this.setSelectionStart(i + l);
			this.setSelectionEnd(this.selectionStart);
			this.onChanged(this.text);
		}
	}

	private void onChanged(String newText) {
		this.pages.forEach(page -> page.getOptions().stream().filter(OptionExtended.class::isInstance)
				.map(OptionExtended.class::cast).forEach(optionExtended -> optionExtended.setHighlight(false)));
		this.lastSearch.set(newText.trim());
		if (this.editable && !newText.trim().isEmpty()) {
			List<Option<?>> fuzzy = StringUtils.fuzzySearch(this.pages, newText, 2);
			fuzzy.stream().filter(OptionExtended.class::isInstance).map(OptionExtended.class::cast)
					.forEach(optionExtended -> optionExtended.setHighlight(true));
		}
	}

	private void erase(int offset) {
		if (GuiScreen.isCtrlKeyDown()) {
			this.eraseWords(offset);
		} else {
			this.eraseCharacters(offset);
		}
	}

	public void eraseWords(int wordOffset) {
		if (!this.text.isEmpty()) {
			if (this.selectionEnd != this.selectionStart) {
				this.write("");
			} else {
				this.eraseCharacters(this.getWordSkipPosition(wordOffset) - this.selectionStart);
			}
		}
	}

	public void eraseCharacters(int characterOffset) {
		if (!this.text.isEmpty()) {
			if (this.selectionEnd != this.selectionStart) {
				this.write("");
			} else {
				int i = this.getCursorPosWithOffset(characterOffset);
				int j = Math.min(i, this.selectionStart);
				int k = Math.max(i, this.selectionStart);
				if (j != k) {
					String string = new StringBuilder(this.text).delete(j, k).toString();
					if (this.textPredicate.test(string)) {
						this.text = string;
						this.setCursor(j);
						this.onChanged(this.text);
					}
				}
			}
		}
	}

	public int getWordSkipPosition(int wordOffset) {
		return this.getWordSkipPosition(wordOffset, this.getCursor());
	}

	private int getWordSkipPosition(int wordOffset, int cursorPosition) {
		return this.getWordSkipPosition(wordOffset, cursorPosition, true);
	}

	private int getWordSkipPosition(int wordOffset, int cursorPosition, boolean skipOverSpaces) {
		int i = cursorPosition;
		boolean bl = wordOffset < 0;
		int j = Math.abs(wordOffset);

		for (int k = 0; k < j; k++) {
			if (!bl) {
				int l = this.text.length();
				i = this.text.indexOf(32, i);
				if (i == -1) {
					i = l;
				} else {
					while (skipOverSpaces && i < l && this.text.charAt(i) == ' ') {
						i++;
					}
				}
			} else {
				while (skipOverSpaces && i > 0 && this.text.charAt(i - 1) == ' ') {
					i--;
				}

				while (i > 0 && this.text.charAt(i - 1) != ' ') {
					i--;
				}
			}
		}

		return i;
	}

	public int getCursor() {
		return this.selectionStart;
	}

	public void setCursor(int cursor) {
		this.setSelectionStart(cursor);
		if (!this.selecting) {
			this.setSelectionEnd(this.selectionStart);
		}

		this.onChanged(this.text);
	}

	public void moveCursor(int offset) {
		this.setCursor(this.getCursorPosWithOffset(offset));
	}

	private int getCursorPosWithOffset(int offset) {
		return this.selectionStart + offset;
	}

	public void setSelectionStart(int cursor) {
		this.selectionStart = MathHelper.clamp_int(cursor, 0, this.text.length());
	}

	public void setCursorToStart() {
		this.setCursor(0);
	}

	public void setCursorToEnd() {
		this.setCursor(this.text.length());
	}

	public void setSelectionEnd(int index) {
		int i = this.text.length();
		this.selectionEnd = MathHelper.clamp_int(index, 0, i);
		if (this.textRenderer != null) {
			if (this.firstCharacterIndex > i) {
				this.firstCharacterIndex = i;
			}

			int j = this.getInnerWidth();
			String string = this.textRenderer.trimStringToWidth(this.text.substring(this.firstCharacterIndex), j);
			int k = string.length() + this.firstCharacterIndex;
			if (this.selectionEnd == this.firstCharacterIndex) {
				this.firstCharacterIndex = this.firstCharacterIndex
						- this.textRenderer.trimStringToWidth(this.text, j, true).length();
			}

			if (this.selectionEnd > k) {
				this.firstCharacterIndex = this.firstCharacterIndex + (this.selectionEnd - k);
			} else if (this.selectionEnd <= this.firstCharacterIndex) {
				this.firstCharacterIndex = this.firstCharacterIndex - (this.firstCharacterIndex - this.selectionEnd);
			}

			this.firstCharacterIndex = MathHelper.clamp_int(this.firstCharacterIndex, 0, i);
		}
	}

	public boolean isActive() {
		return this.isVisible() && this.isFocused() && this.isEditable();
	}

	@Override
	public boolean keyTyped(char typedChar, int keyCode) {
		this.pages.forEach(page2 -> page2.getOptions().stream().filter(OptionExtended.class::isInstance)
				.map(OptionExtended.class::cast).forEach(optionExtended2 -> optionExtended2.setSelected(false)));
		if (!this.isActive()) {
			return false;
		} else {
			this.selecting = GuiScreen.isShiftKeyDown();
			if (keyCode == 30 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown()) {
				this.setCursorToEnd();
				this.setSelectionEnd(0);
				return true;
			} else if (keyCode == 46 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown()) {
				GuiScreen.setClipboardString(this.getSelectedText());
				return true;
			} else if (keyCode == 47 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown()) {
				if (this.editable) {
					this.write(GuiScreen.getClipboardString());
				}

				return true;
			} else if (keyCode == 45 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown()) {
				GuiScreen.setClipboardString(this.getSelectedText());
				if (this.editable) {
					this.write("");
				}

				return true;
			} else {
				switch (keyCode) {
				case 14:
					if (this.editable) {
						this.selecting = false;
						this.erase(-1);
						this.selecting = GuiScreen.isShiftKeyDown();
					}

					return true;
				case 28:
					if (this.editable) {
						int count = 0;

						for (OptionPage page : this.pages) {
							UnmodifiableIterator var6 = page.getOptions().iterator();

							while (var6.hasNext()) {
								Option<?> option = (Option<?>) var6.next();
								if (option instanceof OptionExtended) {
									OptionExtended<?> optionExtended = (OptionExtended<?>) option;
									if (optionExtended.isHighlight() && optionExtended.getParentDimension() != null) {
										if (count == (Integer) this.lastSearchIndex.get()) {
											Dim2i optionDim = optionExtended.getDim2i();
											if (optionDim == null) {
												return false;
											}

											Dim2i parentDim = optionExtended.getParentDimension();
											int maxOffset = parentDim.getHeight() - this.tabDimHeight;
											int input = optionDim.getOriginY() - parentDim.getOriginY();
											int inputOffset = input + optionDim.getHeight() == parentDim.getHeight()
													? parentDim.getHeight()
													: input;
											int offset = inputOffset * maxOffset / parentDim.getHeight();
											int total = this.pages.stream()
													.mapToInt(page2 -> Math.toIntExact(page2.getOptions().stream()
															.filter(OptionExtended.class::isInstance)
															.map(OptionExtended.class::cast)
															.filter(OptionExtended::isHighlight).count()))
													.sum();
											int value = total == this.lastSearchIndex.get() + 1 ? 0
													: (Integer) this.lastSearchIndex.get() + 1;
											optionExtended.setSelected(true);
											this.lastSearchIndex.set(value);
											this.tabFrameSelectedTab.set(page.getName());
											this.tabFrameScrollBarOffset.set(0);
											this.optionPageScrollBarOffset.set(offset);
											this.setFocused(false);
											this.sodiumVideoOptionsScreen.rebuildGUI();
											return true;
										}

										count++;
									}
								}
							}
						}
					}

					return true;
				case 199:
					this.setCursorToStart();
					return true;
				case 203:
					if (GuiScreen.isCtrlKeyDown()) {
						this.setCursor(this.getWordSkipPosition(-1));
					} else {
						this.moveCursor(-1);
					}

					boolean state2 = this.getCursor() != this.lastCursorPosition && this.getCursor() != 0;
					this.lastCursorPosition = this.getCursor();
					return state2;
				case 205:
					if (GuiScreen.isCtrlKeyDown()) {
						this.setCursor(this.getWordSkipPosition(1));
					} else {
						this.moveCursor(1);
					}

					boolean state = this.getCursor() != this.lastCursorPosition
							&& this.getCursor() != this.text.length() + 1;
					this.lastCursorPosition = this.getCursor();
					return state;
				case 207:
					this.setCursorToEnd();
					return true;
				case 211:
					if (this.editable) {
						this.selecting = false;
						this.erase(1);
						this.selecting = GuiScreen.isShiftKeyDown();
					}

					return true;
				default:
					if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
						if (this.editable) {
							this.lastSearch.set(this.text.trim());
							this.write(Character.toString(typedChar));
							this.lastSearchIndex.set(0);
						}

						return true;
					} else {
						return false;
					}
				}
			}
		}
	}

	public int getInnerWidth() {
		return this.dim.getWidth() - 12;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public boolean isEditable() {
		return this.editable;
	}

	public void setFocused(boolean focused) {
		this.focused = focused;
	}

	public boolean isFocused() {
		return this.focused;
	}
}
