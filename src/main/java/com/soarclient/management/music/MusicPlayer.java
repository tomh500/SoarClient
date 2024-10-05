package com.soarclient.management.music;

import java.io.FileInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

import com.soarclient.libraries.flac.FLACDecoder;
import com.soarclient.libraries.flac.frame.Frame;
import com.soarclient.libraries.flac.metadata.StreamInfo;
import com.soarclient.libraries.flac.util.ByteData;

public class MusicPlayer implements Runnable {

	private Runnable runnable;

	private FLACDecoder decoder;
	private StreamInfo streamInfo;

	private AudioFormat audioFormat;
	private DataLine.Info info;
	private SourceDataLine sourceDataLine;

	private Music currentMusic;
	private boolean playing;
	private float volume;

	public MusicPlayer(Runnable runnable) {
		this.runnable = runnable;
		this.playing = false;
		this.volume = 0.5F;
	}

	@Override
	public void run() {

		if (currentMusic != null && playing) {

			try {
				decoder = new FLACDecoder(new FileInputStream(currentMusic.getAudio()));
				streamInfo = decoder.readStreamInfo();
				audioFormat = new AudioFormat(streamInfo.getSampleRate(), streamInfo.getBitsPerSample(),
						streamInfo.getChannels(), (streamInfo.getBitsPerSample() <= 8) ? false : true, false);
				info = new DataLine.Info(SourceDataLine.class, audioFormat);

				sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
				sourceDataLine.open(audioFormat);
				setVolume(volume);
				sourceDataLine.start();

				Frame frame;

				while ((frame = decoder.readNextFrame()) != null) {

					while (!playing) {
						Thread.sleep(10);
					}

					ByteData byteData = new ByteData(1024);
					ByteData pcm = decoder.decodeFrame(frame, byteData);

					sourceDataLine.write(pcm.getData(), 0, pcm.getLen());
				}

				sourceDataLine.drain();
				sourceDataLine.close();
				runnable.run();
			} catch (Exception e) {
			}
		}
	}

	public void setCurrentMusic(Music currentMusic) {

		playing = false;

		if (sourceDataLine != null) {
			sourceDataLine.stop();
			sourceDataLine.drain();
			sourceDataLine.close();
		}

		this.currentMusic = currentMusic;
		playing = true;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public float getCurrentTime() {

		if (sourceDataLine == null || audioFormat == null) {
			return 0;
		}

		return (float) (sourceDataLine.getMicrosecondPosition() / 1000000.0);
	}

	public float getEndTime() {

		if (streamInfo == null) {
			return 0;
		}

		long totalSamples = streamInfo.getTotalSamples();
		int sampleRate = streamInfo.getSampleRate();

		if (totalSamples > 0 && sampleRate > 0) {
			return (float) totalSamples / sampleRate;
		}

		return 0;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		if (volume >= 0.0f && volume <= 1.0f) {
			this.volume = volume;
			if (sourceDataLine != null) {
				FloatControl gainControl = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
				float gain = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
				gainControl.setValue(gain);
			}
		}
	}
}