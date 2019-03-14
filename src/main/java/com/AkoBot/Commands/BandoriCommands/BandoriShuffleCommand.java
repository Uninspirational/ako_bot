package com.AkoBot.Commands.BandoriCommands;

import com.AkoBot.Bandori.*;
import com.AkoBot.Commands.JoinCommand;
import com.AkoBot.Music.MusicManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class BandoriShuffleCommand {
    public void bShuffle(MessageReceivedEvent messageReceivedEvent, MusicManager musicManager, BandoriSongs bandoriSongs) {
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        Member member = messageReceivedEvent.getMember();
        String message = messageReceivedEvent.getMessage().getContentRaw();
        try {
            //$bshuffle <all || bandname> <null || -type song type> <null || -r>;
            BandoriTypes bandoriTypes = new BandoriTypes();
            SongType songType = null;
            BandType bandType = null;
            boolean random = false, all = false;
            ArrayList<BandoriSong> chosen;
            message = message.substring(message.indexOf(" ") + 1);
            if (message.endsWith("-r")) {
                random = true;
                message = message.substring(0, message.length() - 3);
            }
            if (message.contains("-type")) {
                songType = bandoriTypes.getSongType(message.substring(message.indexOf("-type ")));
                message = message.substring(0, message.indexOf("-type "));
            }
            if (message.startsWith("all")){
                all = true;
                if (songType != null)
                    message = message.substring(4);
            }
            if (!all) {
                bandType = bandoriTypes.getBandType(message);
                chosen = shuffleAllSongs(bandoriSongs, bandType, songType, random);
            }
            else {
                chosen = shuffleAllSongs(bandoriSongs, null, songType, random);
            }
            if (chosen.isEmpty()) {
                textChannel.sendMessage("No songs under that criteria were found!").queue();
                return;
            }
            if (!new JoinCommand().Join(messageReceivedEvent, musicManager)) {
                return;
            }
            queueAllSongs(textChannel, member, chosen, musicManager, random);
            sendEmbedMessage(textChannel, chosen, bandType, songType, random, all);

        }
        catch (NullPointerException f) {
        }
        catch (IndexOutOfBoundsException e) {
            textChannel.sendMessage("**$bshuffle <all** *or* **bandname> -type <song type>** - shuffle songs by a certain band or shuffle all songs alphabetically into the queue \n" +
                    "**include <all> to shuffle all songs** *or* **include a band name to shuffle all songs by that band**\n" +
                    "**include -type <song type> to specify the type of song to shuffle.** Defaults to either the game version or the first available version of the song. Don't include \"-type\" you don't want to specify song type\n" +
                    "**add -r at the end to randomize order**").queue();
        }
    }
    private ArrayList<BandoriSong> shuffleAllSongs(BandoriSongs bandoriSongs, BandType bandType, SongType songType, boolean random) {
        Hashtable <String, BandoriCollection> hashtable = bandoriSongs.getHashTable();
        ArrayList<String> keys = (ArrayList<String>) bandoriSongs.getKeys().clone();
        if (bandType != null) {
            return loadByBand(hashtable, keys, bandType, songType);
        }
        else {
            return loadNormally(hashtable, keys, songType);
        }
    }
    private void queueAllSongs(TextChannel textChannel, Member member, ArrayList<BandoriSong> list, MusicManager musicManager, boolean random) {
        BandoriSongLoadCommand command = new BandoriSongLoadCommand();
        Random r = new Random();
        r.setSeed(System.currentTimeMillis());
        int size = list.size();
        int temp, value;
        if (random) {
            int[] randomValues = new int[list.size()];
            for (int i = 0; i < size; i++) {
                randomValues[i] = i;
            }
            for (int i = 0; i < size; i++) {
                value = r.nextInt(size);
                temp = randomValues[i];
                randomValues[i] = randomValues[value];
                randomValues[value] = temp;
            }
            for (int i: randomValues) {
                System.out.println(i);
            }
            for (int i: randomValues)
                command.loadBandoriSong(textChannel, list.get(i), member, musicManager);
        }
        else {
            for (BandoriSong song : list) {
                command.loadBandoriSong(textChannel, song, member, musicManager);
            }
        }
    }
    private ArrayList<BandoriSong> loadNormally(Hashtable<String, BandoriCollection> hashtable, ArrayList<String> keys, SongType songType) {
        BandoriCollection collection;
        ArrayList<BandoriSong> list = new ArrayList<>();
        System.out.println("load normally");
        for (String key: keys) {
            collection = hashtable.get(key);
            System.out.println(key + " " + collection.getName());
            if (songType != null) {
                list.add(collection.searchByType(songType));
            }
            else {
                list.add(collection.getFirstSong());
            }
        }
        return list;
    }
    private ArrayList<BandoriSong> loadByBand(Hashtable<String, BandoriCollection> hashtable, ArrayList<String> keys, BandType bandType, SongType songType) {
        BandoriTypes bandoriTypes = new BandoriTypes();
        BandoriCollection collection;
        ArrayList<BandoriSong> list = new ArrayList<>();
        System.out.println("load by band");
        for (String key: keys) {
            collection = hashtable.get(key);
            System.out.println(key + " " + bandType + " " + collection.getBand());
            if (bandoriTypes.getBandString(bandType).contains(collection.getBand())) {
                if (songType != null) {
                    list.add(collection.searchByType(songType));
                }
                else {
                    list.add(collection.getFirstSong());
                }
            }
        }
        return list;
    }
    private void sendEmbedMessage(TextChannel textChannel, ArrayList<BandoriSong> list, BandType bandType, SongType songType, boolean random, boolean all) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        BandoriTypes bandoriTypes = new BandoriTypes();
        int i = 0;
        String title = "Shuffled " + list.size();
        if (songType != null) {
            title = title.concat(bandoriTypes.getSongString(songType) + " songs");
        }
        else {
            title = title.concat(" songs");
        }
        if (bandType != null) {
            title = title.concat(" by ").concat(bandoriTypes.getBandString(bandType));
        }
        else if (random) {
            title = title.concat(" randomly");
        }
        embedBuilder.setTitle(title)
                .setDescription("Showing up to the first 10 songs added to the queue");
        while (i < 10 && i < list.size()) {
            embedBuilder.addField("", "[" + list.get(i).getName() + "](" + list.get(i++).getWiki() + ")", false);
        }
        MessageBuilder messageBuilder = new MessageBuilder().setEmbed(embedBuilder.build());
        textChannel.sendMessage(messageBuilder.build()).queue();
    }
}
