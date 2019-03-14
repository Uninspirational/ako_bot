package com.AkoBot.Commands.HelpCommands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class Help3Command {
    public void Help3(MessageReceivedEvent messageReceivedEvent) {
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        User user = messageReceivedEvent.getAuthor();
        EmbedBuilder embedBuilder =
                new EmbedBuilder()
                        .setTitle("List of Card Search Commands")
                        .setColor(new Color(0xBA00BA))
                        .addField("BanG Dream! Girls Band Party Database Commands",
                                        "**$card <category> <search term>** - search for a card by category that includes the search term. See below for acceptable categorys\n" +
                                        "**$member <category> <search term>** - search for a band member by category that includes the search term. See below for acceptable categorys\n",
                                false)
                        .addField("Card Search Categories",
                                "include -t at the end to get the trained version of the card" +
                                        "**name** - card name\n" +
                                        "**more coming soon**", true)
                        .addField("Member Search Categories",
                                "**name** - character name\n" +
                                        "**id** - member id (between 6 and 30, inclusive)\n" +
                                        "**jpname** - character name in Japanese\n" +
                                        "**band** - band the character is in\n" +
                                        "**school** - school the character attends\n" +
                                        "**year** - year in school (first/second/third)\n" +
                                        "**birthday | bday** - birthday in YYYY-MM-DD format\n" +
                                        "**rmvc | usvoiceactor | envoiceactor | usva | enva** - character's voice actor in romaji\n" +
                                        "**vc | jpvoiceactor** - character's voice actor in Japanese\n" +
                                        "**foodlike | fl** - character's favorite foods\n" +
                                        "**fooddislike | fd | foodhate** - character's least favorite foods\n" +
                                        "**sign** - character's astrological sign\n" +
                                        "**instrument** - character's instrument she plays in the band", false);
        MessageBuilder messageBuilder = new MessageBuilder()
                .setEmbed(embedBuilder.build());
        user.openPrivateChannel().queue((channel) ->
                channel.sendMessage(messageBuilder.build()).queue());
        textChannel.sendMessage("Help message sent!").queue();
    }
}
