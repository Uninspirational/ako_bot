package com.AkoBot.Music;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

public class AudioListener extends AudioEventAdapter {
    private final BlockingQueue<Song> tracks;
    private final AudioPlayer audioPlayer;
    private Song playing;
    /**
     * initializes the audio player
     *
     * @param audioPlayer audioPlayer
     */
    AudioListener(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.tracks = new LinkedBlockingQueue<>();
    }
    /**
     * returns songs in queue
     *
     * @return the queued songs
     */
    public Queue<Song> getTracks() {
        return this.tracks;
    }

    /**
     *
     * @return returns the audioplayer
     */
    private AudioPlayer getAudioPlayer() {
        return this.audioPlayer;
    }

    /**
     * adds a song to the queue
     * @param song song to add
     */
    void addTrack(Song song) {
        tracks.add(song);
        if (audioPlayer.getPlayingTrack() == null) {
            if (song.getBandoriSong() != null) {
                audioPlayer.startTrack(song.getBandoriSong().getAudioTrack(), false);
            }
            else {
                playing = song;
                audioPlayer.startTrack(song.getAudioTrack(), false);
            }
        }

    }

    /**
     * play the next song
     */
    @SuppressWarnings("ConstantConditions")
    void playNextTrack() throws NullPointerException{
        tracks.poll();
        try
        {
            playing = tracks.peek();
            getAudioPlayer().startTrack((tracks.peek().getAudioTrack()), false);
        }
        catch (NullPointerException e)
        {
            playing = null;
            getAudioPlayer().destroy();
//            getAudioPlayer().getPlayingTrack().stop();
        }

    }

    /**
     *
     * @param audioPlayer audioplayer
     * @param audioTrack audiotrack
     */
    @Override
    public void onTrackStart(AudioPlayer audioPlayer, AudioTrack audioTrack) {
        playing = tracks.peek();
    }

    /**
     * returns top of the list
     * @return song playing now
     */
    public Song currentlyPlaying() {
        return playing;
//        return new Song(audioPlayer.getPlayingTrack(), tracks.peek().getMember(), tracks.peek().getBandoriSong());
    }
    /**
     * handles what to do after a song ends
     *
     * @param player    the player in use to play music
     * @param track     track
     * @param endReason reason for ending
     */
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            playNextTrack();
        }
    }
}
