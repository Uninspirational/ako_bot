package com.AkoBot.Music;

import com.AkoBot.Bandori.BandoriSong;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Member;

public class Song{
    private AudioTrack audioTrack;
    private Member member;
    private BandoriSong bandoriSong;
    private String url;

    /**
     * Song object to store information
     * @param audioTrack the actual track
     * @param member person who requested the song
     * @param bandoriSong bandori song (may be null)
     */
    Song(AudioTrack audioTrack, Member member, BandoriSong bandoriSong){
        this.audioTrack = audioTrack;
        this.member = member;
        this.bandoriSong = bandoriSong;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AudioTrack getAudioTrack() {
        return audioTrack;
    }

    public Member getMember() {
        return member;
    }

    public BandoriSong getBandoriSong() {
        return bandoriSong;
    }
}
