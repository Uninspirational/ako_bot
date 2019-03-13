package com.AkoBot.Commands.BandoriCommands;

import com.AkoBot.Bandori.BandoriCards;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class BandoriCardLoadCommand {
    public BandoriCards bandoriCards(MessageReceivedEvent messageReceivedEvent) {
        BandoriCards bandoriCards = new BandoriCards();
        bandoriCards.bandoriRefresh(messageReceivedEvent);
        return bandoriCards;
    }
}
