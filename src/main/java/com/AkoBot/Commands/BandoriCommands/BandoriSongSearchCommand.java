package com.AkoBot.Commands.BandoriCommands;

import com.AkoBot.Bandori.BandoriCollection;
import com.AkoBot.Bandori.BandoriSong;
import com.AkoBot.Bandori.BandoriSongs;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

public class BandoriSongSearchCommand {
    public void searchBandoriSong(MessageReceivedEvent messageReceivedEvent, BandoriSongs bandoriSongsv2) {
        Hashtable<String, BandoriCollection> collection = bandoriSongsv2.getHashTable();
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        String message = messageReceivedEvent.getMessage().getContentStripped();
        ArrayList<String> keys = bandoriSongsv2.getKeys();
        ArrayList<String> hits = new ArrayList<>();
        LinkedList<BandoriSong> songs;
        try {
            String term = message.substring(message.indexOf(" ")).toLowerCase();
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

            //if one song was found
            if (hits.size() == 1) {
                BandoriCollection result = collection.get(hits.get(0));
                embedBuilder
                        .setTitle(result.getName(), result.getWikiUrl())
                        .setThumbnail(result.getThumbNail())
                        .setColor(new Color(0xBA00BA))
                        .setAuthor(result.getBand())
                        .addField("Description", result.getDescription(), false);
                songs = result.getAllSongs();
                for (BandoriSong bandoriSong: songs) {
                    embedBuilder.addField("", "[" + bandoriSong.getName() + "]" + "(" + bandoriSong.getUrl() + ")",  false);
                }

            }

            //if no songs were found
            else if (hits.size() == 0) {
                embedBuilder
                        .setTitle("No results found for " + term)
                        .setThumbnail("https://vignette.wikia.nocookie.net/bandori/images/8/84/Stamp_014001_en.png/revision/latest?cb=20180619053434")
                        .setColor(new Color(0xBA00BA))
                        .addField("Try broadening your search term\n Make sure to only include alphanumerical characters", "", false);
            }

            //if multiple songs were found
            else {
                embedBuilder
                        .setTitle("Multiple results found for " + term)
                        .setThumbnail("https://vignette.wikia.nocookie.net/bandori/images/8/84/Stamp_014001_en.png/revision/latest?cb=20180619053434")
                        .setColor(new Color(0xBA00BA));
                for (String hit: hits) {
                    embedBuilder.addField("", "[" + collection.get(hit).getName() + "]" + "(" + collection.get(hit).getWikiUrl() + ")", false);
                }
            }

            //send embed message
            try {
                MessageBuilder messageBuilder = new MessageBuilder()
                        .setEmbed(embedBuilder.build());
                textChannel.sendMessage(messageBuilder.build()).queue();
            }
            catch (Exception e) {
                textChannel.sendMessage("Too many results! Try refining your search options").queue();
            }
        }
        catch (StringIndexOutOfBoundsException e) {
            textChannel.sendMessage("$help2 for more information").queue();
        }
    }
}
