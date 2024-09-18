package com.soarclient.libs.sodium.client.gui;

import com.google.common.collect.ImmutableList;
import com.soarclient.libs.sodium.client.gui.SodiumGameOptions.GraphicsQuality;
import com.soarclient.libs.sodium.client.gui.SodiumGameOptions.LightingQuality;
import com.soarclient.libs.sodium.client.gui.options.OptionFlag;
import com.soarclient.libs.sodium.client.gui.options.OptionGroup;
import com.soarclient.libs.sodium.client.gui.options.OptionImpact;
import com.soarclient.libs.sodium.client.gui.options.OptionImpl;
import com.soarclient.libs.sodium.client.gui.options.OptionPage;
import com.soarclient.libs.sodium.client.gui.options.control.ControlValueFormatter;
import com.soarclient.libs.sodium.client.gui.options.control.CyclingControl;
import com.soarclient.libs.sodium.client.gui.options.control.SliderControl;
import com.soarclient.libs.sodium.client.gui.options.control.TickBoxControl;
import com.soarclient.libs.sodium.client.gui.options.named.GraphicsMode;
import com.soarclient.libs.sodium.client.gui.options.named.ParticleMode;
import com.soarclient.libs.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import com.soarclient.libs.sodium.client.gui.options.storage.SodiumOptionsStorage;
import com.soarclient.libs.sodium.client.render.chunk.backends.multidraw.MultidrawChunkRenderBackend;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentTranslation;
import org.lwjgl.opengl.Display;

public class SodiumGameOptionPages {
	private static final SodiumOptionsStorage sodiumOpts = new SodiumOptionsStorage();
	private static final MinecraftOptionsStorage vanillaOpts = new MinecraftOptionsStorage();

	public static OptionPage general() {
		List<OptionGroup> groups = new ArrayList();
		groups.add(
			OptionGroup.createBuilder()
				.add(
					OptionImpl.createBuilder(int.class, vanillaOpts)
						.setName(new ChatComponentTranslation("options.renderDistance", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.view_distance.tooltip", new Object[0]))
						.setControl(option -> new SliderControl(option, 2, 32, 1, ControlValueFormatter.chunks()))
						.setBinding((options, value) -> options.renderDistanceChunks = value, options -> options.renderDistanceChunks)
						.setImpact(OptionImpact.HIGH)
						.setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
						.build()
				)
				.add(
					OptionImpl.createBuilder(int.class, vanillaOpts)
						.setName(new ChatComponentTranslation("options.gamma", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.brightness.tooltip", new Object[0]))
						.setControl(opt -> new SliderControl(opt, 0, 100, 1, ControlValueFormatter.brightness()))
						.setBinding((opts, value) -> opts.gammaSetting = (float)value.intValue() * 0.01F, opts -> (int)(opts.gammaSetting / 0.01F))
						.build()
				)
				.add(
					OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.clouds.name", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.clouds.tooltip", new Object[0]))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.quality.enableClouds = value, opts -> opts.quality.enableClouds)
						.setImpact(OptionImpact.LOW)
						.build()
				)
				.add(
					OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.fog.name", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.fog.tooltip", new Object[0]))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.quality.enableFog = value, opts -> opts.quality.enableFog)
						.setImpact(OptionImpact.LOW)
						.setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
						.build()
				)
				.build()
		);
		groups.add(
			OptionGroup.createBuilder()
				.add(
					OptionImpl.createBuilder(int.class, vanillaOpts)
						.setName(new ChatComponentTranslation("options.guiScale", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.gui_scale.tooltip", new Object[0]))
						.setControl(option -> new SliderControl(option, 0, 3, 1, ControlValueFormatter.guiScale()))
						.setBinding((opts, value) -> {
							opts.guiScale = value;
							if (Minecraft.getMinecraft().currentScreen instanceof SodiumOptionsGUI) {
								Minecraft.getMinecraft().displayGuiScreen(new SodiumOptionsGUI(((SodiumOptionsGUI)Minecraft.getMinecraft().currentScreen).prevScreen));
							}
						}, opts -> opts.guiScale)
						.build()
				)
				.add(
					OptionImpl.createBuilder(boolean.class, vanillaOpts)
						.setName(new ChatComponentTranslation("options.fullscreen", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.fullscreen.tooltip", new Object[0]))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> {
							opts.fullScreen = value;
							Minecraft client = Minecraft.getMinecraft();
							if (client.isFullScreen() != opts.fullScreen) {
								client.toggleFullscreen();
								opts.fullScreen = client.isFullScreen();
							}
						}, opts -> opts.fullScreen)
						.build()
				)
				.add(
					OptionImpl.createBuilder(boolean.class, vanillaOpts)
						.setName(new ChatComponentTranslation("options.vsync", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.v_sync.tooltip", new Object[0]))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> {
							opts.enableVsync = value;
							Display.setVSyncEnabled(opts.enableVsync);
						}, opts -> opts.enableVsync)
						.setImpact(OptionImpact.VARIES)
						.build()
				)
				.add(
					OptionImpl.createBuilder(int.class, vanillaOpts)
						.setName(new ChatComponentTranslation("options.framerateLimit", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.fps_limit.tooltip", new Object[0]))
						.setControl(option -> new SliderControl(option, 5, 260, 5, ControlValueFormatter.fpsLimit()))
						.setBinding((opts, value) -> opts.limitFramerate = value, opts -> opts.limitFramerate)
						.build()
				)
				.build()
		);
		groups.add(
			OptionGroup.createBuilder()
				.add(
					OptionImpl.createBuilder(boolean.class, vanillaOpts)
						.setName(new ChatComponentTranslation("options.viewBobbing", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.view_bobbing.tooltip", new Object[0]))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.viewBobbing = value, opts -> opts.viewBobbing)
						.build()
				)
				.build()
		);
		return new OptionPage(new ChatComponentTranslation("stat.generalButton", new Object[0]), ImmutableList.copyOf(groups));
	}

	public static OptionPage quality() {
		List<OptionGroup> groups = new ArrayList();
		groups.add(
			OptionGroup.createBuilder()
				.add(
					OptionImpl.createBuilder(GraphicsMode.class, vanillaOpts)
						.setName(new ChatComponentTranslation("options.graphics", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.graphics_quality.tooltip", new Object[0]))
						.setControl(option -> new CyclingControl(option, GraphicsMode.class))
						.setBinding((opts, value) -> opts.fancyGraphics = value.isFancy(), opts -> GraphicsMode.fromBoolean(opts.fancyGraphics))
						.setImpact(OptionImpact.HIGH)
						.setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
						.build()
				)
				.build()
		);
		groups.add(
			OptionGroup.createBuilder()
				.add(
					OptionImpl.createBuilder(GraphicsQuality.class, sodiumOpts)
						.setName(new ChatComponentTranslation("options.renderClouds", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.clouds_quality.tooltip", new Object[0]))
						.setControl(option -> new CyclingControl(option, GraphicsQuality.class))
						.setBinding((opts, value) -> opts.quality.cloudQuality = value, opts -> opts.quality.cloudQuality)
						.setImpact(OptionImpact.LOW)
						.build()
				)
				.add(
					OptionImpl.createBuilder(int.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.cloud_height.name", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.cloud_height.tooltip", new Object[0]))
						.setControl(option -> new SliderControl(option, 128, 230, 1, ControlValueFormatter.blocks()))
						.setBinding((opts, value) -> opts.quality.cloudHeight = value, opts -> opts.quality.cloudHeight)
						.setImpact(OptionImpact.LOW)
						.build()
				)
				.add(
					OptionImpl.createBuilder(GraphicsQuality.class, sodiumOpts)
						.setName(new ChatComponentTranslation("soundCategory.weather", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.weather_quality.tooltip", new Object[0]))
						.setControl(option -> new CyclingControl(option, GraphicsQuality.class))
						.setBinding((opts, value) -> opts.quality.weatherQuality = value, opts -> opts.quality.weatherQuality)
						.setImpact(OptionImpact.MEDIUM)
						.build()
				)
				.add(
					OptionImpl.createBuilder(GraphicsQuality.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.leaves_quality.name", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.leaves_quality.tooltip", new Object[0]))
						.setControl(option -> new CyclingControl(option, GraphicsQuality.class))
						.setBinding((opts, value) -> opts.quality.leavesQuality = value, opts -> opts.quality.leavesQuality)
						.setImpact(OptionImpact.MEDIUM)
						.setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
						.build()
				)
				.add(
					OptionImpl.createBuilder(ParticleMode.class, vanillaOpts)
						.setName(new ChatComponentTranslation("options.particles", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.particle_quality.tooltip", new Object[0]))
						.setControl(opt -> new CyclingControl(opt, ParticleMode.class))
						.setBinding((opts, value) -> opts.particleSetting = value.ordinal(), opts -> ParticleMode.fromOrdinal(opts.particleSetting))
						.setImpact(OptionImpact.MEDIUM)
						.build()
				)
				.add(
					OptionImpl.createBuilder(LightingQuality.class, sodiumOpts)
						.setName(new ChatComponentTranslation("options.ao", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.smooth_lighting.tooltip", new Object[0]))
						.setControl(option -> new CyclingControl(option, LightingQuality.class))
						.setBinding((opts, value) -> opts.quality.smoothLighting = value, opts -> opts.quality.smoothLighting)
						.setImpact(OptionImpact.MEDIUM)
						.setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
						.build()
				)
				.add(
					OptionImpl.createBuilder(int.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.biome_blend.name", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.biome_blend.tooltip", new Object[0]))
						.setControl(option -> new SliderControl(option, 0, 7, 1, ControlValueFormatter.quantityOrDisabled("sodium.options.biome_blend.value", "gui.none")))
						.setBinding((opts, value) -> opts.quality.biomeBlendRadius = value, opts -> opts.quality.biomeBlendRadius)
						.setImpact(OptionImpact.LOW)
						.setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
						.build()
				)
				.add(
					OptionImpl.createBuilder(int.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.entity_distance.name", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.entity_distance.tooltip", new Object[0]))
						.setControl(option -> new SliderControl(option, 50, 500, 25, ControlValueFormatter.percentage()))
						.setBinding(
							(opts, value) -> opts.quality.entityDistanceScaling = (float)value.intValue() / 100.0F, opts -> Math.round(opts.quality.entityDistanceScaling * 100.0F)
						)
						.setImpact(OptionImpact.MEDIUM)
						.build()
				)
				.add(
					OptionImpl.createBuilder(boolean.class, vanillaOpts)
						.setName(new ChatComponentTranslation("options.entityShadows", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.entity_shadows.tooltip", new Object[0]))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.entityShadows = value, opts -> opts.entityShadows)
						.setImpact(OptionImpact.LOW)
						.build()
				)
				.add(
					OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.vignette.name", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.vignette.tooltip", new Object[0]))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.quality.enableVignette = value, opts -> opts.quality.enableVignette)
						.setImpact(OptionImpact.LOW)
						.build()
				)
				.build()
		);
		groups.add(
			OptionGroup.createBuilder()
				.add(
					OptionImpl.createBuilder(int.class, vanillaOpts)
						.setName(new ChatComponentTranslation("options.mipmapLevels", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.mipmap_levels.tooltip", new Object[0]))
						.setControl(option -> new SliderControl(option, 0, 4, 1, ControlValueFormatter.multiplier()))
						.setBinding((opts, value) -> opts.mipmapLevels = value, opts -> opts.mipmapLevels)
						.setImpact(OptionImpact.MEDIUM)
						.setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
						.build()
				)
				.build()
		);
		return new OptionPage(new ChatComponentTranslation("sodium.options.pages.quality", new Object[0]), ImmutableList.copyOf(groups));
	}

	public static OptionPage advanced() {
		List<OptionGroup> groups = new ArrayList();
		groups.add(
			OptionGroup.createBuilder()
				.add(
					OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.use_chunk_multidraw.name", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.use_chunk_multidraw.tooltip", new Object[0]))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.advanced.useChunkMultidraw = value, opts -> opts.advanced.useChunkMultidraw)
						.setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
						.setImpact(OptionImpact.EXTREME)
						.setEnabled(MultidrawChunkRenderBackend.isSupported(sodiumOpts.getData().advanced.ignoreDriverBlacklist))
						.build()
				)
				.add(
					OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.use_vertex_objects.name", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.use_vertex_objects.tooltip", new Object[0]))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.advanced.useVertexArrayObjects = value, opts -> opts.advanced.useVertexArrayObjects)
						.setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
						.setImpact(OptionImpact.LOW)
						.build()
				)
				.build()
		);
		groups.add(
			OptionGroup.createBuilder()
				.add(
					OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.use_block_face_culling.name", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.use_block_face_culling.tooltip", new Object[0]))
						.setControl(TickBoxControl::new)
						.setImpact(OptionImpact.MEDIUM)
						.setBinding((opts, value) -> opts.advanced.useBlockFaceCulling = value, opts -> opts.advanced.useBlockFaceCulling)
						.setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
						.build()
				)
				.add(
					OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.use_compact_vertex_format.name", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.use_compact_vertex_format.tooltip", new Object[0]))
						.setControl(TickBoxControl::new)
						.setImpact(OptionImpact.MEDIUM)
						.setBinding((opts, value) -> opts.advanced.useCompactVertexFormat = value, opts -> opts.advanced.useCompactVertexFormat)
						.setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
						.build()
				)
				.add(
					OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.use_fog_occlusion.name", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.use_fog_occlusion.tooltip", new Object[0]))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.advanced.useFogOcclusion = value, opts -> opts.advanced.useFogOcclusion)
						.setImpact(OptionImpact.MEDIUM)
						.setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
						.build()
				)
				.add(
					OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.translucency_sorting.name", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.translucency_sorting.tooltip", new Object[0]))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.advanced.translucencySorting = value, opts -> opts.advanced.translucencySorting)
						.setImpact(OptionImpact.MEDIUM)
						.setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
						.build()
				)
				.add(
					OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.use_entity_culling.name", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.use_entity_culling.tooltip", new Object[0]))
						.setControl(TickBoxControl::new)
						.setImpact(OptionImpact.MEDIUM)
						.setBinding((opts, value) -> opts.advanced.useEntityCulling = value, opts -> opts.advanced.useEntityCulling)
						.build()
				)
				.add(
					OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.use_particle_culling.name", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.use_particle_culling.tooltip", new Object[0]))
						.setControl(TickBoxControl::new)
						.setImpact(OptionImpact.LOW)
						.setBinding((opts, value) -> opts.advanced.useParticleCulling = value, opts -> opts.advanced.useParticleCulling)
						.build()
				)
				.add(
					OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.animate_only_visible_textures.name", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.animate_only_visible_textures.tooltip", new Object[0]))
						.setControl(TickBoxControl::new)
						.setImpact(OptionImpact.HIGH)
						.setBinding((opts, value) -> opts.advanced.animateOnlyVisibleTextures = value, opts -> opts.advanced.animateOnlyVisibleTextures)
						.build()
				)
				.build()
		);
		groups.add(
			OptionGroup.createBuilder()
				.add(
					OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.allow_direct_memory_access.name", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.allow_direct_memory_access.tooltip", new Object[0]))
						.setControl(TickBoxControl::new)
						.setImpact(OptionImpact.HIGH)
						.setBinding((opts, value) -> opts.advanced.allowDirectMemoryAccess = value, opts -> opts.advanced.allowDirectMemoryAccess)
						.build()
				)
				.build()
		);
		groups.add(
			OptionGroup.createBuilder()
				.add(
					OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.ignore_driver_blacklist.name", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.ignore_driver_blacklist.tooltip", new Object[0]))
						.setControl(TickBoxControl::new)
						.setBinding((opts, value) -> opts.advanced.ignoreDriverBlacklist = value, opts -> opts.advanced.ignoreDriverBlacklist)
						.build()
				)
				.build()
		);
		return new OptionPage(new ChatComponentTranslation("sodium.options.pages.advanced", new Object[0]), ImmutableList.copyOf(groups));
	}

	public static OptionPage performance() {
		List<OptionGroup> groups = new ArrayList();
		groups.add(
			OptionGroup.createBuilder()
				.add(
					OptionImpl.createBuilder(int.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.chunk_update_threads.name", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.chunk_update_threads.tooltip", new Object[0]))
						.setControl(
							o -> new SliderControl(
									o,
									0,
									Runtime.getRuntime().availableProcessors(),
									1,
									ControlValueFormatter.quantityOrDisabled("sodium.options.threads.value", "sodium.options.default")
								)
						)
						.setImpact(OptionImpact.HIGH)
						.setBinding((opts, value) -> opts.performance.chunkBuilderThreads = value, opts -> opts.performance.chunkBuilderThreads)
						.setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
						.build()
				)
				.add(
					OptionImpl.createBuilder(boolean.class, sodiumOpts)
						.setName(new ChatComponentTranslation("sodium.options.always_defer_chunk_updates.name", new Object[0]))
						.setTooltip(new ChatComponentTranslation("sodium.options.always_defer_chunk_updates.tooltip", new Object[0]))
						.setControl(TickBoxControl::new)
						.setImpact(OptionImpact.HIGH)
						.setBinding((opts, value) -> opts.performance.alwaysDeferChunkUpdates = value, opts -> opts.performance.alwaysDeferChunkUpdates)
						.setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
						.build()
				)
				.build()
		);
		return new OptionPage(new ChatComponentTranslation("sodium.options.pages.performance", new Object[0]), ImmutableList.copyOf(groups));
	}
}
