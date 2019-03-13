package com.AkoBot.Music;

import com.AkoBot.Bandori.BandoriSong;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class MusicManager {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(MusicManager.class);

    private final AudioPlayerManager manager = new DefaultAudioPlayerManager();
    private final Map<String, MusicController> players = new HashMap<String, MusicController>();

    /**
     *
     */
    public MusicManager(){
        AudioSourceManagers.registerRemoteSources(manager);
        AudioSourceManagers.registerLocalSource(manager);
        manager.setPlayerCleanupThreshold(10 * 60 * 1000);
    }

    /**
     * returns the player in a guild or makes one if one does not exist
     * @param guild the player should be in
     * @return the player in the guild
     */
    public MusicController getPlayer(Guild guild){
        if(!players.containsKey(guild.getId())) {
            players.put(guild.getId(), new MusicController(manager.createPlayer(), guild));
        }
        return players.get(guild.getId());
    }

    /**
     * remove player after disconnecting
     * @param guild guild bot was in
     */
    public void removePlayer(Guild guild) {
        if(players.containsKey(guild.getId())) {
            players.remove(guild.getId());
        }
    }

    /**
     *
     * @return the audio player manager
     */
    public AudioPlayerManager getManager() {
        return manager;
    }

    /**
     * look for video on youtube using GSON
     * @param textChannel where search command came from
     * @param source keyword to search for
     * @param member user who requested video
     * @return true if found
     */
    public boolean searchVideo(TextChannel textChannel, String source, Member member) {
        String keyword = source.replace(" ", "+");
        String apiUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&order=relevance&q=" + keyword + "&key=AIzaSyCXPibw1rn9TvyrIhwJfIJ2AErh-DCYBHA";
        try {
            //connect to url
            URL url = new URL(apiUrl);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(5 * 1000);
            urlConnection.setReadTimeout(5 * 1000);
            urlConnection.connect();
//            textChannel.sendMessage("Practicing at CiRCLE...").queue();

            //parse json object
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(new InputStreamReader((InputStream) urlConnection.getContent()));
            JsonObject rootobj = jsonElement.getAsJsonObject();

            //isolate the videoID from the JSONObject
            JsonObject jsonObject = rootobj.getAsJsonArray("items").get(0).getAsJsonObject();
            String test = jsonObject.getAsJsonObject("id").getAsJsonPrimitive("videoId").getAsString();
//            textChannel.sendMessage("id: " + test).queue();
            String result = "https://www.youtube.com/watch?v=" + test;
//            textChannel.sendMessage("Starting the performance!" + result).queue();
            loadTrack(textChannel, result, member, null);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
        catch (SocketTimeoutException f) {
            textChannel.sendMessage("Ako lost connection. Returning to room selection... Rin-rin... my live boosts...").queue();
            return false;
        }
        catch (IOException g) {
            g.printStackTrace();
            return false;
        }
        catch (NullPointerException h) {
            textChannel.sendMessage("No results found...").queue();
        }
        return true;
    }
    /**
     * adds a track to be played
     * @param textChannel the textchannel from which the play command came from
     * @param source the name of the song
     * @param member the member who sent the command
     */
    public void loadTrack(final TextChannel textChannel, final String source, final Member member, final BandoriSong bandoriSong){
        final Guild guild = textChannel.getGuild();
        final MusicController player = getPlayer(guild);
        System.out.println(source);
        try {
            URL test = new URL(source);
        }
        catch (MalformedURLException e) {
            if (bandoriSong == null) {
                textChannel.sendMessage("Writing song...").queue();
                searchVideo(textChannel, source, member);
                return;
            }
            System.out.println(bandoriSong.getUrl());
        }
        manager.loadItemOrdered(player, source, new AudioLoadResultHandler(){
            /**
             * add the youtube video into the queue
             * @param track video to be played
             */
            public void trackLoaded(AudioTrack track) {
                Song song;
                //if normal youtube video
                if (bandoriSong == null) {
                    textChannel.sendMessage(member.getEffectiveName() + ", I can't wait for you to hear us play " + track.getInfo().title).queue();
                    song = new Song(track, member, null);
                    song.setUrl(source);
                }
                //if bandori song
                else {
                    song = new Song(track, member, bandoriSong);
                    song.getBandoriSong().setAudioTrack(track);
                }
                players.get(guild.getId()).playTrack(song);
            }

            /**
             * adds a youtube playlist into the queue
             * DOES NOT WORK
             * @param playlist playlist to be played
             */
            public void playlistLoaded(AudioPlaylist playlist) {
                StringBuilder builder = new StringBuilder();
                builder.append("I hope you enjoy our performance!\n**").append(playlist.getName()).append("**\n");
                for(int i = 0; i < playlist.getTracks().size() && i < 10; i++){
                    AudioTrack track = playlist.getTracks().get(i);
                    builder.append("\n[").append(i + 1).append("]").append(" ** - ** ").append(track.getInfo().title);
                    players.get(textChannel.getGuild().getId()).playTrack(new Song(track, member,null));
                }
                textChannel.sendMessage(builder.toString()).queue();
            }

            /**
             *
             */
            public void noMatches() {
                //if (!searchVideo(textChannel, source, member)) {
                textChannel.sendMessage("I've never heard of that songs before...").queue();
                //}
            }

            /**
             * if loading failed
             * @param e exception triggered
             */
            public void loadFailed(FriendlyException e) {

                textChannel.sendMessage("Fuee... One more time!" + e.getCause().toString()).queue();
            }
        });
    }
}