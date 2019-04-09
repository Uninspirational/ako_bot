package com.AkoBot.Bandori;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class BandoriTeam {
    ArrayList<BandoriCard> team;
    public BandoriTeam() {
        this.team = new ArrayList<>();
    }
    public void addCard(MessageReceivedEvent messageReceivedEvent, BandoriCard card, EventWaiter waiter) {
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        String currentTeam = "";
        for (int i = 0; i < team.size(); i++) {
            currentTeam = "[" + i + "]" + " - " + currentTeam + "\n";
        }
        currentTeam = "Which member would you like to replace? Type the number or type $cancel";
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.addField(currentTeam, "", false);
        textChannel.sendMessage(embedBuilder.build()).queue();
        waitForAddCard(messageReceivedEvent.getAuthor(), textChannel, waiter);
    }
    private void waitForAddCard(User user, TextChannel channel, EventWaiter waiter) {
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

            }
            catch (Exception f) {
                textChannel.sendMessage("Please enter the corresponding number for the card you would like to replace").queue();
                this.waitForAddCard(user, channel, waiter);
            }
        }, 25, TimeUnit.SECONDS, () -> channel.sendMessage("Timed out").queue());

    }
}
