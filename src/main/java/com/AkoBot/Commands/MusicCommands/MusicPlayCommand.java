package com.AkoBot.Commands.MusicCommands;

import com.AkoBot.Commands.JoinCommand;
import com.AkoBot.Music.MusicManager;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MusicPlayCommand {
    public void Play(MessageReceivedEvent messageReceivedEvent, MusicManager musicManager) {
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        String message = messageReceivedEvent.getMessage().getContentRaw();
        Member member = messageReceivedEvent.getMember();

        try {
            if (new JoinCommand().Join(messageReceivedEvent, musicManager)) {
                if (messageReceivedEvent.getMember().getVoiceState().inVoiceChannel()) {
                    message = message.substring(message.indexOf(" ") + 1);
                    musicManager.loadTrack(textChannel, message, member, null);
                }
            }
        }
        catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            textChannel.sendMessage("A performance? Great! We’ll play whatever you request. (Include link or search)").queue();
        }

    }
}
