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
    private ArrayList<JsonObject> bandoriMembers;
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
                while (i < page.size())
                    bandoriMembers.add(page.get(i++).getAsJsonObject());
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
            new Logger().logError(f);
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
                    searchForMember(textChannel, keyterm, "food_like");
                    break;
                case "fooddislike":
                case "foodhate":
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
        boolean exist = false;
        boolean multiple = false;
        String name;
        JsonObject[] result = new JsonObject[25];
        int j = 0;
        for (JsonObject finder: bandoriMembers) {
            finder = finder.getAsJsonObject();
            name = finder.get(category).toString().toLowerCase();
            if (name.contains(keyterm)) {
                result[j++] = finder;
                if (exist)
                    multiple = true;
                exist = true;
            }
        }
        if (multiple)
            multipleResultPrinter(textChannel, result, category, keyterm);
        else if (exist)
            cardPrinter(textChannel, result[0]);
    }
    public String cutter(JsonElement jsonElement) {
        return jsonElement.toString().substring(1, jsonElement.toString().length() - 1);
    }
    public void cardPrinter(TextChannel textChannel, JsonObject jsonObject) {
        int id = jsonObject.get("id").getAsInt();
        String name = cutter(jsonObject.get("name"));
        String japanese_name = cutter(jsonObject.get("japanese_name"));
        String image = cutter(jsonObject.get("image"));
        String square_image = cutter(jsonObject.get("square_image"));
        String i_band = cutter(jsonObject.get("i_band"));
        String school = cutter(jsonObject.get("school"));
        String i_school_year = cutter(jsonObject.get("i_school_year"));
        String romaji_CV = cutter(jsonObject.get("romaji_CV"));
        String CV = cutter(jsonObject.get("CV"));
        String birthday = cutter(jsonObject.get("birthday"));
        String food_like = cutter(jsonObject.get("food_like"));
        String food_dislike = cutter(jsonObject.get("food_dislike"));
        String i_astrological_sign = cutter(jsonObject.get("i_astrological_sign"));
        String instrument = cutter(jsonObject.get("instrument"));
        String description = cutter(jsonObject.get("description"));
        EmbedBuilder embedBuilder =
                new EmbedBuilder()
                        .setTitle("**" + name + "**" + "\n" + japanese_name)
                        .setDescription(i_school_year + " year " + "at **" + school + "**\n" + "*" + description + "*")
                        .setColor(new Color(0xBA00BA))
                        .setThumbnail(square_image)
                        .setImage(image)
                        .addField(instrument, "", false)
                        .setAuthor(i_band, null, bandIcon(i_band))
                        .addField("Voice Actor", romaji_CV + "\n" + CV, false)
                        .addField("Favorite foods", food_like, true)
                        .addField("Least favorite foods", food_dislike, true)
                        .addField("Birthday", birthday, true)
                        .addField("Astrological sign", i_astrological_sign, true);
        MessageBuilder messageBuilder = new MessageBuilder()
                .setEmbed(embedBuilder.build());
        textChannel.sendMessage("Member ID: " + id + messageBuilder.build()).queue();
    }
    public void multipleResultPrinter(TextChannel textChannel, JsonObject[] jsonObjects, String category, String keyterm) {
        String list = "";
        int i = 0;
        while (jsonObjects[i] != null){
            if (!(category.equals("food_like") || category.equals("food_dislike") || category.equals("name"))) {
                list = list.concat(category + " - ");
            }
            list = list.concat(cutter(jsonObjects[i].get("name")));
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
    public String bandIcon(String bandname) {
        switch (bandname) {
            case ("Poppin'Party") :
                return "https://vignette.wikia.nocookie.net/bandori/images/1/1f/PoPiPa_icon.png/revision/latest?cb=20180522125930";
            case ("Hello, Happy World!") :
                return "https://vignette.wikia.nocookie.net/bandori/images/5/52/HaroHapi_icon.png/revision/latest?cb=20180522125928";
            case ("Afterglow") :
                return "https://vignette.wikia.nocookie.net/bandori/images/0/01/Afterglow_icon.png/revision/latest?cb=20180522125931";
            case ("Roselia") :
                return "https://vignette.wikia.nocookie.net/bandori/images/d/db/Roselia_icon.png/revision/latest?cb=20180522125933";
            case ("Pastel*Palettes") :
                return "https://vignette.wikia.nocookie.net/bandori/images/0/0b/PasuPare_icon.png/revision/latest?cb=20180522125926";
            default:
                return "";
        }
    }
    public String bandIcon(int id) {
        return bandIcon(cutter(bandoriMembers.get((id - 6)).get("i_band")));
    }
    public String searchById(int id) {
        return cutter(bandoriMembers.get(id - 6).get("name"));
    }
}