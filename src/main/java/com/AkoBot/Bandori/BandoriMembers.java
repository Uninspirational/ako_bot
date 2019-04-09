package com.AkoBot.Bandori;

import com.AkoBot.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class BandoriMembers {
    private ArrayList<BandoriMember> bandoriMembers;
    final org.slf4j.Logger logger = LoggerFactory.getLogger(BandoriMembers.class);

    public void bandoriRefresh(MessageReceivedEvent messageReceivedEvent) {
        try {
            String urlString = "https://bandori.party/api/members/?page=1";
            boolean flag = true;
            bandoriMembers = new ArrayList<>();
            JsonArray page;
            int i = 0;
            do {
                URL url = new URL(urlString);
                URLConnection urlConnection = url.openConnection();
                JsonParser jsonParser = new JsonParser();
                InputStreamReader inputStreamReader = new InputStreamReader((InputStream) urlConnection.getContent());
                JsonElement jsonElement = jsonParser.parse(inputStreamReader);
                JsonObject rootobj = jsonElement.getAsJsonObject();
                page = rootobj.getAsJsonArray("results");
                while (i < page.size()) {
                    bandoriMembers.add(new BandoriMember(page.get(i++).getAsJsonObject()));
                }
                i = 0;
                urlString = rootobj.get("next").toString();
                if (urlString.equals("null"))
                    flag = false;
                urlString = urlString.substring(1, urlString.length() - 1);
                inputStreamReader.close();
                if (urlString.equals("null"))
                    flag = false;
            } while (flag);
            messageReceivedEvent.getTextChannel().sendMessage("Done!").queue();
        }
        catch (IOException f) {
            logger.debug("" + f);
        }
    }
    public void controller(MessageReceivedEvent messageReceivedEvent) {
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        String message = messageReceivedEvent.getMessage().getContentStripped();
        try {
            String type = message.substring(message.indexOf(" ") + 1).toLowerCase();
            String keyterm = type.substring(type.indexOf(" ") + 1);
            type = type.substring(0, type.indexOf(" ")).toLowerCase();
            switch (type) {
                case "name":
                    searchForMember(textChannel, keyterm, "name");
                    break;
                case "id":
                    searchForMember(textChannel, keyterm, "id");
                    break;
                case "jpname":
                    searchForMember(textChannel, keyterm, "japanese_name");
                    break;
                case "band":
                    searchForMember(textChannel, keyterm, "i_band");
                    break;
                case "school":
                    searchForMember(textChannel, keyterm, "school");
                    break;
                case "year":
                    searchForMember(textChannel, keyterm, "i_school_year");
                    break;
                case "birthday":
                case "bday":
                    searchForMember(textChannel, keyterm, "birthday");
                    break;
                case "rmvc":
                case "usvoiceactor":
                case "envoiceactor":
                case "usva":
                case "enva":
                    searchForMember(textChannel, keyterm, "romaji_CV");
                    break;
                case "vc":
                case "jpvoiceactor":
                    searchForMember(textChannel, keyterm, "CV");
                    break;
                case "foodlike":
                case "fl":
                    searchForMember(textChannel, keyterm, "food_like");
                    break;
                case "fooddislike":
                case "foodhate":
                case "fd":
                    searchForMember(textChannel, keyterm, "food_dislike");
                    break;
                case "sign":
                    searchForMember(textChannel, keyterm, "i_astrological_sign");
                    break;
                case "instrument":
                    searchForMember(textChannel, keyterm, "instrument");
                    break;
                default:
                    textChannel.sendMessage("Um... what am I supposed to looking for? I don't know about anyone's " + keyterm).queue();
            }
        }
        catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            textChannel.sendMessage("You recite the spell like this: $member <category> <search term>").queue();
        }
    }
    public void searchForMember(TextChannel textChannel, String keyterm, String category) {
        ArrayList<BandoriMember> result = new ArrayList<>();
        for (BandoriMember bandoriMember: bandoriMembers) {
            if (bandoriMember.search(category, keyterm)) {
                result.add(bandoriMember);
            }
        }
        if (result.size() > 1)
            multipleResultPrinter(textChannel, result, category, keyterm);
        else if (result.size() == 1) {
            cardPrinter(textChannel, result.get(0));
        }
        else {
            textChannel.sendMessage("No members found").queue();
        }
    }
    public void cardPrinter(TextChannel textChannel, BandoriMember bandoriMember) {
        EmbedBuilder embedBuilder = bandoriMember.getEmbedMessage();
        MessageBuilder messageBuilder = new MessageBuilder()
                .setEmbed(embedBuilder.build());
        textChannel.sendMessage(messageBuilder.build()).queue();
    }
    public void multipleResultPrinter(TextChannel textChannel, ArrayList<BandoriMember> arrayList, String category, String keyterm) {
        String list = "";
        int i = 0;
        while (i < 25 && i < arrayList.size()){
            if (!(category.equals("food_like") || category.equals("food_dislike") || category.equals("name"))) {
                list = list.concat(category + " - ");
            }
            list = list.concat(arrayList.get(i).getName());
            list = list.concat("\n");
            i++;
        }
        String message = "Displaying " + i + " results for " + category + " - " + keyterm;
        EmbedBuilder embedBuilder =
                new EmbedBuilder()
                        .setTitle("I wonder if anybody here will help me become cooler..." + "\n\n" + message)
                        .setDescription(list)
                        .setColor(new Color(0xBA00BA));
        MessageBuilder messageBuilder = new MessageBuilder()
                .setEmbed(embedBuilder.build());
        textChannel.sendMessage(messageBuilder.build()).queue();
    }
    public String searchById(int id) {
        return bandoriMembers.get(id - 6).getName();
    }
    public BandoriMember getMemberById(int id) {
        for (BandoriMember bandoriMember : this.bandoriMembers) {
            if (bandoriMember.getId() == id) {
                return bandoriMember;
            }
        }
        return null;
    }
}