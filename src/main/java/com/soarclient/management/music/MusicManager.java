package com.soarclient.management.music;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.soarclient.libraries.flac.FLACDecoder;
import com.soarclient.libraries.flac.metadata.Metadata;
import com.soarclient.libraries.flac.metadata.Picture;
import com.soarclient.libraries.flac.metadata.VorbisComment;
import com.soarclient.utils.file.FileLocation;
import com.soarclient.utils.file.FileUtils;

public class MusicManager {

	private List<Music> musics = new ArrayList<>();

	public MusicManager() {
		try {
			load();
		} catch (Exception e) {}
	}

	public void load() throws Exception {

		File musicDir = FileLocation.MUSIC_DIR;

		if (musicDir.listFiles() == null) {
			return;
		}

		for (File f : musicDir.listFiles()) {

			String name = f.getName();

			if (name.endsWith(".flac")) {

				FLACDecoder decoder = new FLACDecoder(new FileInputStream(f));
				Metadata[] metadata = decoder.readMetadata();
				String title = null;
				String artist = null;
				byte[] imageData = null;

				for (Metadata meta : metadata) {
					if (meta instanceof VorbisComment) {
						VorbisComment comment = (VorbisComment) meta;
						title = comment.getCommentByName("TITLE")[0];
						artist = comment.getCommentByName("ARTIST")[0];
					} else if (meta instanceof Picture) {
						Picture picture = (Picture) meta;
						imageData = picture.getImage();
					}
				}

				String fileHash = FileUtils.getMd5Checksum(f);
				File album = new File(FileLocation.CACHE_DIR, fileHash);

				if (imageData != null && !album.exists()) {

					FileOutputStream fos = new FileOutputStream(album);

					fos.write(imageData);
					fos.close();
				}

				try {
					musics.add(new Music(f, title == null ? f.getName().replace(".flac", "") : title,
							artist == null ? "" : artist, album));
				} catch (Exception e) {
				}
			}
		}
	}

	public List<Music> getMusics() {
		return musics;
	}
}
