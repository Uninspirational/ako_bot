package com.AkoBot.Commands.MinecraftCommands;

import com.AkoBot.Commands.MinecraftServer;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class MinecraftStartCommand {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(MinecraftStartCommand.class);

    public MinecraftServer startMineCraft(MessageReceivedEvent messageReceivedEvent) {
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        Member member = messageReceivedEvent.getMember();
        try {
            String servername = messageReceivedEvent.getMessage().getContentRaw();
            servername = servername.substring(servername.indexOf(" ") + 1);
            String batchFile = servername.concat(".bat");

            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", batchFile);
            File dir = new File("C:\\docs\\minecraft");
            pb.directory(dir);
            pb.redirectErrorStream(true);
            Process minecraft = pb.start();
            textChannel.sendMessage(member.getNickname() + " the minecraft server has successfully been started!").queue();
            MinecraftServer minecraftServer = new MinecraftServer();
            minecraftServer.setOutputStream(minecraft.getOutputStream());
            minecraftServer.setProcess(minecraft);
            minecraftServer.setServername(servername);
            return minecraftServer;

        }
        catch (StringIndexOutOfBoundsException e){
            textChannel.sendMessage("Please include server name like so:\n$startminecraft <server name>\ntry $infominecraft to see server names").queue();
            return null;
        } catch (IOException f) {
            logger.debug("Minecraft couldn't start", f);
            textChannel.sendMessage("Minecraft couldn't start").queue();
            return null;
        }
    }
}
