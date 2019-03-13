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
    /**
     * initializes the audio player
     *
     * @param audioPlayer audioPlayer
     */
    public AudioListener(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.tracks = new LinkedBlockingQueue<Song>();
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
    public AudioPlayer getAudioPlayer() {
        return this.audioPlayer;
    }

    /**
     * adds a song to the queue
     * @param song song to add
     */
    public void addTrack(Song song) {
        System.out.println("addTrack ran");
        tracks.add(song);
        if (audioPlayer.getPlayingTrack() == null) {
            System.out.println("track is not empty");
            if (song.getBandoriSong() != null) {
                audioPlayer.startTrack(song.getBandoriSong().getAudioTrack(), false);
                System.out.println("Playing bandori song");
            }
            else {
                audioPlayer.startTrack(song.getAudioTrack(), false);
                System.out.println("Playing non-bandori song");
            }
        }

    }

    /**
     * returns the size of the queue
     *
     * @return queue size
     */
    public int getTrackSize() {
        return tracks.size();
    }
    /**
     * play the next song
     */
    public void playNextTrack() throws NullPointerException{
        System.out.println("playNextTrack ran");
        tracks.poll();
        try
        {
            System.out.println(tracks.peek().getAudioTrack().getInfo().title);
            getAudioPlayer().startTrack((tracks.peek().getAudioTrack()), false);
            System.out.println(getAudioPlayer().getPlayingTrack().getInfo().title);
        }
        catch (NullPointerException e)
        {
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
        System.out.println(audioPlayer.getPlayingTrack().getInfo().title);
        System.out.println("onTrackStart ran");
    }

    /**
     * returns top of the list
     * @return song playing now
     */
    public Song currentlyPlaying() {
        return new Song(audioPlayer.getPlayingTrack(), tracks.peek().getMember(), tracks.peek().getBandoriSong());
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
        System.out.println("ontrackend ran");
        if (endReason.mayStartNext) {
            playNextTrack();
        }
    }
}
