package com.soarclient.libraries.krypton.compress;

import com.velocitypowered.natives.compression.VelocityCompressor;
import com.velocitypowered.natives.util.MoreByteBufUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.minecraft.network.PacketBuffer;

public class MinecraftCompressEncoder extends MessageToByteEncoder<ByteBuf> {

	private final int threshold;
	private final VelocityCompressor compressor;

	public MinecraftCompressEncoder(int threshold, VelocityCompressor compressor) {
		this.threshold = threshold;
		this.compressor = compressor;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
		PacketBuffer wrappedBuf = new PacketBuffer(out);
		int uncompressed = msg.readableBytes();
		if (uncompressed < threshold) {
			wrappedBuf.writeVarIntToBuffer(0);
			out.writeBytes(msg);
		} else {
			wrappedBuf.writeVarIntToBuffer(uncompressed);
			ByteBuf compatibleIn = MoreByteBufUtils.ensureCompatible(ctx.alloc(), compressor, msg);
			try {
				compressor.deflate(compatibleIn, out);
			} finally {
				compatibleIn.release();
			}
		}
	}

	@Override
	protected ByteBuf allocateBuffer(ChannelHandlerContext ctx, ByteBuf msg, boolean preferDirect) throws Exception {
		int initialBufferSize = msg.readableBytes() + 1;
		return MoreByteBufUtils.preferredBuffer(ctx.alloc(), compressor, initialBufferSize);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		compressor.close();
	}

}