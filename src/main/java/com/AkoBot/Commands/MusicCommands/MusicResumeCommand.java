package com.AkoBot.Commands.MusicCommands;

import com.AkoBot.Music.MusicManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MusicResumeCommand {
    public void Resume(MessageReceivedEvent messageReceivedEvent, MusicManager musicManager) {
        Guild guild = messageReceivedEvent.getGuild();
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        com.sedmelluq.discord.lavaplayer.player.AudioPlayer audioPlayer = musicManager.getPlayer(guild).getAudioPlayer();
        if (audioPlayer.isPaused()) {
            audioPlayer.setPaused(false);
            textChannel.sendMessage("Let the frozen time...um...thaw and flow once more! Da ga da ga da ga da ga Don!").queue();
        }
        else {
            textChannel.sendMessage("But I'm already playing!").queue();
        }
    }
}
