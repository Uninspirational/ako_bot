package com.AkoBot.Commands.MusicCommands;

import com.AkoBot.Music.MusicController;
import com.AkoBot.Music.MusicManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MusicLeaveCommand {
    private final Logger logger = LoggerFactory.getLogger(MusicLeaveCommand.class);
    public void Leave(MessageReceivedEvent messageReceivedEvent, MusicManager musicManager) {
        Guild guild = messageReceivedEvent.getGuild();
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        Member member = messageReceivedEvent.getMember();
        if (member.getVoiceState().inVoiceChannel()) {
            MusicController musicController = musicManager.getPlayer(guild);
            musicController.getListener().getTracks().clear();
            try {
                musicManager.getPlayer(guild).skipTrack();
            }
            catch (NullPointerException e) {
                logger.debug("Couldn't leave voice channel: " + member.getVoiceState().getChannel() + " in guild: " + guild.getId());
            }
            musicManager.getPlayer(guild).getAudioPlayer().destroy();
            //musicManager.removePlayer(guild);
            guild.getAudioManager().closeAudioConnection();
            textChannel.sendMessage("That gig was so much fun! See you later!").queue();
        }
    }

}
