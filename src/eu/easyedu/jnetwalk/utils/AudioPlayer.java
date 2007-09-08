/*
 * AePlayer.java
 *
 * Created on 8.9.2007, 16:51:31
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.easyedu.jnetwalk.utils;

/**
 *
 * @author hlavki
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer extends Thread {

    private String fileName;
    private static final transient Map<String, Clip> cache = new HashMap<String, Clip>();

    public AudioPlayer(String wavfile) {
        fileName = wavfile;
    }

    @Override
    public void run() {
        playSound();
    }

    private void playSound() {
        Clip clip = cache.get(fileName);
        try {
            if (clip == null) {
                AudioInputStream stream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(fileName));

                // At present, ALAW and ULAW encodings must be converted
                // to PCM_SIGNED before it can be played
                AudioFormat format = stream.getFormat();
                if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                    format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() *
                            2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true); // big endian
                    stream = AudioSystem.getAudioInputStream(format, stream);
                }

                // Create the clip
                DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat(), (int) stream.getFrameLength() *
                        format.getFrameSize());
                clip = (Clip) AudioSystem.getLine(info);

                // This method does not return until the audio file is completely loaded
                clip.open(stream);
                cache.put(fileName, clip);
            }

            // Start playing
            clip.setFramePosition(0);
            clip.start();
        } catch (IOException e) {
        } catch (LineUnavailableException e) {
        } catch (UnsupportedAudioFileException e) {
        }
    }

//    @Override
//    protected void finalize() throws Throwable {
//        super.finalize();
//        for (Clip clip : cache.values()) {
//            clip.drain();
//            clip.close();
//        }
//    }


}