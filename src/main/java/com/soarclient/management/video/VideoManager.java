package com.soarclient.management.video;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.soarclient.libraries.wallpaperengine.WallpaperEngine;
import com.soarclient.management.video.api.Video;
import com.soarclient.utils.file.FileLocation;
import com.soarclient.utils.tuples.Pair;

public class VideoManager {

	private List<Video> videos = new ArrayList<>();
	private WallpaperEngine engine = new WallpaperEngine();
	private Video video;

	public VideoManager() {
		copyVideosFromJar();
		video = videos.get(0);
		engine.setup(video.getFile(), 60);
	}

	public void copyVideosFromJar() {

		List<Pair<Integer, InputStream>> streams = new ArrayList<>();

		streams.add(Pair.of(-6563370,
				VideoManager.class.getClassLoader().getResourceAsStream("assets/minecraft/soar/videos/0.mp4")));
		streams.add(Pair.of(-10237960,
				VideoManager.class.getClassLoader().getResourceAsStream("assets/minecraft/soar/videos/1.mp4")));

		try {
			for (Pair<Integer, InputStream> p : streams) {

				File tempFile = new File(FileLocation.CACHE_DIR, String.valueOf(p.getFirst()));

				if (!tempFile.exists()) {
					try (OutputStream outputStream = new FileOutputStream(tempFile)) {
						byte[] buffer = new byte[8192];
						int bytesRead;
						while ((bytesRead = p.getSecond().read(buffer)) != -1) {
							outputStream.write(buffer, 0, bytesRead);
						}
					}
				}

				videos.add(new Video(tempFile, p.getFirst()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Video getVideo() {
		return video;
	}

	public WallpaperEngine getEngine() {
		return engine;
	}
}
