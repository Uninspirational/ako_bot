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
                                        "**$bplay <-all> <-type> <search term>** - find and play songs by a band or by song name\n" +
                                        "may include <-all> to play all songs that match search term\n" +
                                        "may include <-type> to specify type of song to play, replace \"type\" with a song type. See $help4 for more\n" +
                                        "**$bshuffle <all** *or* **bandname> -type <song type>** - alphabetically shuffle songs by a certain band or shuffle all songs into the queue\n" +
                                        "include <all> to shuffle all songs *or* include a band name to shuffle all songs by that band\n" +
                                        "include -type <song type> to specify the type of song to shuffle. Defaults to either the game version or the first available version of the song. Don't include \"-type\" you don't want to specify song type\n" +
                                        "add -r at the end to randomize order\n" +
                                        "**$bsearch <song name>** - search for a song by name, does not play the song\n",
                                false);
        MessageBuilder messageBuilder = new MessageBuilder()
                .setEmbed(embedBuilder.build());
        user.openPrivateChannel().queue((channel) ->
                channel.sendMessage(messageBuilder.build()).queue());
        textChannel.sendMessage("Help message sent!").queue();
    }
}
