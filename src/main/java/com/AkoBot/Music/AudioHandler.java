package com.AkoBot.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.core.audio.AudioSendHandler;
public class AudioHandler implements AudioSendHandler{
    private final AudioPlayer audioPlayer;
    private AudioFrame lastFrame;

    /**
     * initializes the AudioPlayer
     *
     * @param audioPlayer audioPlayer to be created
     */
    public AudioHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    /**
     * checks to see if next section of audio can be played
     *
     * @return null if unable
     */
    public boolean canProvide() {
        if (lastFrame == null)
            lastFrame = audioPlayer.provide();
        return lastFrame != null;
    }

    /**
     * sends a byte array containing 20ms of audio
     *
     * @return the byte array
     */
    public byte[] provide20MsAudio() {
        byte[] data = canProvide() ? lastFrame.getData() : null;
        lastFrame = null;
        return data;
    }
    public boolean isOpus() {
        return true;
    }
}
