package com.AkoBot.Commands.HelpCommands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class Help2Command {
    public void Help2(MessageReceivedEvent messageReceivedEvent) {
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        User user = messageReceivedEvent.getAuthor();
        EmbedBuilder embedBuilder =
                new EmbedBuilder()
                        .setTitle("List of Wiki Commands")
                        .setColor(new Color(0xBA00BA))

                        .addField("BanG Dream! Girls Band Party Music Commands",
                                "**$bandoriRefresh** - debug feature rescrape all songs from the wiki *note no need to use normally*\n" +
                                        "**$bplay <search term>** - find and play songs by a band or by song name\n" +
                                        "**$bshuffle <band name> *or* <all>** - shuffle songs by a certain band or shuffle all songs alphabetically into the queue **add -r at the end to randomize order**\n\n" +
                                        "**$bsearch <song name>** - search for a song name, does not play the song\n",
                                false);
        MessageBuilder messageBuilder = new MessageBuilder()
                .setEmbed(embedBuilder.build());
        user.openPrivateChannel().queue((channel) ->
                channel.sendMessage(messageBuilder.build()).queue());
        textChannel.sendMessage("Help message sent!").queue();
    }
}
