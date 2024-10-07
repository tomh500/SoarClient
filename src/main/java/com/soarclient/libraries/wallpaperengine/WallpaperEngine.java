package com.soarclient.libraries.wallpaperengine;

import java.io.File;
import java.nio.ByteBuffer;

import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.soarclient.utils.TimerUtils;

import net.minecraft.client.renderer.GlStateManager;

public class WallpaperEngine {

	private TimerUtils timer = new TimerUtils();

	private int framerate;
	private int grabbedFrames;
	private int lastFrameTexID;

	private FFmpegFrameGrabber grabber;
	private Frame currentFrame;

	public void setup(File videoFile, int framerate) {
		this.framerate = framerate;

		try {
			avutil.av_log_set_level(avutil.AV_LOG_ERROR);
			grabber = FFmpegFrameGrabber.createDefault(videoFile);
			grabber.start();
		} catch (FFmpegFrameGrabber.Exception e) {
		}
	}

	public int getTexture() {

		if (timer.delay(1000 / framerate, true)) {
			try {
				if (grabbedFrames == grabber.getLengthInFrames()) {
					grabber.setFrameNumber(0);
					grabbedFrames = 0;
				}

				currentFrame = grabber.grabImage();
				if (currentFrame != null) {
					GL11.glDeleteTextures(lastFrameTexID);
					int texID = GL11.glGenTextures();
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
					GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
					GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
					GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, currentFrame.imageWidth,
							currentFrame.imageHeight, 0, GL12.GL_BGR, GL11.GL_UNSIGNED_BYTE,
							(ByteBuffer) currentFrame.image[0]);
					lastFrameTexID = texID;
					grabbedFrames += 1;
				}
			} catch (FFmpegFrameGrabber.Exception e) {
			}
		}

		GlStateManager.bindTexture(lastFrameTexID);
		return lastFrameTexID;
	}
	
    public void close() {
        try {
            if (grabber != null) {
                grabber.stop();
                grabber.close();
            }
        } catch (FrameGrabber.Exception e) {}
    }
}
