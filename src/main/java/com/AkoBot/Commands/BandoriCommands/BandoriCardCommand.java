package com.AkoBot.Commands.BandoriCommands;

import com.AkoBot.Bandori.BandoriCards;
import com.AkoBot.Bandori.BandoriMembers;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BandoriCardCommand {
    private final Logger logger = LoggerFactory.getLogger(BandoriCardCommand.class);
    public void bandoriCardCommand(MessageReceivedEvent messageReceivedEvent, BandoriCards bandoriCards, BandoriMembers bandoriMembers) {
        try {
            bandoriCards.controller(messageReceivedEvent, bandoriMembers);
        }
        catch (NullPointerException e) {
            logger.debug("NullPointerException in BandoriCardCommand method");
        }
        catch (IndexOutOfBoundsException f) {
            logger.debug("IndexOutOfBoundsException in BandoriCardCommand method");
        }
    }
}
