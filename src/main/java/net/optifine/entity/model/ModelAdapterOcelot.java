package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderOcelot;
import net.minecraft.entity.passive.EntityOcelot;

public class ModelAdapterOcelot extends ModelAdapter {

	public ModelAdapterOcelot() {
		super(EntityOcelot.class, "ocelot", 0.4F);
	}

	public ModelBase makeModel() {
		return new ModelOcelot();
	}

	public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
		if (!(model instanceof ModelOcelot modelocelot)) {
			return null;
		} else {

			return modelPart
					.equals("back_left_leg")
							? modelocelot.ocelotBackLeftLeg
							: (modelPart.equals("back_right_leg") ? modelocelot.ocelotBackRightLeg
									: (modelPart.equals("front_left_leg") ? modelocelot.ocelotFrontLeftLeg
											: (modelPart.equals("front_right_leg") ? modelocelot.ocelotFrontRightLeg
													: (modelPart.equals("tail") ? modelocelot.ocelotTail
															: (modelPart.equals("tail2") ? modelocelot.ocelotTail2
																	: (modelPart.equals("head") ? modelocelot.ocelotHead
																			: (modelPart.equals("body")
																					? modelocelot.ocelotBody
																					: null)))))));
		}
	}

	public String[] getModelRendererNames() {
		return new String[] { "back_left_leg", "back_right_leg", "front_left_leg", "front_right_leg", "tail", "tail2",
				"head", "body" };
	}

	public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		RenderOcelot renderocelot = new RenderOcelot(rendermanager, modelBase, shadowSize);
		return renderocelot;
	}
}
