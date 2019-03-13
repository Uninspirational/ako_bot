package com.AkoBot.Commands.BandoriCommands;

import com.AkoBot.Bandori.BandoriMembers;
import com.AkoBot.Logger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class BandoriMemberCommand {
    public void member(MessageReceivedEvent messageReceivedEvent, BandoriMembers bandoriMembers) {
        try {
            bandoriMembers.controller(messageReceivedEvent);
        }
        catch (NullPointerException e) {
            new Logger().logError(e);
        }
    }
}
