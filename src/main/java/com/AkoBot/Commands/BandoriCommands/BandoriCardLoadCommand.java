package com.AkoBot.Commands.BandoriCommands;

import com.AkoBot.Bandori.BandoriCards;
import com.AkoBot.Bandori.BandoriMembers;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class BandoriCardLoadCommand {
    public BandoriCards bandoriCards(MessageReceivedEvent messageReceivedEvent, BandoriMembers bandoriMembers) {
        BandoriCards bandoriCards = new BandoriCards();
        bandoriCards.bandoriRefresh(messageReceivedEvent, bandoriMembers);
        return bandoriCards;
    }
}
