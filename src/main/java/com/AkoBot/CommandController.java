package com.AkoBot;

import com.AkoBot.Bandori.*;
import com.AkoBot.Commands.*;
import com.AkoBot.Commands.BandoriCommands.*;
import com.AkoBot.Commands.HelpCommands.Help1Command;
import com.AkoBot.Commands.HelpCommands.Help2Command;
import com.AkoBot.Commands.HelpCommands.Help3Command;
import com.AkoBot.Commands.HelpCommands.HelpCommand;
import com.AkoBot.Commands.MinecraftCommands.MinecraftEndCommand;
import com.AkoBot.Commands.MinecraftCommands.MinecraftInfoCommand;
import com.AkoBot.Commands.MinecraftCommands.MinecraftStartCommand;
import com.AkoBot.Commands.MusicCommands.*;
import com.AkoBot.Music.*;
import com.AkoBot.Playlist.*;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.LoggerFactory;

import java.io.*;

public class CommandController {
	private BandoriSongs bandoriSongs = null;
	final org.slf4j.Logger logger = LoggerFactory.getLogger(CommandController.class);
	private BandoriCards bandoriCards = null;
	private MusicManager musicManager = new MusicManager();
	private BandoriMembers bandoriMembers = null;
	private MinecraftServer minecraftServer = new MinecraftServer();
	/**
	 * handles commands
	 *
	 * @param messageReceivedEvent MessageReceivedEvent
	 */
	public void commandController(MessageReceivedEvent messageReceivedEvent) {

		//get textchannel message was sent from and the appropriate command
		TextChannel textChannel = messageReceivedEvent.getTextChannel();
		String tag;
		String[] words;
		//try to just get the command phrase
		try {
			tag = messageReceivedEvent.getMessage().getContentStripped();
			words = tag.split(" ");
			tag = words[0];
			commandManager(messageReceivedEvent, tag);
		}
		//alert user of improper format
		catch (IndexOutOfBoundsException e) {
			textChannel.sendMessage("index out of bounds lul").queue();
			e.printStackTrace();
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
			case "$bandoriRefresh":
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
//			case "$bshuffle":
//				bshuffle(messageReceivedEvent);
//				break;
			case "$bsearch":
				new BandoriSongSearchCommand().searchBandoriSong(messageReceivedEvent, bandoriSongs);
				break;
			case "$bangC":
				bandoriCards = new BandoriCardLoadCommand().bandoriCards(messageReceivedEvent);
				break;
			case "$card":
			case "$c":
				new BandoriCardCommand().bandoriCardCommand(messageReceivedEvent, bandoriCards, bandoriMembers);
				break;
			case "$bangMB":
				bandoriMembers = new BandoriMembersLoadCommand().bandoriMembers(messageReceivedEvent);
				break;
			case "$member":
			case "$m":
				new BandoriMemberCommand().member(messageReceivedEvent, bandoriMembers);
				break;
			case "$startminecraft":
				if (minecraftServer.isRunning()) {
					textChannel.sendMessage("A server is already running!").queue();
				}
				else {
					minecraftServer = new MinecraftStartCommand().startMineCraft(messageReceivedEvent);
				}
				break;
			case "$infominecraft":
				new MinecraftInfoCommand().listServers(messageReceivedEvent, minecraftServer);
			case "$endminecraft":
				if (new MinecraftEndCommand().EndMinecraft(messageReceivedEvent, minecraftServer))
					minecraftServer = null;
				break;
			default:
				incorrectCommandResponse(textChannel);
		}
	}
	public void shutDownMusic() {
		new MusicShutdownCommand().shutDownMusic(musicManager);
	}
	/**
	 * shuffles songs by a certain band
	 * @param messageReceivedEvent messageReceivedEvent
	 */
//	private void bshuffle(MessageReceivedEvent messageReceivedEvent) {
//		Date date = new Date();
//		Random random = new Random();
//		random.setSeed(date.getTime());
//		TextChannel textChannel = messageReceivedEvent.getTextChannel();
//		Member member = messageReceivedEvent.getMember();
//		String[] keyword = messageReceivedEvent.getMessage().getContentStripped().split(" ");
//		String band;
//		boolean alpha = false;
//		ArrayList<BandoriSong> sorted = new ArrayList<BandoriSong>();
//		int i = 0, place;
//		try {
//			//shuffle all songs
//			if (keyword[1].toLowerCase().equals("all")) {
//				while (i < bandoriSongs.size()) {
//					sorted.add(bandoriSongs.get(i++));
//				}
//				textChannel.sendMessage("I shuffled all songs randomly! Umm... Rin-rin! Teach me something cool to say here~").queue();
//			}
//			else {
//				//shuffle songs by band
//				if (keyword.length == 3 && keyword[2].equals("-a")) {
//					alpha = true;
//				}
//				while (i < bandoriSongs.size()) {
//					band = bandoriSongs.get(i).getBand();
//					//System.out.println(keyword + " " + band + (band.toLowerCase().equals(keyword.toLowerCase()) || band.toLowerCase().contains(keyword.toLowerCase())));
//					if (band.toLowerCase().contains(keyword[1].toLowerCase())) {
//						sorted.add(bandoriSongs.get(i));
//						//System.out.println(sorted.get(j).getName() + " " + sorted.get(j).getBand() + "RAN");
//					}
//					i++;
//				}
//				textChannel.sendMessage("I shuffled songs by " + (sorted.get(0).getBand().equals("Roselia") ? "...wait I'm in Roselia! " : sorted.get(0).getBand()) + ((alpha) ? "Also, the songs should play in alphabetical order now!" : "")).queue();
//
//			}
//			//queue up chosen songs
//			Join(messageReceivedEvent);
//			for (i = 0; i < sorted.size(); i++) {
//				if (alpha) {
//					place = i;
//					loadBandoriSong(textChannel, sorted.get(i), member);
//				}
//				else {
//					place = random.nextInt(sorted.size());
//					loadBandoriSong(textChannel, sorted.get(place), member);
//				}
////				System.out.println(sorted.get(place).getName() + " " + sorted.get(place).getBand());
//				sorted.remove(place);
//			}
//		}
//		catch (NullPointerException e) {
//			e.printStackTrace();
//			textChannel.sendMessage("I haven't practiced enough yet $bangM to give me some time!").queue();
//		}
//	}
}