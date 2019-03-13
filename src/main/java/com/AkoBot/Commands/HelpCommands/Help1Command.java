package com.AkoBot.Commands.HelpCommands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class Help1Command {
    public void Help1(MessageReceivedEvent messageReceivedEvent) {
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        User user = messageReceivedEvent.getAuthor();
        EmbedBuilder embedBuilder =
                new EmbedBuilder()
                        .setTitle("List of Music Commands")
                        .setColor(new Color(0xBA00BA))
                        .addField("Music Commands",
                                "**$play <video link> *or* <youtube search term>** - will join and play audio\n" +
                                        "**$join** - join the voice channel\n" +
                                        "**$leave** - clear the queue and leave the voice channel\n" +
                                        "**$pause** - pause playback\n" +
                                        "**$resume** - resume playback\n" +
                                        "**$skip** - play the next song\n" +
                                        "**$clear** - empty the queue *note does not skip the current song*\n" +
                                        "**$queue** - display the queue *note duration does not work for BanG Dream songs*\n" +
                                        "**$np** - display the current playing song *note - duration does not work for BanG Dream songs*\n",
                                false);
        MessageBuilder messageBuilder = new MessageBuilder()
                .setEmbed(embedBuilder.build());
        user.openPrivateChannel().queue((channel) ->
                channel.sendMessage(messageBuilder.build()).queue());
        textChannel.sendMessage("Help message sent!").queue();
    }
}
