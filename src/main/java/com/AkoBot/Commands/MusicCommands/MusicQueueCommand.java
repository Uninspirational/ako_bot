package com.AkoBot.Commands.MusicCommands;

import com.AkoBot.Music.MusicManager;
import com.AkoBot.Music.Song;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Queue;

public class MusicQueueCommand {
    public void Queue(MessageReceivedEvent messageReceivedEvent, MusicManager musicManager) {
        Guild guild = messageReceivedEvent.getGuild();
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        String title, field;
        int counter = 0;
        long length = 0;
        Queue<Song> tracks = musicManager.getPlayer(guild).getListener().getTracks();
        if (tracks.isEmpty()) {
            textChannel.sendMessage("Queue is empty!").queue();
            return;
        }
        Object[] queue = tracks.toArray();
        Song songGetter;

        if (queue[0] instanceof Song) {
            songGetter = (Song) queue[0];
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setDescription("This song is the coolest <3");
            while (counter < queue.length && counter < 10) {
                songGetter = (Song) queue[counter];
                counter++;
                if (songGetter.getBandoriSong() == null) {
                    title = "Requested by " + songGetter.getMember().getEffectiveName();
                    field = "[" + counter + "] - " + "[" + songGetter.getAudioTrack().getInfo().title + "](" + songGetter.getUrl() + ") uploaded by *" + songGetter.getAudioTrack().getInfo().author + "*";
                    embedBuilder.addField(title, field, false);
                }
                else {
                    title = "Requested by " + songGetter.getMember().getEffectiveName();
                    field = "[" + counter + "] - " + "[" + songGetter.getBandoriSong().getName() + "](" + songGetter.getBandoriSong().getWiki() + ") by *" + (songGetter.getBandoriSong().getBand().equals("") ? "Poppin'Party" : songGetter.getBandoriSong().getBand())+ "*";
                    embedBuilder.addField(title, field, false);
                }
            }
            counter = 0;
            while (counter < queue.length) {
                length += songGetter.getAudioTrack().getDuration();
                counter++;
            }
            embedBuilder.setColor(new java.awt.Color(0xBA00BA));
            embedBuilder.setFooter("Total length: " + millisecondConverter(length), null);
            embedBuilder.setThumbnail("https://vignette.wikia.nocookie.net/bandori/images/8/84/Ako_PICO_Icon.png/revision/latest?cb=20180715113757");
            textChannel.sendMessage(embedBuilder.build()).queue();
        }
    }
    private String millisecondConverter(long time) {
        long seconds = time / 1000;
        int minutes = (int) seconds 	/ 60;
        int hours = minutes / 60;
        int days = hours / 24;
        String result;
        result = String.format("%02d:%02d:%02d:%02d", days, hours % 24, minutes % 60, seconds % 60);
        if (days == 0) {
            result = String.format("%02d:%02d:%02d", hours % 24, minutes % 60, seconds % 60);
        }
        else if (hours == 0) {
            result = String.format("%02d:%02d", minutes % 60, seconds % 60);
        }
        return result;
    }
}
