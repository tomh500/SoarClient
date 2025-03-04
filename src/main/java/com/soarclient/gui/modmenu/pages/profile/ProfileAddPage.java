package com.soarclient.gui.modmenu.pages.profile;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.soarclient.Soar;
import com.soarclient.gui.api.SoarGui;
import com.soarclient.gui.api.page.Page;
import com.soarclient.gui.api.page.SimplePage;
import com.soarclient.gui.api.page.impl.RightTransition;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.management.config.ConfigType;
import com.soarclient.management.profile.ProfileIcon;
import com.soarclient.skia.Skia;
import com.soarclient.skia.font.Fonts;
import com.soarclient.skia.font.Icon;
import com.soarclient.ui.component.Component;
import com.soarclient.ui.component.handler.impl.ButtonHandler;
import com.soarclient.ui.component.impl.Button;
import com.soarclient.ui.component.impl.text.TextField;
import com.soarclient.utils.language.I18n;

import net.minecraft.client.MinecraftClient;

public class ProfileAddPage extends SimplePage {

    private List<Component> components = new ArrayList<>();
    private Class<? extends Page> prevPage;
    private TextField nameField, addressField;
    private Button addButton;
    private Object currentIcon;

    public ProfileAddPage(SoarGui parent, Class<? extends Page> prevPage) {
        super(parent, "text.profile", Icon.DESCRIPTION, new RightTransition(true));
        this.prevPage = prevPage;
        addButton = new Button(I18n.get("text.add"), 0, 0, Button.Style.TONAL);
    }

    @Override
    public void init() {

        MinecraftClient client = MinecraftClient.getInstance();

        components.clear();
        super.init();
        parent.setClosable(false);

        float offset = 26;

        nameField = new TextField(x, y + 2280, 180, "");
        addressField = new TextField(x, y + 2280, 180, "");
        addButton = new Button("text.add", x + (width - offset) - addButton.getWidth() - 16,
                y + (height - offset) - addButton.getHeight() - 16, Button.Style.TONAL);

        addButton.setHandler(new ButtonHandler() {
            @Override
            public void onAction() {
                if(!nameField.getText().isEmpty() && !addressField.getText().isEmpty()) {
                    Soar.getInstance().getProfileManager().save(nameField.getText(),
                    client.getGameProfile().getName(), addressField.getText(), currentIcon, ConfigType.MOD);
                    parent.setClosable(true);
                    parent.setCurrentPage(prevPage);
                }
            }
        });

        components.add(nameField);
        components.add(addressField);
        components.add(addButton);
    }

    @Override
    public void draw(double mouseX, double mouseY) {
        super.draw(mouseX, mouseY);

        ColorPalette palette = Soar.getInstance().getColorManager().getPalette();
        float offset = 26;
        float itemX = x + offset;
        float itemY = y + offset;

        Skia.drawRoundedRect(itemX, itemY, width - offset * 2, height - offset * 2, 24, palette.getSurface());

        Skia.drawText(I18n.get("text.createprofile"), itemX + 20, itemY + 20, palette.getOnSurface(),
                Fonts.getMedium(28));
        Skia.drawText(I18n.get("text.icon"), itemX + 20, itemY + 72, palette.getOnSurfaceVariant(),
                Fonts.getMedium(26));

        for (ProfileIcon icon : ProfileIcon.values()) {

            if (currentIcon == null) {
                currentIcon = ProfileIcon.COMMAND;
            }

            float iconSize = 72;
            float iconX = x + offset + 20 + (icon.ordinal() * iconSize) + (icon.ordinal() * 12);
            float iconY = y + offset + 106;

            if (currentIcon.equals(icon)) {

                float borderSize = 4;

                Skia.drawRoundedRect(iconX - borderSize, iconY - borderSize, iconSize + borderSize * 2,
                        iconSize + borderSize * 2, 12 + (borderSize / 2), palette.getTertiaryContainer());
            }

            Skia.drawRoundedImage(icon.getIconPath(), iconX, iconY, iconSize, iconSize, 12);
        }

        for (Component component : components) {
            component.draw(mouseX, mouseY);
        }
    }

    @Override
    public void mousePressed(double mouseX, double mouseY, int button) {
        super.mousePressed(mouseX, mouseY, button);

        for (Component component : components) {
            component.mousePressed(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);

        for (Component component : components) {
            component.mouseReleased(mouseX, mouseY, button);
        }
    }

    @Override
    public void charTyped(char chr, int modifiers) {
        super.charTyped(chr, modifiers);

        for (Component component : components) {
            component.charTyped(chr, modifiers);
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);

        for (Component component : components) {
            component.keyPressed(keyCode, scanCode, modifiers);
        }

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
