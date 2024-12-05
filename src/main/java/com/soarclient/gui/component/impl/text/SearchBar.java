package com.soarclient.gui.component.impl.text;

import org.lwjgl.input.Keyboard;

import com.soarclient.Soar;
import com.soarclient.animation.Animation;
import com.soarclient.animation.Duration;
import com.soarclient.animation.cubicbezier.impl.EaseStandard;
import com.soarclient.animation.other.DummyAnimation;
import com.soarclient.gui.component.Component;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Fonts;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.utils.ColorUtils;
import com.soarclient.utils.language.I18n;
import com.soarclient.utils.mouse.MouseUtils;

public class SearchBar extends Component {

	private Runnable shortcutEvent;
	
	private Animation cursorAnimation;
	private Animation cursorFlashAnimation;
	private Animation hintTextAnimation;

	private TextInputHelper input = new TextInputHelper();
	private String hintText;

	public SearchBar(float x, float y, float width, Runnable shortcutEvent) {
		super(x, y);
		this.shortcutEvent = shortcutEvent;
		this.width = width;
		this.height = 42;
		this.hintTextAnimation = new DummyAnimation(0, 1);
		this.cursorAnimation = new DummyAnimation(0, 1);
		this.cursorFlashAnimation = new DummyAnimation(0, 0);
		this.hintText = "text.search";
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();
		NanoVGHelper nvg = NanoVGHelper.getInstance();

		float hintTextValue = hintTextAnimation.getValue();

		nvg.drawRoundedRect(x, y, width, height, 20, palette.getSurface());
		nvg.drawText(Icon.SEARCH, x + 12, y + 11.5F, palette.getOnSurface(), 24, Fonts.ICON);
		nvg.drawText(I18n.get(hintText), x + 40 - (25 * (1 - hintTextValue)), y + 15F,
				ColorUtils.applyAlpha(palette.getOnSurfaceVariant(), (int) (hintTextValue * 255)), 16F, Fonts.REGULAR);
		
		drawCursor();
		
		if (!getText().isEmpty() || isFocused()) {
			nvg.drawText(getText(), x + 40, y + 15F, palette.getOnSurfaceVariant(), 16F, Fonts.REGULAR);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_F) && !isFocused()) {

			if (getText().isEmpty()) {
				hintTextAnimation = new EaseStandard(Duration.MEDIUM_1, hintTextAnimation.getValue(), 0);
			}
			
			shortcutEvent.run();
			input.setFocused(true);
		}
	}

	private void drawCursor() {

		NanoVGHelper nvg = NanoVGHelper.getInstance();

		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();
		int selectionEnd = input.getSelectionEnd();
		int cursorPosition = input.getCursorPosition();
		String text = getText();
		boolean focused = isFocused();

		if (focused && cursorPosition == selectionEnd) {
			if (cursorFlashAnimation.getEnd() == 0 && cursorFlashAnimation.isFinished()) {
				cursorFlashAnimation = new EaseStandard(Duration.SHORT_4, 0, 1);
			} else if (cursorFlashAnimation.getEnd() == 1 && cursorFlashAnimation.isFinished()) {
				cursorFlashAnimation = new EaseStandard(Duration.SHORT_4, 1, 0);
			}
		} else {
			if (cursorFlashAnimation.getEnd() != 0) {
				cursorFlashAnimation = new EaseStandard(Duration.SHORT_4, 1, 0);
			}
		}

		float cursorResult = nvg.getTextWidth(text.substring(0, cursorPosition), 16, Fonts.REGULAR);

		cursorAnimation = new EaseStandard(Duration.SHORT_1,
				(cursorAnimation instanceof DummyAnimation) ? cursorResult : cursorAnimation.getValue(), cursorResult);

		nvg.drawRect(x + 40 + cursorAnimation.getValue(), y + 9, 1, 24,
				ColorUtils.applyAlpha(palette.getSurfaceTint(), (int) (cursorFlashAnimation.getValue() * 255)));

		if (cursorPosition != selectionEnd) {

			int start = selectionEnd > cursorPosition ? cursorPosition : selectionEnd;
			int end = selectionEnd > cursorPosition ? selectionEnd : cursorPosition;

			float selectionWidth = nvg.getTextWidth(text.substring(start, end), 16, Fonts.REGULAR);
			float offset = nvg.getTextWidth(text.substring(0, start), 16, Fonts.REGULAR);

			if (!(cursorAnimation instanceof DummyAnimation)) {
				cursorAnimation = new DummyAnimation();
			}

			nvg.drawRect(x + 40 + offset, y + 9, selectionWidth, 24, palette.getSurfaceTint());
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		boolean isInside = MouseUtils.isInside(mouseX, mouseY, x, y, width, height);

		if (mouseButton == 0) {

			if (isInside && !isFocused()) {
				
				if (getText().isEmpty()) {
					hintTextAnimation = new EaseStandard(Duration.MEDIUM_1, hintTextAnimation.getValue(), 0);
				}
				
				input.setFocused(true);
			} else if (!isInside && isFocused()) {
				
				if (getText().isEmpty()) {
					hintTextAnimation = new EaseStandard(Duration.MEDIUM_1, hintTextAnimation.getValue(), 1);
				}
				
				input.setFocused(false);
			}
		}
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
		input.keyTyped(typedChar, keyCode);
	}

	public String getHintText() {
		return hintText;
	}

	public void setHintText(String hintText) {
		this.hintText = hintText;
	}

	public String getText() {
		return input.getText();
	}

	public void setText(String text) {
		input.setText(text);
	}

	public boolean isFocused() {
		return input.isFocused();
	}
}
