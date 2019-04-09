package com.AkoBot.Commands.HelpCommands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class Help4Command {
    public void Help4(MessageReceivedEvent messageReceivedEvent) {
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        User user = messageReceivedEvent.getAuthor();
        EmbedBuilder embedBuilder =
                new EmbedBuilder()
                        .setTitle("List of Music Commands")
                        .setColor(new Color(0xBA00BA))
                        .addField("Gacha Commands",
                                "**$gacha** - roll for gacha, will list the card ids\n",
                                false);
        MessageBuilder messageBuilder = new MessageBuilder()
                .setEmbed(embedBuilder.build());
        user.openPrivateChannel().queue((channel) ->
                channel.sendMessage(messageBuilder.build()).queue());
        textChannel.sendMessage("Help message sent!").queue();
    }
}
