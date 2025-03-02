package com.soarclient.gui.modmenu.pages.profile;

import org.lwjgl.glfw.GLFW;

import com.soarclient.gui.api.SoarGui;
import com.soarclient.gui.api.page.Page;
import com.soarclient.gui.api.page.impl.RightTransition;
import com.soarclient.skia.font.Icon;

public class ProfileAddPage extends Page {

    private Class<? extends Page> prevPage;

    public ProfileAddPage(SoarGui parent, Class<? extends Page> prevPage) {
        super(parent, "text.profile", Icon.DESCRIPTION, new RightTransition(true));
        this.prevPage = prevPage;
    }

    @Override
    public void draw(double mouseX, double mouseY) {
        super.draw(mouseX, mouseY);
    }

    @Override
	public void mousePressed(double mouseX, double mouseY, int button) {
        super.mousePressed(mouseX, mouseY, button);
    }

    @Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
	public void charTyped(char chr, int modifiers) {
        super.charTyped(chr, modifiers);
    }

    @Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);

        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
			parent.setClosable(true);
			parent.setCurrentPage(prevPage);
		}
    }

    @Override
	public void onClosed() {
		if (!parent.isClosable()) {
			parent.setClosable(true);
			parent.getPage(prevPage).onClosed();
		}
	}
}
