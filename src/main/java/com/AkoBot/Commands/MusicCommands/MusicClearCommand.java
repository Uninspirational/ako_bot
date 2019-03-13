package com.AkoBot.Commands.MusicCommands;

import com.AkoBot.Music.MusicController;
import com.AkoBot.Music.MusicManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MusicClearCommand {
    public void Clear(MessageReceivedEvent messageReceivedEvent, MusicManager musicManager) {
        Guild guild = messageReceivedEvent.getGuild();
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        MusicController musicController = musicManager.getPlayer(guild);
        if (musicController.getListener().getTracks().isEmpty()) {
            textChannel.sendMessage("Huh? We haven't even started yet! (Queue is empty)").queue();
            return;
        }
        musicController.getListener().getTracks().clear();
        textChannel.sendMessage("Oh, is our studio time up already?.").queue();
    }
}
