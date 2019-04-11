package com.AkoBot;

import com.AkoBot.Bandori.*;
import com.AkoBot.Commands.*;
import com.AkoBot.Commands.BandoriCommands.*;
import com.AkoBot.Commands.BandoriGameCommands.BandoriGachaCommand;
import com.AkoBot.Commands.BandoriGameCommands.BandoriGameCommand;
import com.AkoBot.Commands.HelpCommands.*;
import com.AkoBot.Commands.MinecraftCommands.MinecraftEndCommand;
import com.AkoBot.Commands.MinecraftCommands.MinecraftInfoCommand;
import com.AkoBot.Commands.MinecraftCommands.MinecraftStartCommand;
import com.AkoBot.Commands.MusicCommands.*;
import com.AkoBot.Music.*;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.LoggerFactory;


class CommandController {
	private BandoriSongs bandoriSongs = null;
//	final org.slf4j.Logger logger = LoggerFactory.getLogger(CommandController.class);
	private BandoriCards bandoriCards = null;
	private MusicManager musicManager = new MusicManager();
	private BandoriMembers bandoriMembers = null;
	private MinecraftServer minecraftServer = new MinecraftServer();
	private Profiles profiles = new Profiles();
	private EventWaiter waiter;
	/**
	 * handles commands
	 *
	 * @param messageReceivedEvent MessageReceivedEvent
	 */
    void commandController(MessageReceivedEvent messageReceivedEvent, EventWaiter waiter) {
		//get textchannel message was sent from and the appropriate command
		TextChannel textChannel = messageReceivedEvent.getTextChannel();
		String tag;
		String[] words;
		this.waiter = waiter;
		//try to just get the command phrase
		try {
			tag = messageReceivedEvent.getMessage().getContentStripped();
			words = tag.split(" ");
			tag = words[0];
			commandManager(messageReceivedEvent, tag);
		}
		//alert user of improper format
		catch (IndexOutOfBoundsException e) {
			incorrectCommandResponse(textChannel);
		}
	}

	/**
	 * sends message alerting user of issue
	 *
	 * @param textChannel textchannel to send message to
	 */
	private void incorrectCommandResponse(TextChannel textChannel) {
		textChannel.sendMessage("Sorry, could you repeat that? It didn't quite make sense to me... (Invalid Command)").queue();
	}

	/**
	 * Decides which command method needs to be run
	 *
	 * @param messageReceivedEvent MessageReceivedEvent
	 * @param tag                  error checked commmand to be executed
	 */
	private void commandManager(MessageReceivedEvent messageReceivedEvent, String tag) {
		//$help", "$play", "$join", "$leave", "$pause", "$resume", "$skip", "$clear", "$queue
		TextChannel textChannel = messageReceivedEvent.getTextChannel();
		//String text = messageReceivedEvent.getMessage().getContentStripped();
		switch (tag) {
			case "$refreshall":
				if (messageReceivedEvent.getAuthor().getId().equals("137710437490884608")) {
					bandoriSongs = new BandoriRefreshCommand().bandoriRefresh(messageReceivedEvent);
					bandoriMembers = new BandoriMembersLoadCommand().bandoriMembers(messageReceivedEvent);
					bandoriCards = new BandoriCardLoadCommand().bandoriCards(messageReceivedEvent, bandoriMembers);
				}
				break;
			case "$bangC":
				if (messageReceivedEvent.getAuthor().getId().equals("137710437490884608"))
					bandoriCards = new BandoriCardLoadCommand().bandoriCards(messageReceivedEvent, bandoriMembers);
				break;
			case "$bangMB":
				if (messageReceivedEvent.getAuthor().getId().equals("137710437490884608"))
					bandoriMembers = new BandoriMembersLoadCommand().bandoriMembers(messageReceivedEvent);
				break;
			case "$bandoriRefresh":
				if (messageReceivedEvent.getAuthor().getId().equals("137710437490884608"))
					bandoriSongs = new BandoriRefreshCommand().bandoriRefresh(messageReceivedEvent);
				break;
			case "$help":
				new HelpCommand().Help(messageReceivedEvent);
				break;
			case "$help1":
				new Help1Command().Help1(messageReceivedEvent);
				break;
			case "$help2":
				new Help2Command().Help2(messageReceivedEvent);
				break;
			case "$help3":
				new Help3Command().Help3(messageReceivedEvent);
				break;
            case "$help4":
                new Help4Command().Help4(messageReceivedEvent);
                break;
			case "$play":
				new MusicPlayCommand().Play(messageReceivedEvent, musicManager);
				break;
			case "$join":
				new JoinCommand().Join(messageReceivedEvent, musicManager);
				break;
			case "$leave":
				new MusicLeaveCommand().Leave(messageReceivedEvent, musicManager);
				break;
			case "$pause":
				new MusicPauseCommand().Pause(messageReceivedEvent, musicManager);
				break;
			case "$resume":
				new MusicResumeCommand().Resume(messageReceivedEvent, musicManager);
				break;
			case "$skip":
				new MusicSkipCommand().Skip(messageReceivedEvent, musicManager);
				break;
			case "$clear":
				new MusicClearCommand().Clear(messageReceivedEvent, musicManager);
				break;
			case "$queue":
			case "$q":
				new MusicQueueCommand().Queue(messageReceivedEvent, musicManager);
				break;
			case "$np":
				new MusicNowPlayingCommand().NowPlaying(messageReceivedEvent, musicManager);
				break;
			case "$bplay":
				new BandoriPlayCommand().bplay(messageReceivedEvent, musicManager, bandoriSongs);
				break;
			case "$bshuffle":
				new BandoriShuffleCommand().bShuffle(messageReceivedEvent, musicManager, bandoriSongs);
				break;
			case "$bsearch":
				new BandoriSongSearchCommand().searchBandoriSong(messageReceivedEvent, bandoriSongs);
				break;
			case "$card":
			case "$c":
				new BandoriCardCommand().bandoriCardCommand(messageReceivedEvent, bandoriCards, bandoriMembers);
				break;
			case "$member":
			case "$m":
				new BandoriMemberCommand().member(messageReceivedEvent, bandoriMembers);
				break;
			case "$startminecraft":
				minecraftServer = new MinecraftStartCommand().startMineCraft(messageReceivedEvent, minecraftServer);
				break;
			case "$infominecraft":
				new MinecraftInfoCommand().listServers(messageReceivedEvent, minecraftServer);
				break;
			case "$endminecraft":
				minecraftServer = new MinecraftEndCommand().EndMinecraft(messageReceivedEvent, minecraftServer);
				break;
			case "$gacha":
				new BandoriGachaCommand(bandoriCards.getBandoriCards()).rollGacha(messageReceivedEvent, retrieveProfile(messageReceivedEvent));
				break;
			case "$test":
				new BandoriGameCommand(messageReceivedEvent, waiter, profiles);
				break;
			default:
				incorrectCommandResponse(textChannel);
		}
	}
	void shutDownMusic() {
		new MusicShutdownCommand().shutDownMusic(musicManager);
		profiles.saveAll();
	}
	private Profile retrieveProfile(MessageReceivedEvent messageReceivedEvent) {
		return profiles.getProfile(messageReceivedEvent.getAuthor().getId());
	}
}