package dev.vexor.radium.extra.client.gui;

import com.google.common.collect.ImmutableList;
import dev.vexor.radium.extra.client.SodiumExtraClientMod;
import dev.vexor.radium.extra.client.gui.options.control.SliderControlExtended;
import dev.vexor.radium.extra.client.gui.options.storage.SodiumExtraOptionsStorage;
import dev.vexor.radium.extra.util.ControlValueFormatterExtended;
import net.caffeinemc.mods.sodium.client.gui.options.*;
import net.caffeinemc.mods.sodium.client.gui.options.control.CyclingControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.TickBoxControl;
import net.caffeinemc.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import java.util.*;

public class SodiumExtraGameOptionPages {
	public static final SodiumExtraOptionsStorage sodiumExtraOpts = new SodiumExtraOptionsStorage();
	public static final MinecraftOptionsStorage vanillaOpts = new MinecraftOptionsStorage();

	public static OptionPage animation() {
		List<OptionGroup> groups = new ArrayList<>();
		groups.add(OptionGroup.createBuilder().add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
				.setEnabled(() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.animation").isEnabled())
				.setName(new ChatComponentText("Animations"))
				.setTooltip(new ChatComponentTranslation("sodium-extra.option.animations_all.tooltip"))
				.setControl(TickBoxControl::new)
				.setBinding((opts, value) -> opts.animationSettings.animation = value,
						opts -> opts.animationSettings.animation)
				.setFlags(OptionFlag.REQUIRES_ASSET_RELOAD).build()).build());

		groups.add(OptionGroup.createBuilder().add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
				.setEnabled(() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.animation").isEnabled())
				.setName(new ChatComponentText(Blocks.water.getLocalizedName()))
				.setTooltip(new ChatComponentTranslation("sodium-extra.option.animate_water.tooltip"))
				.setControl(TickBoxControl::new)
				.setBinding((opts, value) -> opts.animationSettings.water = value, opts -> opts.animationSettings.water)
				.setFlags(OptionFlag.REQUIRES_ASSET_RELOAD).build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
						.setEnabled(() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.animation")
								.isEnabled())
						.setName(new ChatComponentTranslation(Blocks.lava.getLocalizedName()))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.animate_lava.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.animationSettings.lava = value,
								opts -> opts.animationSettings.lava)
						.setFlags(OptionFlag.REQUIRES_ASSET_RELOAD).build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
						.setEnabled(() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.animation")
								.isEnabled())
						.setName(new ChatComponentTranslation(Blocks.fire.getLocalizedName()))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.animate_fire.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.animationSettings.fire = value,
								opts -> opts.animationSettings.fire)
						.setFlags(OptionFlag.REQUIRES_ASSET_RELOAD).build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
						.setEnabled(() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.animation")
								.isEnabled())
						.setName(new ChatComponentTranslation(Blocks.portal.getLocalizedName()))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.animate_portal.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.animationSettings.portal = value,
								opts -> opts.animationSettings.portal)
						.setFlags(OptionFlag.REQUIRES_ASSET_RELOAD).build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
						.setEnabled(() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.animation")
								.isEnabled())
						.setName(new ChatComponentTranslation("sodium-extra.option.block_animations"))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.block_animations.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((options, value) -> options.animationSettings.blockAnimations = value,
								options -> options.animationSettings.blockAnimations)
						.setFlags(OptionFlag.REQUIRES_ASSET_RELOAD).build())
				.build());
		return new OptionPage(new ChatComponentTranslation("sodium-extra.option.animations"),
				ImmutableList.copyOf(groups));
	}

	public static OptionPage particle() {
		List<OptionGroup> groups = new ArrayList<>();
		groups.add(OptionGroup.createBuilder().add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
				.setEnabled(() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.particle").isEnabled())
				.setName(new ChatComponentText("Particles"))
				.setTooltip(new ChatComponentTranslation("sodium-extra.option.particles_all.tooltip"))
				.setControl(TickBoxControl::new).setBinding((opts, value) -> opts.particleSettings.particles = value,
						opts -> opts.particleSettings.particles)
				.build()).build());

		groups.add(OptionGroup.createBuilder().add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
				.setEnabled(() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.particle").isEnabled())
				.setName(new ChatComponentText("Rain Splash"))
				.setTooltip(new ChatComponentTranslation("sodium-extra.option.rain_splash.tooltip"))
				.setControl(TickBoxControl::new)
				.setBinding((opts, value) -> opts.particleSettings.rainSplash = value,
						opts -> opts.particleSettings.rainSplash)
				.build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
						.setEnabled(
								() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.particle").isEnabled())
						.setName(new ChatComponentText("Block Break"))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.block_break.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.particleSettings.blockBreak = value,
								opts -> opts.particleSettings.blockBreak)
						.build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
						.setEnabled(
								() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.particle").isEnabled())
						.setName(new ChatComponentText("Block Breaking"))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.block_breaking.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.particleSettings.blockBreaking = value,
								opts -> opts.particleSettings.blockBreaking)
						.build())
				.build());

		return new OptionPage(new ChatComponentTranslation("options.particles"), ImmutableList.copyOf(groups));
	}

	public static OptionPage detail() {
		List<OptionGroup> groups = new ArrayList<>();
		groups.add(OptionGroup.createBuilder()
				.add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
						.setEnabled(() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.sky").isEnabled())
						.setName(new ChatComponentTranslation("sodium-extra.option.sky"))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.sky.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.detailSettings.sky = value, opts -> opts.detailSettings.sky)
						.build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
						.setEnabled(
								() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.stars").isEnabled())
						.setName(new ChatComponentTranslation("sodium-extra.option.stars"))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.stars.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.detailSettings.stars = value,
								opts -> opts.detailSettings.stars)
						.build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
						.setEnabled(
								() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.sun_moon").isEnabled())
						.setName(new ChatComponentTranslation("sodium-extra.option.sun"))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.sun.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.detailSettings.sun = value, opts -> opts.detailSettings.sun)
						.build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
						.setEnabled(
								() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.sun_moon").isEnabled())
						.setName(new ChatComponentTranslation("sodium-extra.option.moon"))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.moon.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.detailSettings.moon = value, opts -> opts.detailSettings.moon)
						.build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
						.setEnabled(
								() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.particle").isEnabled())
						.setName(new ChatComponentText("Rain & Snow"))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.rain_snow.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.detailSettings.rainSnow = value,
								opts -> opts.detailSettings.rainSnow)
						.build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
						.setEnabled(() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.biome_colors")
								.isEnabled())
						.setName(new ChatComponentTranslation("sodium-extra.option.biome_colors"))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.biome_colors.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((options, value) -> options.detailSettings.biomeColors = value,
								options -> options.detailSettings.biomeColors)
						.setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD).build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
						.setEnabled(() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.sky_colors")
								.isEnabled())
						.setName(new ChatComponentTranslation("sodium-extra.option.sky_colors"))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.sky_colors.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((options, value) -> options.detailSettings.skyColors = value,
								options -> options.detailSettings.skyColors)
						.setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD).build())
				.build());
		return new OptionPage(new ChatComponentTranslation("sodium-extra.option.details"),
				ImmutableList.copyOf(groups));
	}

	public static OptionPage render() {
		List<OptionGroup> groups = new ArrayList<>();

		groups.add(OptionGroup.createBuilder().add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
				.setEnabled(
						() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.render.entity").isEnabled())
				.setName(new ChatComponentText("Item Frame"))
				.setTooltip(new ChatComponentTranslation("sodium-extra.option.item_frames.tooltip"))
				.setControl(TickBoxControl::new)
				.setBinding((opts, value) -> opts.renderSettings.itemFrame = value,
						opts -> opts.renderSettings.itemFrame)
				.build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
						.setEnabled(() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.render.entity")
								.isEnabled())
						.setName(new ChatComponentText("Armor Stand"))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.armor_stands.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((options, value) -> options.renderSettings.armorStand = value,
								options -> options.renderSettings.armorStand)
						.build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
						.setEnabled(() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.render.entity")
								.isEnabled())
						.setName(new ChatComponentText("Painting"))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.paintings.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((options, value) -> options.renderSettings.painting = value,
								options -> options.renderSettings.painting)
						.setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD).build())
				.build());
		groups.add(OptionGroup.createBuilder().add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
				.setEnabled(() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.render.block.entity")
						.isEnabled())
				.setName(new ChatComponentTranslation("sodium-extra.option.beacon_beam"))
				.setTooltip(new ChatComponentTranslation("sodium-extra.option.beacon_beam.tooltip"))
				.setControl(TickBoxControl::new)
				.setBinding((opts, value) -> opts.renderSettings.beaconBeam = value,
						opts -> opts.renderSettings.beaconBeam)
				.build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
						.setEnabled(() -> SodiumExtraClientMod.mixinConfig().getOptions()
								.get("mixin.render.block.entity").isEnabled())
						.setName(new ChatComponentTranslation("sodium-extra.option.enchanting_table_book"))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.enchanting_table_book.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.renderSettings.enchantingTableBook = value,
								opts -> opts.renderSettings.enchantingTableBook)
						.build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
						.setEnabled(() -> SodiumExtraClientMod.mixinConfig().getOptions()
								.get("mixin.render.block.entity").isEnabled())
						.setName(new ChatComponentText("Piston"))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.piston.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((options, value) -> options.renderSettings.piston = value,
								options -> options.renderSettings.piston)
						.build())
				.build());
		groups.add(OptionGroup.createBuilder().add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
				.setEnabled(
						() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.render.entity").isEnabled())
				.setName(new ChatComponentTranslation("sodium-extra.option.item_frame_name_tag"))
				.setTooltip(new ChatComponentTranslation("sodium-extra.option.item_frame_name_tag.tooltip"))
				.setControl(TickBoxControl::new)
				.setBinding((opts, value) -> opts.renderSettings.itemFrameNameTag = value,
						opts -> opts.renderSettings.itemFrameNameTag)
				.build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
						.setEnabled(() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.render.entity")
								.isEnabled())
						.setName(new ChatComponentTranslation("sodium-extra.option.player_name_tag"))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.player_name_tag.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((options, value) -> options.renderSettings.playerNameTag = value,
								options -> options.renderSettings.playerNameTag)
						.build())
				.build());
		return new OptionPage(new ChatComponentTranslation("sodium-extra.option.render"), ImmutableList.copyOf(groups));
	}

	public static OptionPage extra() {
		List<OptionGroup> groups = new ArrayList<>();
		groups.add(OptionGroup.createBuilder().add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
				.setEnabled(() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.reduce_resolution_on_mac")
						.isEnabled() && Minecraft.isRunningOnMac)
				.setName(new ChatComponentTranslation("sodium-extra.option.reduce_resolution_on_mac"))
				.setTooltip(new ChatComponentTranslation("sodium-extra.option.reduce_resolution_on_mac.tooltip"))
				.setEnabled(() -> Minecraft.isRunningOnMac).setImpact(OptionImpact.HIGH).setControl(TickBoxControl::new)
				.setBinding((opts, value) -> opts.extraSettings.reduceResolutionOnMac = value,
						opts -> opts.extraSettings.reduceResolutionOnMac)
				.build()).build());
		groups.add(OptionGroup.createBuilder()
				.add(OptionImpl.createBuilder(SodiumExtraGameOptions.OverlayCorner.class, sodiumExtraOpts)
						.setName(new ChatComponentTranslation("sodium-extra.option.overlay_corner"))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.overlay_corner.tooltip"))
						.setControl(option -> new CyclingControl<>(option, SodiumExtraGameOptions.OverlayCorner.class))
						.setBinding((opts, value) -> opts.extraSettings.overlayCorner = value,
								opts -> opts.extraSettings.overlayCorner)
						.build())
				.add(OptionImpl.createBuilder(SodiumExtraGameOptions.TextContrast.class, sodiumExtraOpts)
						.setName(new ChatComponentTranslation("sodium-extra.option.text_contrast"))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.text_contrast.tooltip"))
						.setControl(option -> new CyclingControl<>(option, SodiumExtraGameOptions.TextContrast.class))
						.setBinding((opts, value) -> opts.extraSettings.textContrast = value,
								opts -> opts.extraSettings.textContrast)
						.build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
						.setName(new ChatComponentTranslation("sodium-extra.option.show_fps"))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.show_fps.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.extraSettings.showFps = value,
								opts -> opts.extraSettings.showFps)
						.build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
						.setName(new ChatComponentTranslation("sodium-extra.option.show_fps_extended"))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.show_fps_extended.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.extraSettings.showFPSExtended = value,
								opts -> opts.extraSettings.showFPSExtended)
						.build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
						.setName(new ChatComponentTranslation("sodium-extra.option.show_coordinates"))
						.setTooltip(new ChatComponentTranslation("sodium-extra.option.show_coordinates.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.extraSettings.showCoords = value,
								opts -> opts.extraSettings.showCoords)
						.build())
				.build());
		groups.add(OptionGroup.createBuilder().add(OptionImpl.createBuilder(boolean.class, vanillaOpts)
				.setName(new ChatComponentTranslation("sodium-extra.option.advanced_item_tooltips"))
				.setTooltip(new ChatComponentTranslation("sodium-extra.option.advanced_item_tooltips.tooltip"))
				.setControl(TickBoxControl::new)
				.setBinding((opts, value) -> opts.advancedItemTooltips = value, opts -> opts.advancedItemTooltips)
				.build()).build());
		groups.add(OptionGroup.createBuilder().add(OptionImpl.createBuilder(boolean.class, sodiumExtraOpts)
				.setEnabled(
						() -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.prevent_shaders").isEnabled())
				.setName(new ChatComponentTranslation("sodium-extra.option.prevent_shaders"))
				.setTooltip(new ChatComponentTranslation("sodium-extra.option.prevent_shaders.tooltip"))
				.setControl(TickBoxControl::new)
				.setBinding((options, value) -> options.extraSettings.preventShaders = value,
						options -> options.extraSettings.preventShaders)
				.setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD).build()).build());

		return new OptionPage(new ChatComponentTranslation("sodium-extra.option.extras"), ImmutableList.copyOf(groups));
	}
}
