package com.AkoBot.Commands.MinecraftCommands;

import com.AkoBot.Commands.MinecraftServer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class MinecraftInfoCommand {
    public void listServers(MessageReceivedEvent messageReceivedEvent, MinecraftServer minecraftServer) {
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Minecraft Server Information")
                .setThumbnail("https://gamepedia.cursecdn.com/minecraft_gamepedia/c/c7/Grass_Block.png?version=08c50447b4f0c1f9c1792a8b833e3b78")
                .setColor(new Color(0x5b8731))
                .setDescription("All servers have the port 25565, and only one can be open at a time\nUse $startminecraft <server name> to start a server");
        if (minecraftServer == null) {
            embedBuilder.addField("No server is currently running, ", "", false);
        }
        else if (minecraftServer.isRunning()) {
            embedBuilder.addField("This server is currently running: ", minecraftServer.getServername(), false);
        }
        else {
            embedBuilder.addField("No server is currently running", "", false);
        }
        embedBuilder
                .addField("vanilla_1", "first vanilla server", false)
                .addField("vanilla_2", "second vanilla server", false)
                .addField("pixelmon_1", "first pixelmon server", false);
        MessageBuilder messageBuilder = new MessageBuilder().setEmbed(embedBuilder.build());
        textChannel.sendMessage(messageBuilder.build()).queue();
    }
}
