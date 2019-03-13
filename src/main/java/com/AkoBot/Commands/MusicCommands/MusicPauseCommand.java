package com.AkoBot.Commands.MusicCommands;

import com.AkoBot.Music.MusicManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MusicPauseCommand {
    public void Pause(MessageReceivedEvent messageReceivedEvent, MusicManager musicManager) {
        Guild guild = messageReceivedEvent.getGuild();
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        com.sedmelluq.discord.lavaplayer.player.AudioPlayer audioPlayer = musicManager.getPlayer(guild).getAudioPlayer();
        if (!audioPlayer.isPaused()) {
            audioPlayer.setPaused(true);
            textChannel.sendMessage("Demons arise and obey my command. Freeze time! Dark summoners are so cool!").queue();
        }
        else {
            textChannel.sendMessage("But I'm already frozen!").queue();
        }
    }
}
