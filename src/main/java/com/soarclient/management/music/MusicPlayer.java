package com.soarclient.management.music;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

public class MusicPlayer implements Runnable {

	private Runnable runnable;

	private AudioInputStream audioInputStream;
	private AudioFormat audioFormat;
	private SourceDataLine sourceDataLine;

	private Music currentMusic;
	private boolean playing;
	private float volume;

	public MusicPlayer(Runnable runnable) {
		this.playing = false;
		this.volume = 0.5F;
		this.runnable = runnable;
	}

	@Override
	public void run() {

		if (currentMusic != null && playing) {

			try (AudioInputStream encodedInputStream = AudioSystem.getAudioInputStream(currentMusic.getAudio())) {

				AudioFormat encodedFormat = encodedInputStream.getFormat();
				AudioInputStream decodedInputStream = AudioSystem.getAudioInputStream(
						new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, encodedFormat.getSampleRate(), 16,
								encodedFormat.getChannels(), 2 * encodedFormat.getChannels(),
								encodedFormat.getFrameRate(), encodedFormat.isBigEndian(), encodedFormat.properties()),
						encodedInputStream);

				audioFormat = decodedInputStream.getFormat();
				audioInputStream = AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
						44100, audioFormat.getSampleSizeInBits(), audioFormat.getChannels(), audioFormat.getFrameSize(),
						44100, audioFormat.isBigEndian(), audioFormat.properties()), decodedInputStream);

				AudioFormat playbackFormat = audioInputStream.getFormat();
				sourceDataLine = AudioSystem.getSourceDataLine(playbackFormat);

				sourceDataLine.open();
				sourceDataLine.start();

				byte[] buffer = new byte[44100];

				for (int read = audioInputStream.read(buffer); read != -1; read = audioInputStream.read(buffer)) {

					while (!playing) {
						Thread.sleep(10);
					}

					sourceDataLine.write(buffer, 0, read);
				}

				sourceDataLine.drain();
				sourceDataLine.close();
				audioInputStream.close();
				runnable.run();
			} catch (Exception e) {
				e.printStackTrace();
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

		if (audioInputStream != null) {
			try {
				audioInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
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

		if (sourceDataLine == null || audioInputStream == null || audioFormat == null) {
			return 0;
		}

		return (float) (sourceDataLine.getMicrosecondPosition() / 1000000.0);
	}

	public float getEndTime() {

		if (sourceDataLine == null || audioInputStream == null || audioFormat == null) {
			return 0;
		}

		return (float) (audioInputStream.getFrameLength() / audioFormat.getFrameRate());
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