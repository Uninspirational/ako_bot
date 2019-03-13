package com.AkoBot.Commands;

import net.dv8tion.jda.core.entities.TextChannel;

public class MessageSender {
    public MessageSender(TextChannel textChannel, String message)
    {
        String line;
        while (!message.equals("")) {
            if (message.length() >= 1950) {
                line = message.substring(0, 1950);
                textChannel.sendMessage(line).queue();
                message = message.substring(1950);
            }
            if (!message.equals("")) {
                textChannel.sendMessage(message).queue();
                message = "";
            }
        }
    }
}
