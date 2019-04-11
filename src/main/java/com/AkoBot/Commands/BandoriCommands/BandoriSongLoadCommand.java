package com.AkoBot.Commands.BandoriCommands;

import com.AkoBot.Bandori.BandoriSong;
import com.AkoBot.Music.MusicManager;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

class BandoriSongLoadCommand {
    void loadBandoriSong(TextChannel textChannel, BandoriSong bandoriSong, Member member, MusicManager musicManager) {
        String url = bandoriSong.getUrl();
        musicManager.loadTrack(textChannel, url, member, bandoriSong);
    }
}
