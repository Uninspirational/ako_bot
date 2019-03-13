package com.AkoBot.Bandori;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Member;


public class BandoriSong {
    private String name;
    private String url;
    private AudioTrack audioTrack;
    private int id;
    private SongType songType;
    private String band;
    private String wiki;
    public BandoriSong(String name, String url, AudioTrack audioTrack, int id, SongType songType) {
        this.name = name;
        this.url = url;
        this.audioTrack = audioTrack;
        this.id = id;
        this.songType = songType;
    }

    /**
     * constructors
     * @param name of song
     * @param url to
     * @param id
     * @param songType
     */
    public BandoriSong(String name, String url, int id, SongType songType) {
        this(name, url, null, id, songType);
    }
    public BandoriSong(String name, String url, SongType songType) {
        this(name, url, null, -1, songType);
    }

    public void setBand(String band) { this.band = band; }
    public void setName(String name) { this.name = name; }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }

    public String getWiki() {
        return wiki;
    }

    public String getBand() {
        return band;
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }

    public SongType getSongType() { return this.songType; }

    public int getId() { return this.id; }

    public void setId(int id) {
        this.id = id;
    }

    public AudioTrack getAudioTrack() {
        return this.audioTrack;
    }

    public void setAudioTrack(AudioTrack audioTrack) {
        this.audioTrack = audioTrack;
    }
}
