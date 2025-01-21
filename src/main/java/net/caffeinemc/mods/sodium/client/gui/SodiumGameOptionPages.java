package net.caffeinemc.mods.sodium.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import net.caffeinemc.mods.sodium.client.gl.arena.staging.MappedStagingBuffer;
import net.caffeinemc.mods.sodium.client.gl.device.RenderDevice;
import net.caffeinemc.mods.sodium.client.gui.options.OptionFlag;
import net.caffeinemc.mods.sodium.client.gui.options.OptionGroup;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpact;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpl;
import net.caffeinemc.mods.sodium.client.gui.options.OptionPage;
import net.caffeinemc.mods.sodium.client.gui.options.control.ControlValueFormatter;
import net.caffeinemc.mods.sodium.client.gui.options.control.CyclingControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.SliderControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.TickBoxControl;
import net.caffeinemc.mods.sodium.client.gui.options.named.GraphicsMode;
import net.caffeinemc.mods.sodium.client.gui.options.named.ParticleMode;
import net.caffeinemc.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import net.caffeinemc.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

// TODO: Rename in Sodium 0.6
public class SodiumGameOptionPages {
	private static final SodiumOptionsStorage sodiumOpts = new SodiumOptionsStorage();
	private static final MinecraftOptionsStorage vanillaOpts = new MinecraftOptionsStorage();

	public static OptionPage general() {
		List<OptionGroup> groups = new ArrayList<>();

		groups.add(OptionGroup.createBuilder()
				.add(OptionImpl.createBuilder(int.class, vanillaOpts)
						.setName(new ChatComponentTranslation("options.renderDistance"))
						.setTooltip(new ChatComponentTranslation("sodium.options.view_distance.tooltip"))
						.setControl(option -> new SliderControl(option, 2, 32, 1, ControlValueFormatter.chunks()))
						.setBinding((options, value) -> options.renderDistanceChunks = (value),
								options -> options.renderDistanceChunks)
						.setImpact(OptionImpact.HIGH).setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD).build())
				.add(OptionImpl.createBuilder(int.class, vanillaOpts)
						.setName(new ChatComponentTranslation("options.gamma"))
						.setTooltip(new ChatComponentTranslation("sodium.options.brightness.tooltip"))
						.setControl(opt -> new SliderControl(opt, 0, 100, 1, ControlValueFormatter.brightness()))
						.setBinding((opts, value) -> opts.gammaSetting = (value * 0.01f),
								(opts) -> (int) (opts.gammaSetting / 0.01D))
						.build())
				.build());

		groups.add(OptionGroup.createBuilder()
				.add(OptionImpl.createBuilder(int.class, vanillaOpts)
						.setName(new ChatComponentTranslation("options.guiScale"))
						.setTooltip(new ChatComponentTranslation("sodium.options.gui_scale.tooltip"))
						.setControl(option -> new SliderControl(option, 0, 3, 1, ControlValueFormatter.guiScale()))
						.setBinding((opts, value) -> {
							opts.guiScale = value;

							// Resizing our window
							if (Minecraft.getMinecraft().currentScreen instanceof SodiumOptionsGUI) {
								Minecraft.getMinecraft().displayGuiScreen(new SodiumOptionsGUI(
										((SodiumOptionsGUI) Minecraft.getMinecraft().currentScreen).prevScreen));
							}
						}, opts -> opts.guiScale).build())
				.add(OptionImpl.createBuilder(boolean.class, vanillaOpts)
						.setName(new ChatComponentTranslation("options.fullscreen"))
						.setTooltip(new ChatComponentTranslation("sodium.options.fullscreen.tooltip"))
						.setControl(TickBoxControl::new).setBinding((opts, value) -> {
							opts.fullScreen = (value);

							Minecraft client = Minecraft.getMinecraft();

							if (client.isFullScreen() != opts.fullScreen) {
								client.toggleFullscreen();

								// The client might not be able to enter full-screen mode
								opts.fullScreen = (client.isFullScreen());
							}
						}, (opts) -> opts.fullScreen).build())
				.add(OptionImpl.createBuilder(boolean.class, vanillaOpts)
						.setName(new ChatComponentTranslation("options.vsync"))
						.setTooltip(new ChatComponentTranslation("sodium.options.v_sync.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.enableVsync = value, opts -> opts.enableVsync)
						.setImpact(OptionImpact.VARIES).build())
				.add(OptionImpl.createBuilder(int.class, vanillaOpts)
						.setName(new ChatComponentTranslation("options.framerateLimit"))
						.setTooltip(new ChatComponentTranslation("sodium.options.fps_limit.tooltip"))
						.setControl(option -> new SliderControl(option, 10, 260, 10, ControlValueFormatter.fpsLimit()))
						.setBinding((opts, value) -> {
							opts.limitFramerate = (value);
						}, opts -> opts.limitFramerate).build())
				.build());

		groups.add(OptionGroup.createBuilder()
				.add(OptionImpl.createBuilder(boolean.class, vanillaOpts)
						.setName(new ChatComponentTranslation("options.viewBobbing"))
						.setTooltip(new ChatComponentTranslation("sodium.options.view_bobbing.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.viewBobbing = value, opts -> opts.viewBobbing).build())
				.build());

		return new OptionPage(new ChatComponentTranslation("stat.generalButton"), ImmutableList.copyOf(groups));
	}

	public static OptionPage quality() {
		List<OptionGroup> groups = new ArrayList<>();

		groups.add(OptionGroup.createBuilder()
				.add(OptionImpl.createBuilder(GraphicsMode.class, vanillaOpts)
						.setName(new ChatComponentTranslation("options.graphics"))
						.setTooltip(new ChatComponentTranslation("sodium.options.graphics_quality.tooltip"))
						.setControl(option -> new CyclingControl<>(option, GraphicsMode.class))
						.setBinding((opts, value) -> opts.fancyGraphics = value.isFancy(),
								opts -> GraphicsMode.fromBoolean(opts.fancyGraphics))
						.setImpact(OptionImpact.HIGH).setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD).build())
				.build());

		groups.add(OptionGroup.createBuilder()
				.add(OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("options.renderClouds"))
						.setTooltip(new ChatComponentTranslation("sodium.options.clouds_quality.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.quality.enableClouds = value,
								(opts) -> opts.quality.enableClouds)
						.setImpact(OptionImpact.LOW).build())
				.add(OptionImpl.createBuilder(int.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.cloud_height.name"))
						.setTooltip(new ChatComponentTranslation("sodium.options.cloud_height.tooltip"))
						.setControl(option -> new SliderControl(option, 128, 230, 1, ControlValueFormatter.number()))
						.setBinding((opts, value) -> opts.quality.cloudHeight = value, opts -> opts.quality.cloudHeight)
						.setImpact(OptionImpact.LOW).build())
				.build());

		groups.add(OptionGroup.createBuilder()
				.add(OptionImpl.createBuilder(SodiumGameOptions.GraphicsQuality.class, sodiumOpts)
						.setName(new ChatComponentTranslation("soundCategory.weather"))
						.setTooltip(new ChatComponentTranslation("sodium.options.weather_quality.tooltip"))
						.setControl(option -> new CyclingControl<>(option, SodiumGameOptions.GraphicsQuality.class))
						.setBinding((opts, value) -> opts.quality.weatherQuality = value,
								opts -> opts.quality.weatherQuality)
						.setImpact(OptionImpact.MEDIUM).build())
				.add(OptionImpl.createBuilder(SodiumGameOptions.GraphicsQuality.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.leaves_quality.name"))
						.setTooltip(new ChatComponentTranslation("sodium.options.leaves_quality.tooltip"))
						.setControl(option -> new CyclingControl<>(option, SodiumGameOptions.GraphicsQuality.class))
						.setBinding((opts, value) -> opts.quality.leavesQuality = value,
								opts -> opts.quality.leavesQuality)
						.setImpact(OptionImpact.MEDIUM).setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD).build())
				.add(OptionImpl.createBuilder(ParticleMode.class, vanillaOpts)
						.setName(new ChatComponentTranslation("options.particles"))
						.setTooltip(new ChatComponentTranslation("sodium.options.particle_quality.tooltip"))
						.setControl(opt -> new CyclingControl<>(opt, ParticleMode.class))
						.setBinding((opts, value) -> opts.particleSetting = value.ordinal(),
								(opts) -> ParticleMode.fromOrdinal(opts.particleSetting))
						.setImpact(OptionImpact.MEDIUM).build())
				.add(OptionImpl.createBuilder(SodiumGameOptions.LightingQuality.class, sodiumOpts)
						.setName(new ChatComponentTranslation("options.ao"))
						.setTooltip(new ChatComponentTranslation("sodium.options.smooth_lighting.tooltip"))
						.setControl(option -> new CyclingControl<>(option, SodiumGameOptions.LightingQuality.class))
						.setBinding((opts, value) -> opts.quality.smoothLighting = value,
								opts -> opts.quality.smoothLighting)
						.setImpact(OptionImpact.MEDIUM).setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD).build())
				.add(OptionImpl.createBuilder(int.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.biome_blend.name"))
						.setTooltip(new ChatComponentTranslation("sodium.options.biome_blend.tooltip"))
						.setControl(option -> new SliderControl(option, 1, 7, 1, ControlValueFormatter.biomeBlend()))
						.setBinding((opts, value) -> opts.quality.biomeBlendRadius = (value),
								opts -> opts.quality.biomeBlendRadius)
						.setImpact(OptionImpact.LOW).setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD).build())
				.add(OptionImpl.createBuilder(boolean.class, vanillaOpts)
						.setName(new ChatComponentTranslation("options.entityShadows"))
						.setTooltip(new ChatComponentTranslation("sodium.options.entity_shadows.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.entityShadows = (value), opts -> opts.entityShadows)
						.setImpact(OptionImpact.MEDIUM).build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.vignette.name"))
						.setTooltip(new ChatComponentTranslation("sodium.options.vignette.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.quality.enableVignette = value,
								opts -> opts.quality.enableVignette)
						.build())
				.build());

		groups.add(OptionGroup.createBuilder()
				.add(OptionImpl.createBuilder(int.class, vanillaOpts)
						.setName(new ChatComponentTranslation("options.mipmapLevels"))
						.setTooltip(new ChatComponentTranslation("sodium.options.mipmap_levels.tooltip"))
						.setControl(option -> new SliderControl(option, 0, 4, 1, ControlValueFormatter.multiplier()))
						.setBinding((opts, value) -> opts.mipmapLevels = (value), opts -> opts.mipmapLevels)
						.setImpact(OptionImpact.MEDIUM).setFlags(OptionFlag.REQUIRES_ASSET_RELOAD).build())
				.build());

		return new OptionPage(new ChatComponentTranslation("sodium.options.pages.quality"),
				ImmutableList.copyOf(groups));
	}

	public static OptionPage performance() {
		List<OptionGroup> groups = new ArrayList<>();

		groups.add(OptionGroup.createBuilder()
				.add(OptionImpl.createBuilder(int.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.chunk_update_threads.name"))
						.setTooltip(new ChatComponentTranslation("sodium.options.chunk_update_threads.tooltip"))
						.setControl(o -> new SliderControl(o, 0, Runtime.getRuntime().availableProcessors(), 1,
								ControlValueFormatter.quantityOrDisabled("threads", "Default")))
						.setImpact(OptionImpact.HIGH)
						.setBinding((opts, value) -> opts.performance.chunkBuilderThreads = value,
								opts -> opts.performance.chunkBuilderThreads)
						.setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD).build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.always_defer_chunk_updates.name"))
						.setTooltip(new ChatComponentTranslation("sodium.options.always_defer_chunk_updates.tooltip"))
						.setControl(TickBoxControl::new).setImpact(OptionImpact.HIGH)
						.setBinding((opts, value) -> opts.performance.alwaysDeferChunkUpdates = value,
								opts -> opts.performance.alwaysDeferChunkUpdates)
						.setFlags(OptionFlag.REQUIRES_RENDERER_UPDATE).build())
				.build());

		groups.add(OptionGroup.createBuilder()
				.add(OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.use_block_face_culling.name"))
						.setTooltip(new ChatComponentTranslation("sodium.options.use_block_face_culling.tooltip"))
						.setControl(TickBoxControl::new).setImpact(OptionImpact.MEDIUM)
						.setBinding((opts, value) -> opts.performance.useBlockFaceCulling = value,
								opts -> opts.performance.useBlockFaceCulling)
						.setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD).build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.use_fog_occlusion.name"))
						.setTooltip(new ChatComponentTranslation("sodium.options.use_fog_occlusion.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.performance.useFogOcclusion = value,
								opts -> opts.performance.useFogOcclusion)
						.setImpact(OptionImpact.MEDIUM).setFlags(OptionFlag.REQUIRES_RENDERER_UPDATE).build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.use_entity_culling.name"))
						.setTooltip(new ChatComponentTranslation("sodium.options.use_entity_culling.tooltip"))
						.setControl(TickBoxControl::new).setImpact(OptionImpact.MEDIUM)
						.setBinding((opts, value) -> opts.performance.useEntityCulling = value,
								opts -> opts.performance.useEntityCulling)
						.build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.smart_cull.name"))
						.setTooltip(new ChatComponentTranslation("sodium.options.smart_cull.tooltip"))
						.setControl(TickBoxControl::new).setImpact(OptionImpact.MEDIUM)
						.setBinding((opts, value) -> opts.performance.smartCull = value,
								opts -> opts.performance.smartCull)
						.build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.animate_only_visible_textures.name"))
						.setTooltip(
								new ChatComponentTranslation("sodium.options.animate_only_visible_textures.tooltip"))
						.setControl(TickBoxControl::new).setImpact(OptionImpact.HIGH)
						.setBinding((opts, value) -> opts.performance.animateOnlyVisibleTextures = value,
								opts -> opts.performance.animateOnlyVisibleTextures)
						.setFlags(OptionFlag.REQUIRES_RENDERER_UPDATE).build())
				.build());

		return new OptionPage(new ChatComponentTranslation("sodium.options.pages.performance"),
				ImmutableList.copyOf(groups));
	}

	public static OptionPage advanced() {
		List<OptionGroup> groups = new ArrayList<>();

		boolean isPersistentMappingSupported = MappedStagingBuffer.isSupported(RenderDevice.INSTANCE);

		groups.add(OptionGroup.createBuilder()
				.add(OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.use_persistent_mapping.name"))
						.setTooltip(new ChatComponentTranslation("sodium.options.use_persistent_mapping.tooltip"))
						.setControl(TickBoxControl::new).setImpact(OptionImpact.MEDIUM)
						.setEnabled(() -> isPersistentMappingSupported)
						.setBinding((opts, value) -> opts.advanced.useAdvancedStagingBuffers = value,
								opts -> opts.advanced.useAdvancedStagingBuffers)
						.setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD).build())
				.build());

		groups.add(OptionGroup.createBuilder()
				.add(OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.cpu_render_ahead.name"))
						.setTooltip(new ChatComponentTranslation("sodium.options.cpu_render_ahead.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.advanced.cpuRenderAhead = value,
								opts -> opts.advanced.cpuRenderAhead)
						.build())
				.add(OptionImpl.createBuilder(int.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.cpu_render_ahead_limit.name"))
						.setTooltip(new ChatComponentTranslation("sodium.options.cpu_render_ahead_limit.tooltip"))
						.setControl(opt -> new SliderControl(opt, 0, 9, 1,
								ControlValueFormatter.translateVariable("sodium.options.cpu_render_ahead_limit.value")))
						.setBinding((opts, value) -> opts.advanced.cpuRenderAheadLimit = value,
								opts -> opts.advanced.cpuRenderAheadLimit)
						.build())
				.build());

		groups.add(OptionGroup.createBuilder()
				.add(OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.fps_overlay.name"))
						.setTooltip(new ChatComponentTranslation("sodium.options.fps_overlay.tooltip"))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.advanced.fpsOverlay = value, opts -> opts.advanced.fpsOverlay)
						.build())
				.build());

		return new OptionPage(new ChatComponentTranslation("sodium.options.pages.advanced"),
				ImmutableList.copyOf(groups));
	}

	public static OptionPage culling() {
		List<OptionGroup> groups = new ArrayList<>();

		groups.add(OptionGroup.createBuilder()
				.add(OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentText("Skip Armor Stands with Nametag"))
						.setTooltip(new ChatComponentText("Skips culling of armor stands with nametags"))
						.setControl(TickBoxControl::new).setImpact(OptionImpact.MEDIUM)
						.setBinding((opts, value) -> opts.culling.skipMarkerArmorStands = value,
								opts -> opts.culling.skipMarkerArmorStands)
						.build())
				.add(OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentText("Render nametags through walls"))
						.setTooltip(new ChatComponentText("Self-explanatory")).setControl(TickBoxControl::new)
						.setImpact(OptionImpact.MEDIUM)
						.setBinding((opts, value) -> opts.culling.renderNametagsThroughWalls = value,
								opts -> opts.culling.renderNametagsThroughWalls)
						.build())
				.build());

		groups.add(OptionGroup.createBuilder().add(OptionImpl.createBuilder(int.class, sodiumOpts)
				.setName(new ChatComponentText("Tracing Distance"))
				.setTooltip(new ChatComponentText("The maximum range of the occluder"))
				.setControl(option -> new SliderControl(option, 32, 2048, 1, ControlValueFormatter.number()))
				.setBinding((opts, value) -> opts.culling.tracingDistance = value, opts -> opts.quality.cloudHeight)
				.setImpact(OptionImpact.MEDIUM).build()).build());

		return new OptionPage(new ChatComponentText("Culling"), ImmutableList.copyOf(groups));
	}
}
