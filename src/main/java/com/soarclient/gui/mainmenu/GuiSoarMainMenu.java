package com.soarclient.gui.mainmenu;

import org.lwjgl.input.Mouse;

import com.soarclient.Soar;
import com.soarclient.animation.Animation;
import com.soarclient.animation.SimpleAnimation;
import com.soarclient.animation.cubicbezier.impl.EaseEmphasizedDecelerate;
import com.soarclient.gui.SoarGui;
import com.soarclient.libraries.material3.hct.Hct;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.management.video.VideoManager;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.shaders.blur.GaussianBlur;

import net.minecraft.client.gui.ScaledResolution;

public class GuiSoarMainMenu extends SoarGui {

	private static boolean FIRST_IN = true;

	private Animation inAnimation;
	private GaussianBlur gaussianBlur = new GaussianBlur(false);
	private SimpleAnimation[] backgroundAnimations = new SimpleAnimation[2];
	private ColorPalette palette;

	public GuiSoarMainMenu() {
		
		for (int i = 0; i < backgroundAnimations.length; i++) {
			backgroundAnimations[i] = new SimpleAnimation();
		}
		
		palette = new ColorPalette(Hct.fromInt(Soar.getInstance().getVideoManager().getVideo().getHct()), false);
	}

	@Override
	public void init() {
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		ScaledResolution sr = new ScaledResolution(mc);
		NanoVGHelper nvg = NanoVGHelper.getInstance();
		VideoManager videoManager = Soar.getInstance().getVideoManager();

		backgroundAnimations[0].onTick(Mouse.getX(), 16);
		backgroundAnimations[1].onTick(Mouse.getY(), 16);
		
		nvg.setupAndDraw(() -> {
			if(inAnimation != null) {
				nvg.save();
				nvg.scale(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 3 - (inAnimation.getValue() * 2));
				nvg.drawImage(videoManager.getEngine().getTexture(), -21 + backgroundAnimations[0].getValue() / 90,
						backgroundAnimations[1].getValue() * -1 / 90, sr.getScaledWidth() + 21, sr.getScaledHeight() + 20,
						1F);
				nvg.restore();
			}
		});

		if (inAnimation != null && inAnimation.getValue() != 1.0) {
			gaussianBlur.draw(50 - (49 * inAnimation.getValue()));
		}
		
		if (FIRST_IN) {
			inAnimation = new EaseEmphasizedDecelerate(8000, 0, 1);
			FIRST_IN = false;
		}
	}

	@Override
	public void onClosed() {
		Soar.getInstance().getVideoManager().getEngine().close();
	}
}
