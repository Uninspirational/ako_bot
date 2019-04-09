package com.AkoBot.Bandori;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class BandoriTeam {
    private ArrayList<BandoriCard> team;
    public BandoriTeam() {
        this.team = new ArrayList<>();
    }
    public void addCard(MessageReceivedEvent messageReceivedEvent, BandoriCards bandoriCards, EventWaiter waiter) {
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        String currentTeam = "";
        for (int i = 0; i < team.size(); i++) {
            currentTeam = "[" + i + "]" + " - " + currentTeam + "\n";
        }
        currentTeam = "Which member would you like to replace? Type the number or type $cancel";
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.addField(currentTeam, "", false);
        textChannel.sendMessage(embedBuilder.build()).queue();
        waitForOldCard(messageReceivedEvent.getAuthor(), textChannel, waiter, bandoriCards);
    }
    private void waitForOldCard(User user, TextChannel channel, EventWaiter waiter, BandoriCards bandoriCards) {
        waiter.waitForEvent(GuildMessageReceivedEvent.class, e -> e.getChannel().equals(channel) && e.getAuthor().equals(user), e -> {
            TextChannel textChannel = e.getChannel();
            String message = e.getMessage().getContentStripped().toLowerCase();
            if (message.equals("$cancel")) {
                textChannel.sendMessage("Cancelled").queue();
                return;
            }
            try
            {
                int oldCard = Integer.parseInt(message);
                waitForNewCard(user, textChannel, waiter, oldCard, bandoriCards);
            }
            catch (Exception f) {
                textChannel.sendMessage("Please enter the corresponding number for the card you would like to replace").queue();
                this.waitForOldCard(user, channel, waiter, bandoriCards);
            }
        }, 25, TimeUnit.SECONDS, () -> channel.sendMessage("Timed out").queue());

    }
    private void waitForNewCard(User user, TextChannel channel, EventWaiter waiter, final int oldCard, BandoriCards bandoriCards) {
        waiter.waitForEvent(GuildMessageReceivedEvent.class, e -> e.getChannel().equals(channel) && e.getAuthor().equals(user), e -> {
            TextChannel textChannel = e.getChannel();
            String message = e.getMessage().getContentStripped().toLowerCase();
            if (message.equals("$cancel")) {
                textChannel.sendMessage("Cancelled").queue();
                return;
            }
            try
            {
                int newCard = Integer.parseInt(message);
                replaceCard(textChannel, newCard, oldCard, bandoriCards);
            }
            catch (Exception f) {
                textChannel.sendMessage("Please enter the corresponding number for the new card you would like to add").queue();
                this.waitForNewCard(user, channel, waiter, oldCard, bandoriCards);
            }
        }, 25, TimeUnit.SECONDS, () -> channel.sendMessage("Timed out").queue());
    }
    private void replaceCard(TextChannel textChannel, int newCard, int oldCard, BandoriCards bandoriCards) {
        BandoriCard[] cardsArray = (BandoriCard[]) this.team.toArray();
        cardsArray[oldCard] = bandoriCards.getBandoriCards().get(newCard);
        this.team.clear();
        this.team.addAll(Arrays.asList(cardsArray));
    }
}
