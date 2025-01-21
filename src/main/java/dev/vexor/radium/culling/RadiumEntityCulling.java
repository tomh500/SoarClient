package dev.vexor.radium.culling;

import com.logisticscraft.occlusionculling.OcclusionCullingInstance;

import net.caffeinemc.mods.sodium.client.SodiumClientMod;

public class RadiumEntityCulling {

    public static RadiumEntityCulling INSTANCE = new RadiumEntityCulling();
    public OcclusionCullingInstance culling;
    public static boolean enabled = true;
    public CullTask cullTask;
    private Thread cullThread;

	public int renderedBlockEntities = 0;
	public int skippedBlockEntities = 0;
	public int renderedEntities = 0;
	public int skippedEntities = 0;

	public void onInitialize() {
        culling = new OcclusionCullingInstance(SodiumClientMod.options().culling.tracingDistance, new RadiumCullingDataProvider());
        cullTask = new CullTask(culling, SodiumClientMod.options().culling.blockEntityWhitelist);

		cullThread = new Thread(cullTask, "CullThread");

		cullThread.setUncaughtExceptionHandler((thread, ex) -> {
			ex.printStackTrace();
		});

		cullThread.start();
	}
    
    public void worldTick() {
        cullTask.requestCull = true;
    }
    
    public void clientTick() {
        cullTask.requestCull = true;
    }
}
