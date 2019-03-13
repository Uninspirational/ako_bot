package com.AkoBot.Commands.MusicCommands;

import com.AkoBot.Bandori.BandoriSong;
import com.AkoBot.Commands.MillisecondConverter;
import com.AkoBot.Music.MusicManager;
import com.AkoBot.Music.Song;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MusicNowPlayingCommand {
    public void NowPlaying(MessageReceivedEvent messageReceivedEvent, MusicManager musicManager) {

        Guild guild = messageReceivedEvent.getGuild();
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        String title, author, url;
        MillisecondConverter millisecondConverter = new MillisecondConverter();
        long length, position;
        try {
            //AudioTrack audioTrack = musicManager.getPlayer(guild).getAudioPlayer().getPlayingTrack();
            Song np = musicManager.getPlayer(guild).getListener().currentlyPlaying();
            if (np.getBandoriSong() == null) {
                AudioTrack audioTrack = np.getAudioTrack();
                title = audioTrack.getInfo().title;
                author = audioTrack.getInfo().author;
                url = audioTrack.getInfo().uri;
                length = audioTrack.getInfo().length;
                position = audioTrack.getPosition();
            }
            else {
                BandoriSong bandoriSong = np.getBandoriSong();
                title = bandoriSong.getName();
                author = bandoriSong.getBand();
                url = bandoriSong.getWiki();
                position = np.getAudioTrack().getPosition();
                length = np.getAudioTrack().getInfo().length;

            }
            EmbedBuilder response = new EmbedBuilder();
            response.setTitle(title, url).setColor(new java.awt.Color(0xBA00BA));
            response.setDescription("*by " + author + "*").setAuthor("Now Singing <3 ~~~");
            response.setFooter(millisecondConverter.millisecondConverter(position) + " / " + millisecondConverter.millisecondConverter(length), null);
            textChannel.sendMessage(response.build()).queue();

        }
        catch (NullPointerException e) {
            textChannel.sendMessage("Sorry! We're still setting up, so please be patient! (Nothing is playing)").queue();
        }
    }
}
