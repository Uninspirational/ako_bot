package com.AkoBot.Commands.BandoriGameCommands;

import com.AkoBot.Bandori.BandoriCard;
import com.AkoBot.Bandori.BandoriCards;
import com.AkoBot.Commands.Profile;
import com.AkoBot.Commands.Profiles;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.concurrent.TimeUnit;

public class BandoriGameCommand {
    private Profiles profiles;
    public BandoriGameCommand(final MessageReceivedEvent messageReceivedEvent, EventWaiter waiter, Profiles profiles, BandoriCards bandoriCards) {
        this.profiles = profiles;
        final TextChannel textChannel = messageReceivedEvent.getTextChannel();
        //ask for opponent name
        textChannel.sendMessage("Who would you like to battle?").queue();
        waiter.waitForEvent(GuildMessageReceivedEvent.class, e -> e.getAuthor().equals(messageReceivedEvent.getAuthor()) && e.getChannel().equals(messageReceivedEvent.getChannel()), e -> {
                    try {
                        //get opponent
                        Member member2 = e.getMessage().getMentionedMembers().get(0);
                        textChannel.sendMessage(e.getMessage().getMentionedMembers().get(0).getEffectiveName() + " please type \"confirm\" to accept").queue();
                        waiter.waitForEvent(GuildMessageReceivedEvent.class, g -> member2.equals(g.getMember()) && g.getChannel().equals(textChannel), g -> {
                            //if opponent responded
                            Member member1 = messageReceivedEvent.getMember();
                            if (!g.getMessage().getContentStripped().equals("confirm")) {
                                textChannel.sendMessage("Opponent declined").queue();
                                return;
                            }
                            //check for full team
                            if (!checkTeam(member1, bandoriCards) || !checkTeam(member2, bandoriCards))
                                textChannel.sendMessage("Both players must have a full team!").queue();
                            //run game if both have teams
                            else runGame(textChannel, member1, member2, bandoriCards, waiter);
                        }, 60, TimeUnit.SECONDS, () ->
                                textChannel.sendMessage("Opponent did not respond in time").queue());

                    }
                    catch (IndexOutOfBoundsException f) {
                        textChannel.sendMessage("Please mention a user").queue();
                    }
                }, 25, TimeUnit.SECONDS, () ->
                        messageReceivedEvent.getTextChannel().sendMessage("Timed out").queue()
        );
    }
    private void runGame(TextChannel textChannel, Member member1, Member member2, BandoriCards bandoriCards, EventWaiter waiter) {
        Profile profile1 = profiles.getProfile(member1.getUser().getId(), bandoriCards);
        Profile profile2 = profiles.getProfile(member2.getUser().getId(), bandoriCards);
        String team1Synergy = profile1.getTeam().checkSynergy();
        String team2Synergy = profile2.getTeam().checkSynergy();
        //send embed message with synergy for both players
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.addField("", "**Bonuses for " + member1.getAsMention() + "**\n" + team1Synergy + "\n", false)
                .addField("", "**Bonuses for " + member2.getAsMention() + "**\n" + team2Synergy + "\n", false)
                .addField("Select a card!", "DM send to both players", false);
        textChannel.sendMessage(embedBuilder.build()).queue();
        //cases to check for synergy and use them

        //send game start prompts
        embedBuilder.clear();
        embedBuilder.addField("Your cards:", profile1.getTeam().getString(), false);
        member1.getUser().openPrivateChannel().queue((channel) ->
                channel.sendMessage(embedBuilder.build()).queue());

        embedBuilder.clear();
        embedBuilder.addField("Your cards:", profile2.getTeam().getString(), false);
        member2.getUser().openPrivateChannel().queue((channel) ->
                channel.sendMessage(embedBuilder.build()).queue());


        BandoriGameTurn turn = new BandoriGameTurn(profile1.getTeam(), profile2.getTeam());
        textChannel.sendMessage("end of code, still WIP").queue();

    }
    private boolean checkTeam(Member member, BandoriCards bandoriCards) {
        if (this.profiles.getProfile(member.getUser().getId(), bandoriCards).getTeam() == null)
            return false;
        return this.profiles.getProfile(member.getUser().getId(), bandoriCards).getTeam().isFull();
    }
}