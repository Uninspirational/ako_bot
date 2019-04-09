package com.AkoBot.Commands.BandoriGameCommands;

import com.AkoBot.Bandori.BandoriCard;
import com.AkoBot.Commands.Profile;
import com.AkoBot.Commands.Profiles;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.concurrent.TimeUnit;

public class BandoriGameCommand {
    private Profiles profiles;
    public BandoriGameCommand(final MessageReceivedEvent messageReceivedEvent, EventWaiter waiter, Profiles profiles) {
        this.profiles = profiles;
        final TextChannel textChannel = messageReceivedEvent.getTextChannel();
        Profile profile1 = this.profiles.getProfile(messageReceivedEvent.getAuthor().getId());
        for (BandoriCard card : profile1.getTeam()) {
            if (card == null) {
                textChannel.sendMessage("Please choose your band first!").queue();
                return;
            }

        }
        textChannel.sendMessage("Who would you like to battle?").queue();
        waiter.waitForEvent(GuildMessageReceivedEvent.class, e -> e.getAuthor().equals(messageReceivedEvent.getAuthor()) && !e.getChannel().equals(messageReceivedEvent.getChannel()), e -> {
            try {
                Member member = e.getMessage().getMentionedMembers().get(0);
            }
            catch (IndexOutOfBoundsException f) {
                textChannel.sendMessage("Please mention a user").queue();
            }
            textChannel.sendMessage(e.getAuthor().getName()).queue();
            textChannel.sendMessage("Success!").queue();
        }, 25, TimeUnit.SECONDS, () ->
            messageReceivedEvent.getTextChannel().sendMessage("Timed out").queue()
        );
    }
    private void runGame(TextChannel textChannel, Member member1, Member member2) {
        Profile profile1 = profiles.getProfile(member1.getUser().getId());
        Profile profile2 = profiles.getProfile(member2.getUser().getId());
        for (BandoriCard card : profile2.getTeam()) {
            if (card == null) {
                textChannel.sendMessage("That user does not have a band!").queue();
                return;
            }
        }
    }
    private void checkSynergy(TextChannel textChannel, BandoriCard[] team) {
        //instrument, band, skills, type, 3 stats, trained
        //cool, happy, power, pure
        int[] attribute = new int[4];
        //Afterglow, HelloHappy, Pasupare, PPP, Ras, Roselia
        int[] band = new int[5];
        //bass, dj, drums, guitar, guitar & vocals, keyboard, vocal
        int[] instrument = new int[7];
        for (BandoriCard card : team) {
            attribute[card.getAttributeInt()]++;
            band[card.getBandoriMember().getId()]++;
            instrument[card.getBandoriMember().getInstrumentInt()]++;
        }

    }
}