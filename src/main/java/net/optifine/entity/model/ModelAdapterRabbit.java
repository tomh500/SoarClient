package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRabbit;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderRabbit;
import net.minecraft.entity.passive.EntityRabbit;

public class ModelAdapterRabbit extends ModelAdapter {

	public ModelAdapterRabbit() {
		super(EntityRabbit.class, "rabbit", 0.3F);
	}

	public ModelBase makeModel() {
		return new ModelRabbit();
	}

	public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
		if (!(model instanceof ModelRabbit modelrabbit)) {
			return null;
		} else {

            return modelPart.equals("left_foot") ? modelrabbit.rabbitLeftFoot
					: (modelPart.equals("right_foot") ? modelrabbit.rabbitRightFoot
							: (modelPart.equals("left_thigh") ? modelrabbit.rabbitLeftThigh
									: (modelPart.equals("right_thigh") ? modelrabbit.rabbitRightThigh
											: (modelPart.equals("body") ? modelrabbit.rabbitBody
													: (modelPart.equals("left_arm") ? modelrabbit.rabbitLeftArm
															: (modelPart.equals("right_arm")
																	? modelrabbit.rabbitRightArm
																	: (modelPart.equals("head") ? modelrabbit.rabbitHead
																			: (modelPart.equals("right_ear")
																					? modelrabbit.rabbitRightEar
																					: (modelPart.equals("left_ear")
																							? modelrabbit.rabbitLeftEar
																							: (modelPart.equals("tail")
																									? modelrabbit.rabbitTail
																									: (modelPart.equals(
																											"nose") ? modelrabbit.rabbitNose
																													: null)))))))))));
		}
	}

	public String[] getModelRendererNames() {
		return new String[] { "left_foot", "right_foot", "left_thigh", "right_thigh", "body", "left_arm", "right_arm",
				"head", "right_ear", "left_ear", "tail", "nose" };
	}

	public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		RenderRabbit renderrabbit = new RenderRabbit(rendermanager, modelBase, shadowSize);
		return renderrabbit;
	}
}
