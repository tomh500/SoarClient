package net.optifine.entity.model;

import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityHorse;

public class ModelAdapterHorse extends ModelAdapter {
	private static final Map<String, Integer> mapPartFields = null;

	public ModelAdapterHorse() {
		super(EntityHorse.class, "horse", 0.75F);
	}

	protected ModelAdapterHorse(Class entityClass, String name, float shadowSize) {
		super(entityClass, name, shadowSize);
	}

	public ModelBase makeModel() {
		return new ModelHorse();
	}

	public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
		if (!(model instanceof ModelHorse modelhorse)) {
			return null;
		} else {

			return modelPart.equals("head") ? modelhorse.head
					: (modelPart.equals("upper_mouth") ? modelhorse.field_178711_b
							: (modelPart.equals("lower_mouth") ? modelhorse.field_178712_c
									: (modelPart.equals("horse_left_ear") ? modelhorse.horseLeftEar
											: (modelPart.equals("horse_right_ear") ? modelhorse.horseRightEar
													: (modelPart.equals("mule_left_ear") ? modelhorse.muleLeftEar
															: (modelPart.equals("mule_right_ear")
																	? modelhorse.muleRightEar
																	: (modelPart.equals("neck") ? modelhorse.neck
																			: (modelPart.equals("horse_face_ropes")
																					? modelhorse.horseFaceRopes
																					: (modelPart.equals("mane")
																							? modelhorse.mane
																							: (modelPart.equals("body")
																									? modelhorse.body
																									: (modelPart.equals(
																											"tail_base")
																													? modelhorse.tailBase
																													: (modelPart
																															.equals("tail_middle")
																																	? modelhorse.tailMiddle
																																	: (modelPart
																																			.equals("tail_tip")
																																					? modelhorse.tailTip
																																					: (modelPart
																																							.equals("back_left_leg")
																																									? modelhorse.backLeftLeg
																																									: (modelPart
																																											.equals("back_left_shin")
																																													? modelhorse.backLeftShin
																																													: (modelPart
																																															.equals("back_left_hoof")
																																																	? modelhorse.backLeftHoof
																																																	: (modelPart
																																																			.equals("back_right_leg")
																																																					? modelhorse.backRightLeg
																																																					: (modelPart
																																																							.equals("back_right_shin")
																																																									? modelhorse.backRightShin
																																																									: (modelPart
																																																											.equals("back_right_hoof")
																																																													? modelhorse.backRightHoof
																																																													: (modelPart
																																																															.equals("front_left_leg")
																																																																	? modelhorse.frontLeftLeg
																																																																	: (modelPart
																																																																			.equals("front_left_shin")
																																																																					? modelhorse.frontLeftShin
																																																																					: (modelPart
																																																																							.equals("front_left_hoof")
																																																																									? modelhorse.frontLeftHoof
																																																																									: (modelPart
																																																																											.equals("front_right_leg")
																																																																													? modelhorse.frontRightLeg
																																																																													: (modelPart
																																																																															.equals("front_right_shin")
																																																																																	? modelhorse.frontRightShin
																																																																																	: (modelPart
																																																																																			.equals("front_right_hoof")
																																																																																					? modelhorse.frontRightHoof
																																																																																					: (modelPart
																																																																																							.equals("mule_left_chest")
																																																																																									? modelhorse.muleLeftChest
																																																																																									: (modelPart
																																																																																											.equals("mule_right_chest")
																																																																																													? modelhorse.muleRightChest
																																																																																													: (modelPart
																																																																																															.equals("horse_saddle_bottom")
																																																																																																	? modelhorse.horseSaddleBottom
																																																																																																	: (modelPart
																																																																																																			.equals("horse_saddle_front")
																																																																																																					? modelhorse.horseSaddleFront
																																																																																																					: (modelPart
																																																																																																							.equals("horse_saddle_back")
																																																																																																									? modelhorse.horseSaddleBack
																																																																																																									: (modelPart
																																																																																																											.equals("horse_left_saddle_rope")
																																																																																																													? modelhorse.horseLeftSaddleRope
																																																																																																													: (modelPart
																																																																																																															.equals("horse_left_saddle_metal")
																																																																																																																	? modelhorse.horseLeftSaddleMetal
																																																																																																																	: (modelPart
																																																																																																																			.equals("horse_right_saddle_rope")
																																																																																																																					? modelhorse.horseRightSaddleRope
																																																																																																																					: (modelPart
																																																																																																																							.equals("horse_right_saddle_metal")
																																																																																																																									? modelhorse.horseRightSaddleMetal
																																																																																																																									: (modelPart
																																																																																																																											.equals("horse_left_face_metal")
																																																																																																																													? modelhorse.horseLeftFaceMetal
																																																																																																																													: (modelPart
																																																																																																																															.equals("horse_right_face_metal")
																																																																																																																																	? modelhorse.horseRightFaceMetal
																																																																																																																																	: (modelPart
																																																																																																																																			.equals("horse_left_rein")
																																																																																																																																					? modelhorse.horseLeftRein
																																																																																																																																					: (modelPart
																																																																																																																																							.equals("horse_right_rein")
																																																																																																																																									? modelhorse.horseRightRein
																																																																																																																																									: null))))))))))))))))))))))))))))))))))))));
		}
	}

	public String[] getModelRendererNames() {
		return new String[] { "head", "upper_mouth", "lower_mouth", "horse_left_ear", "horse_right_ear",
				"mule_left_ear", "mule_right_ear", "neck", "horse_face_ropes", "mane", "body", "tail_base",
				"tail_middle", "tail_tip", "back_left_leg", "back_left_shin", "back_left_hoof", "back_right_leg",
				"back_right_shin", "back_right_hoof", "front_left_leg", "front_left_shin", "front_left_hoof",
				"front_right_leg", "front_right_shin", "front_right_hoof", "mule_left_chest", "mule_right_chest",
				"horse_saddle_bottom", "horse_saddle_front", "horse_saddle_back", "horse_left_saddle_rope",
				"horse_left_saddle_metal", "horse_right_saddle_rope", "horse_right_saddle_metal",
				"horse_left_face_metal", "horse_right_face_metal", "horse_left_rein", "horse_right_rein" };
	}

	public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		RenderHorse renderhorse = new RenderHorse(rendermanager, (ModelHorse) modelBase, shadowSize);
		return renderhorse;
	}
}
