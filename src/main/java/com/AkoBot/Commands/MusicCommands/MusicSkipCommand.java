package com.AkoBot.Commands.MusicCommands;

import com.AkoBot.Music.MusicManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MusicSkipCommand {
    public void Skip(MessageReceivedEvent messageReceivedEvent, MusicManager musicManager) {
        Guild guild = messageReceivedEvent.getGuild();
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        try {
            musicManager.getPlayer(guild).skipTrack();
            textChannel.sendMessage("Alright, next one!").queue();
        }
        catch (NullPointerException e) {
            textChannel.sendMessage("Uh, was there anything else planned? (No songs in queue)").queue();
        }
    }
}
