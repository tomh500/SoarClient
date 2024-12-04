package com.soarclient.viasoar;

import com.soarclient.viasoar.common.ViaSoarCommon;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

public class ViaHelper {

	private static Minecraft mc = Minecraft.getMinecraft();
	
    public static void sendConditionalSwing(final MovingObjectPosition mop) {
        if (mop != null && mop.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) {
            mc.thePlayer.swingItem();
        }
    }

    public static void sendFixedAttack(final EntityPlayer entityIn, final Entity target) {
        if (newerThan(ProtocolVersion.v1_8)) {
            mc.playerController.attackEntity(entityIn, target);
            mc.thePlayer.swingItem();
        } else {
            mc.thePlayer.swingItem();
            mc.playerController.attackEntity(entityIn, target);
        }
    }
    
	public static boolean newerThanOrEqualsTo(ProtocolVersion version) {
		return getVersion().newerThanOrEqualTo(version) && !Minecraft.getMinecraft().isIntegratedServerRunning();
	}
	
	public static boolean olderThanOrEqualsTo(ProtocolVersion version) {
		return getVersion().olderThanOrEqualTo(version) && !Minecraft.getMinecraft().isIntegratedServerRunning();
	}
	
	public static boolean newerThan(ProtocolVersion version) {
		return getVersion().newerThan(version) && !Minecraft.getMinecraft().isIntegratedServerRunning();
	}
	
	public static boolean olderThan(ProtocolVersion version) {
		return getVersion().olderThan(version) && !Minecraft.getMinecraft().isIntegratedServerRunning();
	}
	
	private static ProtocolVersion getVersion() {
		return ViaSoarCommon.getManager().getTargetVersion();
	}
}
