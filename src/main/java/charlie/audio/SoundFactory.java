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

import static charlie.audio.Effect.CHIPS_IN;
import static charlie.audio.Effect.PUSH;
import java.util.Random;

/**
 * This class implements the factory pattern for managing and playing sounds.
 * WAV files must be 8-bit.
 * See here for online converter: https://audio.online-convert.com/convert-to-wav
 * @author Ron Coleman
 */
public class SoundFactory {   
//    private final static Sound DEAL_SOUND0 = new Sound("audio/PlayingCardsPo_eOnFelt01_87.wav");
    private final static Sound DEAL_SOUND1 = new Sound("audio/tap.wav");
//    private final static Sound DEAL_SOUND2 = new Sound("audio/Telemet_33G_HD2-32076.wav");
    public final static Sound[] CHARLIE_SOUNDS = { 
        new Sound("audio/shazam2.wav"),
//        new Sound("audio/impressive-2.wav"),
        new Sound("audio/mostimprs-2.wav")
    };
    public final static Sound[] BJ_SOUNDS = { 
        new Sound("audio/you-can-do-it.wav"), 
        new Sound("audio/you-got-it-1.wav"),
        new Sound("audio/wahoo.wav"),
        new Sound("audio/impressive-2.wav")
    };
    public final static Sound[] NICE_SOUNDS = { 
        new Sound("audio/wow.wav"), 
        new Sound("audio/austin_yeahbaby_converted.wav"),
        new Sound("audio/woow.wav"),
        new Sound("audio/goodshot-2.wav"),
        new Sound("audio/Glock Success Ding.wav"),
        new Sound("audio/Win Game Sound.wav"),
        new Sound("audio/Win_Short_Sound.wav"),
        new Sound("audio/You Win A.wav"),
        new Sound("audio/You Win B.wav"),
    };
    public final static Sound[] OUCH_SOUNDS = {
        new Sound("audio/evil_laf.wav"), 
        new Sound("audio/aaaah.wav"), 
        new Sound("audio/bone_converted.wav"), 
        new Sound("audio/glass.wav"),
        new Sound("audio/job-1.wav"),
        new Sound("audio/better-1.wav"),
        new Sound("audio/1doh.wav"),
        new Sound("audio/ow2.wav"),
        new Sound("audio/funny-2.wav"),
        new Sound("audio/swvader01.wav"),
        new Sound("audio/jabba-the-hutt-laughing.wav"),
        new Sound("audio/ouch.wav"),
        // https://www.mediacollege.com/downloads/sound-effects/star-wars/
        new Sound("audio/hansolo_badfeeling-2.wav"),
        new Sound("audio/Fail.wav"),
        new Sound("audio/wrong 3.wav"),
    };
    public final static Sound[] PUSH_SOUNDS ={ 
        new Sound("audio/trap.wav") ,
        new Sound("audio/Arcade Game Down Bleeps.wav")
//        new Sound("audio/whatwsth.wav")
    };
    private final static Sound DOUBLE_DOWN_SOUND = new Sound("audio/Arcade Dungeon Pickup Collect.wav");
    private final static Sound SPLIT_SOUND = new Sound("audio/Arcade Dungeon Health Restored.wav");
    private final static Sound CHIPS_IN_SOUIND = new Sound("audio/Games_Poker_Chip_08950004.wav");
    private final static Sound CHIPS_OUT_SOUND = new Sound("audio/Games_Poker_Chip_08950003.wav");
    private final static Sound SHUFFLE_SOUND = new Sound("audio/013012_Casino-Cards_28_A1-25.wav");
    private final static Sound TURN_SOUND = new Sound("audio/Telemet_33G_HD2-32076.wav");
    private final static Sound NO_BET_SOUND = new Sound("audio/Arcade Fun Wrong Move.wav");
    private final static Sound BAD_PLAY_SOUND = new Sound("audio/Incorrect.wav");
    private final static Sound ADIOS_SOUND = new Sound("audio/Arcade Fun Fall.wav");
    
    private static long lastTime = System.currentTimeMillis();  
    
    protected static Random toss = new Random();
    
    private static boolean enabled = true;

    /**
     * Primes the sound line so that playing sounds will be more real-time.
     */
    public static void prime() {
        // Get any sound
        Sound sound = CHARLIE_SOUNDS[CHARLIE_SOUNDS.length-1];
        
        // Set volume to allowed minimum
        float volume = sound.getVolume();
        
        sound.setVolume(-80.0f);
        
        // Play the sound
        sound.play();
        
        // Restore volume
        sound.setVolume(volume);
    }
    
    /**
     * Enable sounds to be played.
     * @param state True if sounds are enabled.
     */
    public static void enable(boolean state) {
        enabled = state;
    }
    
    /**
     * Plays a sound
     * @param e Effect Effect to play
     */
    public static void play(Effect e) {
        if(!enabled)
            return;
        
        switch(e) {
            case TURN:
                TURN_SOUND.play();
                break;
            case SHUFFLING:
                backgroundPlay(SHUFFLE_SOUND);
                break;
            case DEAL:
                backgroundPlay(DEAL_SOUND1);
                break;
            case CHARLIE:
                CHARLIE_SOUNDS[toss.nextInt(CHARLIE_SOUNDS.length)].play();
                break;
            case BJ:
                BJ_SOUNDS[toss.nextInt(BJ_SOUNDS.length)].play();
                break;
            case NICE:
                NICE_SOUNDS[toss.nextInt(NICE_SOUNDS.length)].play();
                break;
            case TOUGH:
                OUCH_SOUNDS[toss.nextInt(OUCH_SOUNDS.length)].play();  
                break;
            case PUSH:
                PUSH_SOUNDS[toss.nextInt(PUSH_SOUNDS.length)].play();
                break;    
            case BUST:
                // For some reason this code does not play a sound.
//                BREAK_SOUNDS[toss.nextInt(BREAK_SOUNDS.length)].play();
                break; 
            case CHIPS_IN:
                backgroundPlay(CHIPS_IN_SOUIND);
                break;
            case CHIPS_OUT:
                backgroundPlay(CHIPS_OUT_SOUND);
                break;    
            case NO_BET:
                backgroundPlay(NO_BET_SOUND);
                break;
            case BAD_PLAY:
                backgroundPlay(BAD_PLAY_SOUND);
                break;
            case ADIOS:
                backgroundPlay(ADIOS_SOUND);
                break;
            case DOUBLE_DOWN:
                backgroundPlay(DOUBLE_DOWN_SOUND);
                break;
            case SPLIT:
                backgroundPlay(SPLIT_SOUND);
        }        
    }
    
    /**
     * Plays a sound once in the background.
     * @param sound Sound
     */
    protected static void backgroundPlay(final Sound sound) {
        backgroundPlay(sound, 1);
    }
    
    /**
     * Plays a looping sound in the background.
     * @param sound Sound
     * @param loop Number of times to loop sound
     */
    protected static void backgroundPlay(final Sound sound,final int loop) {
        long now = System.currentTimeMillis();
        
        if(now - lastTime < 500)
            return;
        
        lastTime = now;
        
        new Thread(new Runnable() { 
            @Override
            public void run() {
                for(int i=0; i < loop; i++)
                    sound.play();
            }
        }).start();
    } 
}