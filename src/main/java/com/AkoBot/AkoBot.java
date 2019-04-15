package com.AkoBot;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.security.auth.login.LoginException;

public class AkoBot extends ListenerAdapter{
    //    private String resourcesDirectory = "./src/main/resources/";
    private CommandController commandReader= new CommandController();
    private static JDA akoBot;
    private final Logger logger = LoggerFactory.getLogger(AkoBot.class);
    private static EventWaiter waiter = new EventWaiter();

    /**
     * creat bot, connect, and add Event Listener
     * @param args args array
     * @throws InterruptedException threads for music player
     */
    public static void main(String[] args) throws LoginException, InterruptedException, IllegalArgumentException {
        System.setProperty("http.agent", "Chrome");
        akoBot = new JDABuilder(AccountType.BOT)
                .setToken(authkey.authTokenkey)         // The token of the account that is logging in.
                .addEventListener(new AkoBot())
                .addEventListener(waiter)
                .setAudioEnabled(true)
                .setGame(Game.playing("$help"))
                .build();
        akoBot.awaitReady();
    }
    @Override
    public synchronized void onMessageReceived(MessageReceivedEvent messageReceivedEvent) {
        try {
            TextChannel textChannel = messageReceivedEvent.getTextChannel();
            if (messageReceivedEvent.getAuthor().isBot())
                return;
            if (messageReceivedEvent.getChannelType().equals(ChannelType.PRIVATE))
                return;
            if (commandCheck(messageReceivedEvent)) {
                if (messageReceivedEvent.getMessage().getContentStripped().startsWith("$ping"))
                    textChannel.sendMessage("Ping is " + akoBot.getPing()).queue();
                else if (messageReceivedEvent.getMessage().getContentStripped().startsWith("$shutdown")) {
                    textChannel.sendMessage("Good bye and good night bandori!").queue();
                    commandReader.shutDownMusic();
                    akoBot.shutdown();
                } else
                    commandReader.commandController(messageReceivedEvent, waiter);
            }
        }
        catch (Exception e) {
            logger.debug("Exception thrown {}", e);
        }
    }

    private boolean commandCheck(MessageReceivedEvent messageReceivedEvent) {
        return messageReceivedEvent.getMessage().getContentStripped().startsWith("$");
    }
}
