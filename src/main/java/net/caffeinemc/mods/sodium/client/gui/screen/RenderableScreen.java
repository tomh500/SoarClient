package net.caffeinemc.mods.sodium.client.gui.screen;

import dev.vexor.radium.compat.mojang.minecraft.gui.Renderable;
import dev.vexor.radium.compat.mojang.minecraft.gui.event.GuiEventListener;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.gui.GuiScreen;

public class RenderableScreen extends GuiScreen {
    protected final List<Renderable> widgets = new ArrayList<>();

    @Override
    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, tickDelta);

        if (Mouse.hasWheel()) {
            int dWheel = Mouse.getDWheel();

            getEventListeners().forEach(el -> el.mouseScrolled(mouseX, mouseY, dWheel, dWheel));
        }

        widgets.forEach(renderable -> renderable.render(mouseX, mouseY, tickDelta));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        try {
			super.mouseClicked(mouseX, mouseY, button);
		} catch (IOException e) {
			e.printStackTrace();
		}
        getEventListeners().forEach(el -> el.mouseClicked(mouseX, mouseY, button));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        getEventListeners().forEach(el -> el.mouseReleased(mouseX, mouseY, button));
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int button, long mouseLastClicked) {
        super.mouseClickMove(mouseX, mouseY, button, mouseLastClicked);
        getEventListeners().forEach(el -> el.mouseDragged(mouseX, mouseY, button));
    }

    @Override
    protected void keyTyped(char id, int code) {
        try {
			super.keyTyped(id, code);
		} catch (IOException e) {
			e.printStackTrace();
		}
        getEventListeners().forEach(el -> el.keyPressed(code, id));
    }

    public void clearWidgets() {
        this.widgets.clear();
    }

    public void addRenderableWidget(Renderable renderable) {
        this.widgets.add(renderable);
    }

    protected List<GuiEventListener> getEventListeners() {
        return widgets.stream().filter(it -> it instanceof GuiEventListener).map(GuiEventListener.class::cast).collect(Collectors.toList());
    }
}
