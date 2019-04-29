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
    int votes1 = 0;
    int votes2 = 0;
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

        profile1.shuffleTeam();
        profile2.shuffleTeam();


        //send game start prompts
        embedBuilder.clear();
        embedBuilder.addField("Your cards:", profile1.getTeam().getGameString(), false)
                .addField("Please enter the card you would like to use in the channel the challenge was sent!", "", false);
        member1.getUser().openPrivateChannel().queue((channel) ->
                channel.sendMessage(embedBuilder.build()).queue());

        embedBuilder.clear();
        embedBuilder.addField("Your cards:", profile2.getTeam().getGameString(), false)
                .addField("Please enter the card you would like to use in the channel the challenge was sent!", "", false);
        member2.getUser().openPrivateChannel().queue((channel) ->
                channel.sendMessage(embedBuilder.build()).queue());
        BandoriGameTurn turn = new BandoriGameTurn(profile1.getTeam(), profile2.getTeam());

        //turn 1
        recursivePrompt1(textChannel, profile1, profile2, member1, member2, waiter, turn);
        printResults(textChannel, profile1, profile2, turn);

        //turn 2
        recursivePrompt1(textChannel, profile1, profile2, member1, member2, waiter, turn);
        printResults(textChannel, profile1, profile2, turn);

        //turn 3
        recursivePrompt1(textChannel, profile1, profile2, member1, member2, waiter, turn);
        printResults(textChannel, profile1, profile2, turn);

        //turn 4
        recursivePrompt1(textChannel, profile1, profile2, member1, member2, waiter, turn);
        printResults(textChannel, profile1, profile2, turn);

        //turn 5
        recursivePrompt1(textChannel, profile1, profile2, member1, member2, waiter, turn);
        printResults(textChannel, profile1, profile2, turn);

        printFinalResults(textChannel, profile1, profile2, turn);
    }
    private void recursivePrompt1(TextChannel textChannel, Profile profile1, Profile profile2, Member member1, Member member2, EventWaiter waiter, BandoriGameTurn turn) {
        textChannel.sendMessage(profile1.getMention() + " please enter the number of the card you would like to use!").queue();
        EmbedBuilder embedBuilder = new EmbedBuilder().addField("Your cards:", profile1.getTeam().getGameString(), false)
                .addField("Please enter the card you would like to use in the channel the challenge was sent!", "", false);
        member1.getUser().openPrivateChannel().queue((channel) ->
                channel.sendMessage(embedBuilder.build()).queue());
        waiter.waitForEvent(GuildMessageReceivedEvent.class, e -> e.getAuthor().getId().equals(profile1.getUserId()) && e.getChannel().equals(textChannel), e -> {
                    try{
                        int number1 = Integer.parseInt(e.getMessage().getContentStripped());
                        recursivePrompt2(textChannel, profile2, member2, waiter, turn, number1);
                    }
                    catch (NumberFormatException h) {
                        textChannel.sendMessage("Please enter a valid number!").queue();
                        recursivePrompt1(textChannel, profile1, profile2, member1, member2, waiter, turn);
                    }

                }, 120, TimeUnit.SECONDS, () ->
                        textChannel.sendMessage("Battle timed out").queue()
        );


    }
    private void recursivePrompt2(TextChannel textChannel, Profile profile2, Member member2, EventWaiter waiter, BandoriGameTurn turn, int number1) {
        textChannel.sendMessage(profile2.getMention() + " please enter the number of the card you would like to use!").queue();
        EmbedBuilder embedBuilder = new EmbedBuilder().addField("Your cards:", profile2.getTeam().getGameString(), false)
                .addField("Please enter the card you would like to use in the channel the challenge was sent!", "", false);
        member2.getUser().openPrivateChannel().queue((channel) ->
                channel.sendMessage(embedBuilder.build()).queue());
        waiter.waitForEvent(GuildMessageReceivedEvent.class, g -> g.getAuthor().getId().equals(profile2.getUserId()) && g.getChannel().equals(textChannel), g -> {
                    try{
                        int number2 = Integer.parseInt(g.getMessage().getContentStripped());
                        turn.runTurn(number1, number2);
                    }
                    catch (NumberFormatException f) {
                        textChannel.sendMessage("Please enter a valid number!").queue();
                        recursivePrompt2(textChannel, profile2, member2, waiter, turn, number1);
                    }

                }, 120, TimeUnit.SECONDS, () ->
                        textChannel.sendMessage("Battle timed out").queue()
        );
    }

    private void printResults(TextChannel textChannel, Profile profile1, Profile profile2, BandoriGameTurn turn) {
        String result = "";
        votes1 += turn.getVotes1();
        votes2 += turn.getVotes2();
        if (turn.getWinner() == 1) {
            result += profile1.getMention() + " has won this round!\n";
            result += "You have a total of " + votes1 + " so far\n";
            result += profile2.getMention() + " has lost this round!\n";
            result += "You have a total of " + votes2 + " so far\n";
        }
        else {
            result += profile2.getMention() + " has won this round!\n";
            result += "You have a total of " + votes2 + " so far\n";
            result += profile1.getMention() + " has lost this round!\n";
            result += "You have a total of " + votes1 + " so far\n";
        }
        textChannel.sendMessage(result).queue();
    }
    private void printFinalResults(TextChannel textChannel, Profile profile1, Profile profile2, BandoriGameTurn turn) {
        String result = "";
        result += "The final tallies are:\n" + profile1.getMention() + ": " + this.votes1 + " votes\n";
        result += profile2.getMention() + ": " + this.votes2 + " votes\n";
        if (votes1 > votes2) {
            result += profile1.getMention() + " is the winner!";
        }
        else {
            result += profile2.getMention() + " is the winner!";
        }
        textChannel.sendMessage(result).queue();
    }
    private boolean checkTeam(Member member, BandoriCards bandoriCards) {
        if (this.profiles.getProfile(member.getUser().getId(), bandoriCards).getTeam() == null)
            return false;
        return this.profiles.getProfile(member.getUser().getId(), bandoriCards).getTeam().isFull();
    }
}