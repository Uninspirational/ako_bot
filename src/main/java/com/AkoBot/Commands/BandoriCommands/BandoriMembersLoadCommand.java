package com.AkoBot.Commands.BandoriCommands;

import com.AkoBot.Bandori.BandoriMembers;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class BandoriMembersLoadCommand {
    public BandoriMembers bandoriMembers(MessageReceivedEvent messageReceivedEvent) {
        BandoriMembers bandoriMembers = new BandoriMembers();
        bandoriMembers.bandoriRefresh(messageReceivedEvent);
        return bandoriMembers;
    }
}
