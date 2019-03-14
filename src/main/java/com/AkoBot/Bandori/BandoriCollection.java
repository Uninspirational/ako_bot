package com.AkoBot.Bandori;

import java.util.ArrayList;
import java.util.LinkedList;

public class BandoriCollection {
    private LinkedList<BandoriSong> bandoriSongs;
    private String name;
    private BandType[] band;
    private String wikiUrl;
    private int length = 0;
    private String thumbNail;
    private String key;
    private String description;
    public BandoriCollection(String name, String wikiUrl, String description, BandType[] band, String thumbNail, String key) {
        this.name = name;
        this.wikiUrl = wikiUrl;
        this.bandoriSongs = new LinkedList<>();
        this.thumbNail = thumbNail;
        this.key = key;
        this.description = description;
        this.band = band;
    }
    public BandoriSong getFirstSong() { return this.bandoriSongs.get(0); }

    public void setName(String name) {
        this.name = name;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

    public void setWikiUrl(String wikiUrl) {
        this.wikiUrl = wikiUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setBand(BandType[] band) {
        this.band = band;
    }

    public void addSong(BandoriSong bandoriSong) {
        this.length++;
        this.bandoriSongs.add(bandoriSong);
    }

    public LinkedList<BandoriSong> getAllSongs() {
        return this.bandoriSongs;
    }

    public BandoriSong searchByType(String type) {
        SongType songType = null;
        type = type.toLowerCase();
        if (name.contains("game ver")) {
            songType = SongType.GAME_VERSION;
        }
        else if (type.contains("instrumental")) {
            songType = SongType.INSTRUMENTAL;
        }
        else if (type.contains("remaster")) {
            songType = SongType.REMASTER;
        }
        else if (type.contains("remaster") && type.contains("instrumental")) {
            songType = SongType.REMASTER_INSTRUMENTAL;
        }
        else if (type.contains("short ver")) {
            songType = SongType.SHORT;
        }
        else if (type.contains("live")) {
            if (type.contains("encore")) {
                songType = SongType.ENCORE_LIVE;
            }
            else {
                songType = SongType.LIVE;
            }
        }
        else if (type.contains("acoustic")) {
            if (type.contains("instrumental")) {
                songType = SongType.ACOUSTIC_INSTRUMENTAL;
            }
            else if  (type.contains("popipa")) {
                songType = SongType.POPIPA_ACOUSTIC;
            }
            else {
                songType = SongType.ACOUSTIC;
            }
        }
        else if (type.contains("solo ver")) {
            songType = SongType.SOLO;
        }
        else if (type.contains("tv size")) {
            songType = SongType.TV_SIZE;
        }
        else if (type.contains("full")){
            songType = SongType.FULL_VERSION;
        }
        for (int i = 0; i < length; i++) {
            if (bandoriSongs.get(i).getSongType().equals(songType)) {
                return bandoriSongs.get(i);
            }
        }
        return null;
    }

    public BandoriSong searchByType(SongType songType) {
        for (int i = 0; i < this.length; i++) {
            if (this.bandoriSongs.get(i).getSongType().equals(songType)) {
                return this.bandoriSongs.get(i);
            }
        }
        return null;
    }

    public String getBand() {
        int i = 0;
        String result = "";
        BandoriTypes bandoriTypes = new BandoriTypes();
        while (this.band != null && this.band[i] != null) {
            if (i != 0)
                result = result.concat(", ");
            result = result.concat(bandoriTypes.getBandString(this.band[i++]));
        }
        return result;
    }

    public String getName() {
        return this.name;
    }

    public String getWikiUrl() {
        return this.wikiUrl;
    }

    public String getThumbNail() {
        return this.thumbNail;
    }

    public int getBandSize() { return this.band.length; }
}
