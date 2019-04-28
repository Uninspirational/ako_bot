package com.AkoBot.Commands.BandoriGameCommands;

import com.AkoBot.Bandori.BandoriCard;
import com.AkoBot.Bandori.BandoriMember;
import com.AkoBot.Bandori.BandoriMembers;
import com.AkoBot.Commands.Profile;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.requests.RestAction;

import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class BandoriViewCards {
    public void viewCards(MessageReceivedEvent messageReceivedEvent, Profile profile, EventWaiter waiter, int start) {
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        final int size = profile.getCards().size();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        String message = "";
        ArrayList<BandoriCard> cards = profile.getCards();
        BandoriCard temp;
        int i;
        for (i = start; i < size && i < start + 10; i++) {
            temp = cards.get(i);
            message += "[" + (i + 1) + "] - " + temp.getI_attribute() + " | **" + temp.getName() + "** | Lvl: " + temp.getLevel() + temp.getBandoriMember().getName() + " | *(" + temp.getId() + ")*\n";
        }
        embedBuilder.addField("", message, false);
        embedBuilder.setFooter("Displaying " + (start + 1) + "-" + (i) + " out of " + size, null);
        RestAction<Message> restAction = textChannel.sendMessage(embedBuilder.build());
        Message send = restAction.complete();
        long messageId = send.getIdLong();
        if (i < size) {
            nextPage(textChannel, messageReceivedEvent.getAuthor(), profile, waiter, start + 10, messageId);
        }
    }
    public void viewCard(MessageReceivedEvent messageReceivedEvent, Profile profile, BandoriMembers bandoriMembers) {
        String message = messageReceivedEvent.getMessage().getContentStripped();
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        try {
            message = message.substring(message.indexOf(" ") + 1);
            int cardNum = Integer.parseInt(message) - 1;
            EmbedBuilder embedBuilder = profile.getCards().get(cardNum).getEmbedBuilder(bandoriMembers);
            textChannel.sendMessage(embedBuilder.build()).queue();
        }
        catch (IndexOutOfBoundsException e) {
            textChannel.sendMessage("Please include the card number as well!").queue();
        }
        catch (NumberFormatException f) {
            textChannel.sendMessage("Please use a numerical value!").queue();
        }
    }
    public void viewCards(MessageReceivedEvent messageReceivedEvent, Profile profile, EventWaiter waiter) {
        this.viewCards(messageReceivedEvent, profile, waiter, 0);
    }
    private void nextPage(TextChannel textChannel, User user, Profile profile, EventWaiter waiter, int start, long messageId) {
        waiter.waitForEvent(GuildMessageReceivedEvent.class, e -> (e.getMessage().getContentStripped().equals("$b") || e.getMessage().getContentStripped().equals("$n")) && e.getChannel().equals(textChannel) && e.getAuthor().equals(user), e -> {
            if (e.getMessage().getContentStripped().equals("$viewcards"))
                return;
            long delete = e.getMessageIdLong();
            textChannel.deleteMessageById(delete).queue();
            int move;
            if (e.getMessage().getContentStripped().equals("$n"))
                move = 10;
            else
                move = -10;
            if (!sendCardView(textChannel, start + move, profile, messageId))
                return;
            nextPage(textChannel, user, profile, waiter, start + move, messageId);
        }, 25, TimeUnit.SECONDS, null);
    }
    private boolean sendCardView(TextChannel textChannel, int start, Profile profile, long messageId) {
        final int size = profile.getCards().size();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        String message = "";
        ArrayList<BandoriCard> cards = profile.getCards();
        BandoriCard temp;
        int i;
        if (start < 0 || start + 10 > size) {
            textChannel.sendMessage("No more!").queue();
            return false;
        }
        else {
            for (i = start; i < size && i < start + 10; i++) {
                temp = cards.get(i);
                message += "[" + (i + 1) + "] - " + temp.getI_attribute() + " | **" + temp.getName() + "** | Lvl: " + temp.getLevel() + temp.getBandoriMember().getName() + " | *(" + temp.getId() + ")*\n";
            }
            embedBuilder.addField("", message, false);
            embedBuilder.setFooter("Displaying " + (start + 1) + "-" + (i) + " out of " + size, null);
            textChannel.editMessageById(messageId, embedBuilder.build()).queue();
        }
        return true;
    }
}
