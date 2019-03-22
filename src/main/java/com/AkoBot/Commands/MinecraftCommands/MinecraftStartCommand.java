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

    public MinecraftServer startMineCraft(MessageReceivedEvent messageReceivedEvent, MinecraftServer minecraftServer) {
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        Member member = messageReceivedEvent.getMember();

        String os = System.getProperty("os.name").toLowerCase();

        try {
            if (minecraftServer.isRunning()) {
                textChannel.sendMessage("A server is already running!").queue();
                return minecraftServer;
            }

            String servername = messageReceivedEvent.getMessage().getContentRaw();
            servername = servername.substring(servername.indexOf(" ") + 1);

            String batchFile = "run.bat";
            ProcessBuilder pb = new ProcessBuilder();
            File dir;
            if (os.contains("win")) {
                pb.command("cmd", "/c", batchFile);
                dir = new File("C:\\docs\\minecraft_servers\\" + servername);
            }
            else {
                batchFile = "./run.bat";
                pb.command("sh", "-c", batchFile);
                dir = new File("/home/pi/minecraft_servers/" + servername);
            }
            pb.directory(dir);
            pb.redirectErrorStream(true);
            Process minecraft = pb.start();
            MinecraftServer newServer = new MinecraftServer();
            newServer.setOutputStream(minecraft.getOutputStream());
            newServer.setProcess(minecraft);
            newServer.setServername(servername);
            textChannel.sendMessage(member.getEffectiveName() + " the " + servername + " server has successfully been started!").queue();

            return newServer;

        }
        catch (StringIndexOutOfBoundsException e){
            textChannel.sendMessage("Please include server name like so:\n$startminecraft <server name>\ntry $infominecraft to see server names").queue();
            return minecraftServer;
        } catch (IOException f) {
            logger.debug("Minecraft couldn't start", f);
            textChannel.sendMessage("Please include server name like so:\n$startminecraft <server name>\ntry $infominecraft to see server names").queue();
            return minecraftServer;
        }
    }
}
