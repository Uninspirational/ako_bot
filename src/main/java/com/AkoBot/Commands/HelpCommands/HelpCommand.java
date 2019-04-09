package com.AkoBot.Commands.HelpCommands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class HelpCommand {
    public void Help(MessageReceivedEvent messageReceivedEvent) {
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        User user = messageReceivedEvent.getAuthor();
// {"$help", "$play", "$join", "$leave", "$pause", "$resume", "$skip", "$clear", "$queue", "$np", "$bandoriRefresh", "$bplay", "$bshuffle", "$bsearch", "$bandoriCards", "$card", "$bandoriMembers", "$member"};

        EmbedBuilder embedBuilder =
                new EmbedBuilder()
                        .setTitle("List of help pages")
                        .setColor(new Color(0xBA00BA))
                        .addField("$help", "Don't include the <> when using a command", false)
                        .addField("$help1","Display the music commands", false)
                        .addField("$help2",
                                "Display the WIKI commands", false)
                        .addField("$help3","Display the card and member search commands", false)
                        .addField("$help$","Display gacha commands", false);
//						.addField("Music Commands",
//								"**$play <video link> *or* <youtube search term>** - will join and play audio\n" +
//										"**$join** - join the voice channel\n" +
//										"**$leave** - clear the queue and leave the voice channel\n" +
//										"**$pause** - pause playback\n" +
//										"**$resume** - resume playback\n" +
//										"**$skip** - play the next song\n" +
//										"**$clear** - empty the queue *note does not skip the current song*\n" +
//										"**$queue** - display the queue *note duration does not work for BanG Dream songs*\n" +
//										"**$np** - display the current playing song *note duration does not work for BanG Dream songs*\n",
//								false)
//						.addField("BanG Dream! Girls Band Party Music Commands",
//								"**$bangM** - debug feature rescrape all songs from the wiki *note no need to use normally*\n" +
//										"**$bplay <search term>** - find and play songs by a band or by song name\n" +
//										"**$bshuffle <band name> *or* <all>** - shuffle songs by a certain band or shuffle all songs randomly into the queue **add -a at the end to alphabetize instead**\n" +
//										"**$bsearch <search term>** - search for a song by band or name but does not play the song\n",
//								false)
//						.addField("BanG Dream! Girls Band Party Database Commands",
//								"**bangC** - debug feature rescrape all cards from the BanG Dream API *note no need to use normally*\n" +
//										"**$card <category> <search term>** - search for a card by category that includes the search term. See below for acceptable categorys\n" +
//										"**bangMB** - debug feature rescrape all members from the wiki *note no need to ever use this*\n" +
//										"**$member <category <search term>** - search for a band member by category that includes the search term. See below for acceptable categorys\n",
//								false)
//						.addField("Card Search Categories",
//								"include -t at the end to get the trained version of the card" +
//										"**name** - card name\n" +
//										"**more coming soon**", true)
//						.addField("Member Search Categories",
//								"**name** - character name\n" +
//										"**id** - member id (between 0 and 24)\n" +
//										"**jpname** - character name in Japanese\n" +
//										"**band** - band the character is in\n" +
//										"**school** - school the character attends\n" +
//										"**year** - year in school (first/second/third)\n" +
//										"**birthday | bday** - birthday in YYYY-MM-DD format\n" +
//										"**rmvc | usvoiceactor | envoiceactor | usva | enva** - character's voice actor in romaji\n" +
//										"**vc** - character's voice actor in Japanese\n" +
//										"**foodlike | fl** - character's favorite foods\n" +
//										"**fooddislike | fd | foodhate** - character's least favorite foods\n" +
//										"**sign** - character's astrological sign\n" +
//										"**instrument** - character's instrument she plays in the band", false);
        MessageBuilder messageBuilder = new MessageBuilder()
                .setEmbed(embedBuilder.build());
        user.openPrivateChannel().queue((channel) ->
                channel.sendMessage(messageBuilder.build()).queue());
        textChannel.sendMessage("Help message sent!").queue();
    }
}

