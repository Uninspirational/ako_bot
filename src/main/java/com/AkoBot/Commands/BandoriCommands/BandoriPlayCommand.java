package com.AkoBot.Commands.BandoriCommands;

import com.AkoBot.Bandori.*;
import com.AkoBot.Commands.BandoriCommands.BandoriSongLoadCommand;
import com.AkoBot.Commands.JoinCommand;
import com.AkoBot.Music.MusicManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

public class BandoriPlayCommand {
    public void bplay(MessageReceivedEvent messageReceivedEvent, MusicManager musicManager, BandoriSongs bandoriSongs) {
        //objects from messageReceivedEvent and com.AkoBot classes
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        Member member = messageReceivedEvent.getMember();
        String message = messageReceivedEvent.getMessage().getContentStripped();
        JoinCommand joinCommand = new JoinCommand();
        BandoriSongLoadCommand loadBandoriSongCommand = new BandoriSongLoadCommand();

        //objects from bandoriSongsv2
        Hashtable<String, BandoriCollection> collection = bandoriSongs.getHashTable();
        if (collection == null) {
            textChannel.sendMessage("No songs are loaded, refresh").queue();
            return;
        }
        ArrayList<String> keys = bandoriSongs.getKeys();

        //other objects
        ArrayList<String> hits = new ArrayList<>();
        LinkedList<BandoriSong> songs;
        boolean all = false;
        boolean hasType = false;
        SongType songType = null;
        BandoriTypes bandoriTypes= new BandoriTypes();
        BandoriSong bandoriSong;


        //break down message into parts
        String term = message.substring(message.indexOf(" ") + 1).toLowerCase();

        //tags
        if (term.startsWith("-all")) {
            all = true;
            term = term.replace("-all ", "");
        }
        if (term.startsWith("-")) {
            term = term.substring(1);
            songType = bandoriTypes.getSongType(term);
            if (songType != null)
                hasType = true;
            term = term.substring(term.indexOf(" "));
        }
        term = term.replace(" ", "");
        EmbedBuilder embedBuilder = new EmbedBuilder();

        //search for songs by song name, exact match
        for (String key: keys) {
            if (key.toLowerCase().equals(term)) {
                hits.add(key);
            }
        }

        //if no songs were found, find closest matches
        if (hits.size() == 0) {
            for (String key: keys) {
                if (key.toLowerCase().contains(term)) {
                    hits.add(key);
                }
            }
        }

        //if still no songs were found
        if (hits.size() == 0) {
            embedBuilder
                    .setTitle("No results found for " + term)
                    .addField("Try broadening your search term\n Make sure to only include alphanumerical characters", "", false)
                    .setThumbnail("https://vignette.wikia.nocookie.net/bandori/images/8/84/Stamp_014001_en.png/revision/latest?cb=20180619053434")
                    .setColor(new Color(0xBA00BA));
        }
        //if multiple songs matched
        else if (hits.size() > 1) {
            //try to join voice channel, cancel if failed
            if (!joinCommand.Join(messageReceivedEvent, musicManager))
                return;
            //if user did not ask to play all the songs
            if (!all) {
                embedBuilder
                        .setTitle("Multiple results found for " + term)
                        .setThumbnail("https://vignette.wikia.nocookie.net/bandori/images/8/84/Stamp_014001_en.png/revision/latest?cb=20180619053434")
                        .setColor(new Color(0xBA00BA))
                        .addField("Try narrowing your search term or use the **$bplay -all <song name> **" +
                                "command to add all songs that matches the name\n Make sure to only include alphanumerical characters", "", false);
                for (String hit: hits) {
                    embedBuilder.addField("", "[" + collection.get(hit).getName() + "]" + "(" + collection.get(hit).getWikiUrl() + ")", false);
                }
            }
            //if user did ask to play all the songs
            else {
                embedBuilder
                        .setTitle("Multiple results found for " + term)
                        .setThumbnail("https://vignette.wikia.nocookie.net/bandori/images/8/84/Ako_PICO_Icon.png/revision/latest?cb=20180715113757")
                        .setColor(new Color(0xBA00BA));
                //go through all matching songs
                for (String hit: hits) {
                    songs = collection.get(hit).getAllSongs();
//                    embedBuilder.addField("", "[" + collection.get(hit).getName() + "]" + "(" + collection.get(hit).getWikiUrl() + ") other version", false);
                    //if song type was requested
                    if (hasType) {
                        bandoriSong = collection.get(hit).searchByType(songType);
                        if (bandoriSong != null) {
                            loadBandoriSongCommand.loadBandoriSong(textChannel, bandoriSong, member, musicManager);
                            embedBuilder.addField("", "[" + bandoriSong.getName() + "](" + bandoriSong.getWiki() + ")" + " added to queue", false);
                        }
                    }
                    //if song type was not requested, default to first song
                    else {
                        bandoriSong = songs.get(0);
                        loadBandoriSongCommand.loadBandoriSong(textChannel, bandoriSong, member, musicManager);
                        embedBuilder.addField("","[" + bandoriSong.getName() + "](" + bandoriSong.getWiki()+ ")" + " added to queue", false);
                    }
                }
            }
        }
        //if one song matched
        else {
            //try to join voice channel, cancel if unable
            if (!joinCommand.Join(messageReceivedEvent, musicManager))
                return;
            //one song matched, so get the one song
            BandoriCollection result = collection.get(hits.get(0));
            //embed message to send
            embedBuilder
                    .setTitle(result.getName(), result.getWikiUrl())
                    .setThumbnail(result.getThumbNail())
                    .setDescription("Found exact match")
                    .setAuthor(result.getBand());
            //try to set color to band color if there is one
            //other wise use default color
            if (result.getBandSize() == 1)
                embedBuilder.setColor(bandoriTypes.getColor(bandoriTypes.getBandType(result.getBand())));
            else
                embedBuilder.setColor(new Color(0xBA00BA));

            //if song type was requested
            if (hasType) {
                bandoriSong = result.searchByType(songType);
                loadBandoriSongCommand.loadBandoriSong(textChannel, bandoriSong, member, musicManager);
                embedBuilder.addField("","[" + bandoriSong.getName() + "](" + bandoriSong.getWiki()+ ")" + " added to queue", false);
            }
            //if song type was not requested
            else {
                songs = result.getAllSongs();
                bandoriSong = songs.get(0);
                loadBandoriSongCommand.loadBandoriSong(textChannel, songs.get(0), member, musicManager);
                embedBuilder.addField("","[" + bandoriSong.getName() + "](" + bandoriSong.getWiki()+ ")" + " added to queue", false);
            }
        }
        //send embed message
        MessageBuilder messageBuilder = new MessageBuilder()
                .setEmbed(embedBuilder.build());
        textChannel.sendMessage(messageBuilder.build()).queue();
    }
}
