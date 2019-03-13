package com.AkoBot.Commands;

import com.AkoBot.Music.AudioHandler;
import com.AkoBot.Music.MusicManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

public class JoinCommand {
    public boolean Join(MessageReceivedEvent messageReceivedEvent, MusicManager musicManager) {
        Guild guild = messageReceivedEvent.getGuild();
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        Member member = messageReceivedEvent.getMember();
        VoiceChannel voiceChannel = member.getVoiceState().getChannel();
        try {
            if (voiceChannel.getUserLimit() == voiceChannel.getMembers().size()) {
                textChannel.sendMessage("This venus is too crowded for me! (Voice channel is full)").queue();
                return false;
            }
            else if (messageReceivedEvent.getMember().getVoiceState().inVoiceChannel()) {
                AudioManager audioManager = guild.getAudioManager();
                AudioHandler ah = musicManager.getPlayer(guild).getAudioHandler();
                audioManager.setSendingHandler(ah);
                audioManager.openAudioConnection(voiceChannel);
                musicManager.getPlayer(guild).getAudioPlayer().setVolume(15);
                return true;
            }
        }
        catch (NullPointerException e) {
            textChannel.sendMessage("Hey Rin-Rin, what live house were we supposed to be at again? (Join a voice channel)").queue();
            return false;
        }
        return false;
    }
}
