package com.AkoBot.Commands.BandoriCommands;

import com.AkoBot.Bandori.BandoriSongs;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class BandoriRefreshCommand {
    public BandoriSongs bandoriRefresh(MessageReceivedEvent messageReceivedEvent) {
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        textChannel.sendMessage("Practicing Bandori songs...this might take me a minute...\n...\nPlease don't break my concentration...").queue();
        BandoriSongs bandoriSongs = new BandoriSongs();
        bandoriSongs.BandoriRefresh();
        textChannel.sendMessage("Done!").queue();
        return bandoriSongs;
    }
}
