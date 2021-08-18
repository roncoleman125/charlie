/*
 Copyright (c) 2014 Ron Coleman

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package charlie.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.apache.log4j.Logger;

/**
 * This class is a facade for the Java sound API. It was modified from the
 * Tritonus library to be play the sound multiple times without having to
 * reload the file. Thus, it is memory inefficient. It handles only WAV
 * formats.
 * See http://www.jsresources.org/examples/audio_playing_recording.html.
 * The file must have the following characteristics:
 * characteristics:<br>
 * Dynamic range: 16-bits<br>
 * Sample rate: 8K samples/s<br>
 * Channels: 1 (mono)<br>
 * For some samples I also normalized the audio
 * (see http://www.learndigitalaudio.com/blog/normalize-audio) although
 * I don't see how this is necessary.<br>
 * In any case, see http://audio.online-convert.com/convert-to-wav
 * convert to WAV format.
 * 
 * @author Ron Coleman
 */
public class Sound {
    private static final Logger LOG = Logger.getLogger(Sound.class); 
    
    // Buffer size to read in the sound -- it must be big enough to hold
    // the PCM otherwise the sound ending will be clipped.
    private final int BUFFER_SIZE = 128000*4;
    
    private SourceDataLine line;
    private AudioInputStream audioInputStream;
    private Bais bais;
    private String path;

    public static void main(String[] args) {
        Sound s = new Sound("audio/013012_Casino-Cards_27_A1.wav");
        
        s.play();
    }

    /**
     * Constructor
     * @param path File name
     */
    public Sound(String path) {
        this.path = path;
        File soundFile = new File(path);

        /*
         We have to read in the sound file.
         */
        audioInputStream = null;
        try {
            FileInputStream fis = new FileInputStream(soundFile);
            byte[] buf = new byte[BUFFER_SIZE];

            int n = fis.read(buf, 0, BUFFER_SIZE);
            
            bais = new Bais(buf, 0, n);
            
            audioInputStream = AudioSystem.getAudioInputStream(bais);
        } catch (FileNotFoundException e) {
            LOG.error(e+"");
        }
        catch(IOException | UnsupportedAudioFileException e) {
            LOG.error(e+"");
        }

        /*
         From the AudioInputStream, i.e. from the sound file,
         we fetch information about the format of the
         audio data.
         These information include the sampling frequency,
         the number of
         channels and the size of the samples.
         These information
         are needed to ask Java Sound for a suitable output line
         for this audio file.
         */
        AudioFormat audioFormat = audioInputStream.getFormat();

        /*
         Asking for a line is a rather tricky thing.
         We have to construct an Info object that specifies
         the desired properties for the line.
         First, we have to say which kind of line we want. The
         possibilities are: SourceDataLine (for playback), Clip
         (for repeated playback)	and TargetDataLine (for
         recording).
         Here, we want to do normal playback, so we ask for
         a SourceDataLine.
         Then, we have to pass an AudioFormat object, so that
         the Line knows which format the data passed to it
         will have.
         Furthermore, we can give Java Sound a hint about how
         big the internal buffer for the line should be. This
         isn't used here, signaling that we
         don't care about the exact size. Java Sound will use
         some default value for the buffer size.
         */
        line = null;
        
        DataLine.Info info = new DataLine.Info(SourceDataLine.class,audioFormat);
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);

            /*
             The line is there, but it is not yet ready to
             receive audio data. We have to open the line.
             */
            line.open(audioFormat);
        } catch (LineUnavailableException e) {
            LOG.error(e+"");
        } catch (Exception e) {
            LOG.error(e+"");
        }
    }

    /**
     * Plays the sound synchronously.
     */
    public void play() {
        try {
            bais.rewind();
            
            audioInputStream = AudioSystem.getAudioInputStream(bais);
        } catch (UnsupportedAudioFileException | IOException ex) {
            LOG.error(ex+"");
        }
        
        /*
         Still not enough. The line now can receive data,
         but will not pass them on to the audio output device
         (which means to your sound card). This has to be
         activated.
         */
        line.start();
        

        /*
         Ok, finally the line is prepared. Now comes the real
         job: we have to write data to the line. We do this
         in a loop. First, we read data from the
         AudioInputStream to a buffer. Then, we write from
         this buffer to the Line. This is done until the end
         of the file is reached, which is detected by a
         return value of -1 from the read method of the
         AudioInputStream.
         */

        int nBytesRead = 0;
        byte[] bytes = new byte[BUFFER_SIZE];
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioInputStream.read(bytes, 0, bytes.length);
            } catch (IOException e) {
                LOG.error(e+"");
            }
            if (nBytesRead >= 0) {
                line.write(bytes, 0, nBytesRead);
            }
        }
//        try {
//            audioInputStream.reset();
//        } catch (IOException ex) {
//            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
//        }

        /*
         Wait until all data are played.
         This is only necessary because of the bug noted below.
         (If we do not wait, we would interrupt the playback by
         prematurely closing the line and exiting the VM.)
		 
         Thanks to Margie Fitch for bringing me on the right
         path to this solution.
         */
        line.drain();

        //        /*
        //         All data are played. We can close the shop.
        //         */
//                line.close();        

    }
    
    /**
     * Sets the volume in relative decibels.<br>
     * See http://stackoverflow.com/questions/953598/audio-volume-control-increase-or-decrease-in-java
     * @param db Decibels
     */
    public void setVolume(float db) {
        FloatControl gainControl =
            (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
        
        gainControl.setValue(db);

        gainControl.getValue();
    }
    
    /**
     * Gets the volume in relative decibels.
     * @return Volume
     */
    public float getVolume() {
        FloatControl gainControl =
            (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);

        float vol = gainControl.getValue();
        
        return vol;
    }
    
    /**
     * Gets the sound file path.
     * @return File system path
     */
    public String getPath() {
        return path;
    }
}

