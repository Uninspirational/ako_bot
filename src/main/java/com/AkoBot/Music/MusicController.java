package com.AkoBot.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.core.entities.Guild;

public class MusicController {
    private final AudioPlayer audioPlayer;
    private final AudioListener listener;
    private final Guild guild;

    /**
     * create a music player for the guild to play music for
     * @param audioPlayer AudioPlayer
     * @param guild guild to play in
     */
    public MusicController(AudioPlayer audioPlayer, Guild guild){
        this.audioPlayer = audioPlayer;
        this.guild = guild;
        this.listener = new AudioListener(audioPlayer);
        audioPlayer.addListener(listener);
    }

    /**
     *
     * @return return the audioplayer
     */
    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    /**
     *
     * @return return the guild currently playing in
     */
    public Guild getGuild() {
        return guild;
    }

    /**
     *
     * @return return the audiolistener
     */
    public AudioListener getListener() {
        return listener;
    }

    /**
     *
     * @return return a new audiohandler
     */
    public AudioHandler getAudioHandler(){
        return new AudioHandler(audioPlayer);
    }

    /**
     * add a track to the queue
     * @param song the track to add
     */
    public void playTrack(Song song){
        listener.addTrack(song);
    }
    /**
     * go to the next song
     */
    public void skipTrack() throws NullPointerException{
        listener.playNextTrack();
    }
}
