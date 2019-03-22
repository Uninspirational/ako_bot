package com.AkoBot.Commands.MinecraftCommands;

import com.AkoBot.Commands.MinecraftServer;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class MinecraftEndCommand {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(MinecraftEndCommand.class);

    public MinecraftServer EndMinecraft(MessageReceivedEvent messageReceivedEvent, MinecraftServer minecraftServer) {
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        Member member = messageReceivedEvent.getMember();
        if (!minecraftServer.isRunning()) {
            textChannel.sendMessage("No server is currently running!").queue();
            return minecraftServer;
        }
        else {
            if (minecraftServer.closeMinecraftServer()) {
                textChannel.sendMessage(member.getEffectiveName() + ", you have successfully ended Minecraft").queue();
                logger.debug("Closed Minecraft server");
                return minecraftServer;
            }
            else {
                textChannel.sendMessage(member.getEffectiveName() + ", could not end Minecraft\n<@137710437490884608>").queue();
                logger.debug("Failed to close minecraft server");
                return minecraftServer;
            }
        }
    }
}
