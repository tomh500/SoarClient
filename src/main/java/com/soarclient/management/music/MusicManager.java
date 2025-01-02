package com.soarclient.management.music;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.imageio.ImageIO;

import com.soarclient.utils.RandomUtils;
import com.soarclient.utils.file.FileLocation;
import com.soarclient.utils.file.FileUtils;

import me.eldodebug.jsmp.Jsmp;
import me.eldodebug.jsmp.parser.JsmpResult;
import net.minecraft.client.Minecraft;

public class MusicManager {

	private final List<Music> musics = Collections.synchronizedList(new ArrayList<>());
	private CompletableFuture<Void> loadFuture;

	private Music currentMusic;
	private MusicPlayer musicPlayer;
	private boolean shuffle;
	private boolean repeat;

	public MusicManager() {

		load();

		this.musicPlayer = new MusicPlayer(() -> {

			Music nextMusic;

			if (repeat) {
				nextMusic = currentMusic;
			} else if (shuffle) {
				nextMusic = musics.get(RandomUtils.getRandomInt(0, musics.size() - 1));
			} else {
				nextMusic = null;
			}

			setCurrentMusic(nextMusic);

			if (currentMusic != null) {
				play();
			} else {
				musicPlayer.setPlaying(false);
			}
		});
		this.shuffle = false;
		this.repeat = false;
		new Thread("Music Thread") {
			@Override
			public void run() {
				while (Minecraft.getMinecraft().isRunning()) {
					musicPlayer.run();
				}
			}
		}.start();
	}

	public CompletableFuture<Void> load() {
		
		if (loadFuture != null && !loadFuture.isDone()) {
			return loadFuture;
		}

		loadFuture = CompletableFuture.runAsync(() -> {
			File musicDir = FileLocation.MUSIC_DIR;

			if (musicDir.listFiles() == null) {
				return;
			}

			List<CompletableFuture<Music>> futures = new ArrayList<>();

			for (File f : musicDir.listFiles()) {
				if (!isSupportAudio(f)) {
					continue;
				}

				CompletableFuture<Music> musicFuture = CompletableFuture.supplyAsync(() -> {
					try {
						JsmpResult result = Jsmp.parse(f);
						String title = null;
						String artist = null;
						BufferedImage image = null;
						File albumFile = null;

						if (result != null) {
							title = result.getTitle();
							artist = result.getArtist();
							image = result.getAlbumImage();

							if (image != null) {
								albumFile = new File(FileLocation.CACHE_DIR, FileUtils.getMd5Checksum(f));
								if (!albumFile.exists()) {
									ImageIO.write(image, "png", albumFile);
								}
							}
						}

						return new Music(f, title == null ? f.getName().replace(FileUtils.getExtension(f), "") : title,
								artist == null ? "Unknown" : artist, albumFile);
					} catch (Exception e) {
						e.printStackTrace();
						return new Music(f, f.getName().replace(FileUtils.getExtension(f), ""), "Unknown", null);
					}
				});

				futures.add(musicFuture);
			}

			CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenAccept(v -> {
				futures.forEach(future -> {
					try {
						Music music = future.get();
						musics.add(music);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			});
		});

		return loadFuture;
	}

	public boolean isLoading() {
		return loadFuture != null && !loadFuture.isDone();
	}

	public void play() {

		if (currentMusic == null) {
			return;
		}

		setVolume(getVolume());
		musicPlayer.setCurrentMusic(currentMusic);
	}

	public float getVolume() {
		return musicPlayer.getVolume();
	}

	public void setVolume(float volume) {
		musicPlayer.setVolume(volume);
	}

	public void next() {

		if (currentMusic == null) {
			return;
		}

		int max = musics.size();
		int index = musics.indexOf(currentMusic);

		if (index < max - 1) {
			index++;
		} else {
			index = 0;
		}

		currentMusic = musics.get(index);
		play();
	}

	public void back() {

		if (currentMusic == null) {
			return;
		}

		int max = musics.size();
		int index = musics.indexOf(currentMusic);

		if (index > 0) {
			index--;
		} else {
			index = max - 1;
		}

		currentMusic = musics.get(index);
		play();
	}

	public void switchPlayBack() {
		musicPlayer.setPlaying(!musicPlayer.isPlaying());
	}

	public void stop() {
		musicPlayer.setPlaying(false);
	}

	public boolean isPlaying() {
		return musicPlayer.isPlaying();
	}

	public float getCurrentTime() {
		return musicPlayer.getCurrentTime();
	}

	public float getEndTime() {
		return musicPlayer.getEndTime();
	}
	
	public List<Music> getMusics() {
	    return Collections.unmodifiableList(musics);
	}

	public Music getCurrentMusic() {
		return currentMusic;
	}

	public void setCurrentMusic(Music currentMusic) {
		this.currentMusic = currentMusic;
	}

	public boolean isShuffle() {
		return shuffle;
	}

	public void setShuffle(boolean shuffle) {
		this.shuffle = shuffle;
	}

	public boolean isRepeat() {
		return repeat;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	private boolean isSupportAudio(File f) {

		String ext = FileUtils.getExtension(f);

		return ext.equals("mp3") || ext.equals("flac");
	}
}
