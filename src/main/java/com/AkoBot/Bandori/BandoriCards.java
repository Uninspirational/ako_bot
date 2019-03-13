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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class BandoriCards {
    private ArrayList<JsonObject> bandoriCards;
    final org.slf4j.Logger logger = LoggerFactory.getLogger(BandoriCards.class);

    public void controller(MessageReceivedEvent messageReceivedEvent, BandoriMembers bandoriMembers) {
        try {
            TextChannel textChannel = messageReceivedEvent.getTextChannel();
            String message = messageReceivedEvent.getMessage().getContentStripped();
            String type = message.substring(message.indexOf(" ") + 1).toLowerCase();
            String keyterm = type.substring(type.indexOf(" ") + 1);
            type = type.substring(0, type.indexOf(" ")).toLowerCase();
            boolean trained = keyterm.endsWith("-t");
            if (trained)
                keyterm = keyterm.substring(0, keyterm.length() - 3);
            switch (type) {
                case "name":
                    searchForCard(textChannel, keyterm, trained, bandoriMembers, "name");
                    break;
                case "attribute":
                    searchForCard(textChannel, keyterm, trained, bandoriMembers, "i_attribute");
                    break;
                case "rarity":
                case "stars":
                    searchForCard(textChannel, keyterm, trained, bandoriMembers, "i_rarity");
                    break;
                case "japanese name":
                case "jname":
                    searchForCard(textChannel, keyterm, trained, bandoriMembers, "japanese_name");
                    break;
                case "release date":
                case "rdate":
                case "date":
                    searchForCard(textChannel, keyterm, trained, bandoriMembers, "release_date");
                    break;
                case "skill name":
                case "skill":
                    searchForCard(textChannel, keyterm, trained, bandoriMembers, "skill_name");
                    break;
                case "japanese skill name":
                case "jskill":
                case "jskill name":
                    searchForCard(textChannel, keyterm, trained, bandoriMembers, "japanese_skill_name");
                    break;
                case "skill type":
                    searchForCard(textChannel, keyterm, trained, bandoriMembers, "i_skill_type");
                    break;
                case "cameo members":
                case "cameos":
                case "cameo":
                    searchForCard(textChannel, keyterm, trained, bandoriMembers, "cameo_members");
                    break;
            }
        }
        catch (IndexOutOfBoundsException e) {
            logger.debug("" + e);
        }
    }
    public void bandoriRefresh (MessageReceivedEvent messageReceivedEvent) {
        try {
            String urlString = "https://bandori.party/api/cards/?page=1";
            boolean flag = true;
            bandoriCards = new ArrayList<>();
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
                    bandoriCards.add(page.get(i++).getAsJsonObject());
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
    private void cardPrinter(TextChannel textChannel, JsonObject card, boolean trained, BandoriMembers bandoriMembers) {
        int member = card.get("member").getAsInt();
        int id = card.get("id").getAsInt();
        String name = cutter(card.get("name"));
        String i_attribute = cutter(card.get("i_attribute"));
        int i_rarity = card.get("i_rarity").getAsInt();
        String rarityStars = "☆☆☆☆".substring(0, i_rarity);
        String japanese_name = cutter(card.get("japanese_name"));
        String release_date = cutter(card.get("release_date"));
        String small_image = cutter(card.get("image"));
        String small_image_trained = card.get("image_trained") != null ? cutter(card.get("image_trained")) : null;
        String large_image = cutter(card.get("art"));
        String large_image_trained = card.get("art_trained") != null ? cutter(card.get("art_trained")) : null;
        String large_image_transparent = cutter(card.get("transparent"));
        String large_image_transparent_trained = card.get("transparent_trained") != null ? cutter(card.get("transparent_trained")) : null;
        String skill_name = cutter(card.get("skill_name"));
        String japanese_skill_name = cutter(card.get("japanese_skill_name"));
        String i_skill_type = cutter(card.get("i_skill_type"));
        //String i_side_skill_type = card.get("i_side_skill_type") != null ? cutter(card.get("i_side_skill_type")) : null;
        //String skill_template = cutter(card.get("skill_template"));
        //String skill_variables= card.get("skill_variables").getAsString();
        //String side_skill_template = card.get("side_skill_template") != null ? cutter(card.get("side_skill_template")) : null;
        String full_skill = cutter(card.get("full_skill"));
        full_skill = full_skill.substring(0, full_skill.length() - 1);
        int performance_min = card.get("performance_min").getAsInt();
        int performance_max = card.get("performance_max").getAsInt();
        int performance_trained_max = card.get("performance_trained_max").getAsInt();
        int technique_min = card.get("technique_min").getAsInt();
        int technique_max = card.get("technique_max").getAsInt();
        int technique_trained_max = card.get("technique_trained_max").getAsInt();
        int visual_min = card.get("visual_min").getAsInt();
        int visual_max = card.get("visual_max").getAsInt();
        int visual_trained_max = card.get("visual_trained_max").getAsInt();
        String cameo_members = cutter(card.get("cameo_members"));
        if (cameo_members != null && cameo_members.contains(",")) {
            String[] cams = cameo_members.split(",");
            cameo_members = "";
            for (int i = 0; i < cams.length; i++) {
                cameo_members = cameo_members.concat(bandoriMembers.searchById(Integer.parseInt(cams[i])));
                if (i != cams.length - 1)
                    cameo_members = cameo_members.concat(", ");
            }
        }
        EmbedBuilder embedBuilder = null;
        if (!trained)
            embedBuilder = new EmbedBuilder()
                    .setTitle(rarityStars + "\n" + "**" + name + "**" + "\n" + japanese_name)
                    .setDescription("**" + skill_name + "**" + "\n" + japanese_skill_name + "\n" + "*" + i_skill_type + "*" + "\n" + "*" + full_skill + "*\n" +
                            "[Want a closer look?]" + "(" + large_image_transparent + ")")
                    .setColor(new Color(getAttributeColor(i_attribute)))
                    .setThumbnail(small_image)
                    .setImage(large_image)
                    .setAuthor(bandoriMembers.searchById(member) + "", null, bandoriMembers.bandIcon(member))
                    .addField("Released", release_date, true)
                    .addField("Cameo members", cameo_members, true)
                    .addField(null, null, true)
                    .addField("Performance", performance_min + "/" + performance_max, true)
                    .addField("Technique", technique_min + "/" + technique_max, true)
                    .addField("Visual", visual_min + "/" + visual_max, true);
        else {
            if (small_image != null)
                embedBuilder = new EmbedBuilder()
                        .setTitle(rarityStars + "\n" + "**" + name + "**" + "\n" + japanese_name)
                        .setDescription("**" + skill_name + "**" + "\n" + japanese_skill_name + "\n" + "*" + i_skill_type + "*" + "\n" + "*" + full_skill + "*\n" +
                                "[Click here for the transparent image]" + "(" + large_image_transparent_trained + ")")
                        .setColor(new Color(getAttributeColor(i_attribute)))
                        .setThumbnail(small_image_trained)
                        .setImage(large_image_trained)
                        .setAuthor(bandoriMembers.searchById(member) + "", null, bandoriMembers.bandIcon(member))
                        .addField("Released", release_date, true)
                        .addField("Cameo members", cameo_members, true)
                        .addField(null, null, true)
                        .addField("Performance", performance_trained_max + "", true)
                        .addField("Technique", technique_trained_max + "", true)
                        .addField("Visual", visual_trained_max + "", true);
            else
                textChannel.sendMessage("That card cannot be trained!").queue();
        }
        if (embedBuilder != null) {
            MessageBuilder messageBuilder = new MessageBuilder()
                    .setEmbed(embedBuilder.build());
            textChannel.sendMessage("Card ID: " + id + messageBuilder.build()).queue();
        }
    }
    private void searchForCard(TextChannel textChannel, String keyterm, boolean trained, BandoriMembers bandoriMembers, String category) {
        boolean exist = false;
        boolean multiple = false;
        String name;
        JsonObject[] result = new JsonObject[25];
        int j = 0;
        for (JsonObject finder: bandoriCards) {
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
            cardPrinter(textChannel, result[0], trained, bandoriMembers);
    }
    private void multipleResultPrinter(TextChannel textChannel, JsonObject[] jsonObjects, String category, String keyterm) {
        String list = "";
        int i = 0;
        while (jsonObjects[i] != null){
            list = list.concat(category + " - ");
            list = list.concat(cutter(jsonObjects[i].get("name")));
            list = list.concat("\n");
            i++;
        }
        String message = "Displaying " + i + " results for " + category + " - " + keyterm;
        EmbedBuilder embedBuilder =
                new EmbedBuilder()
                        .setTitle("Wow I wish I could pull a four star one day..." + "\n\n" + message)
                        .setDescription(list)
                        .setColor(new Color(0xBA00BA));
        MessageBuilder messageBuilder = new MessageBuilder()
                .setEmbed(embedBuilder.build());
        textChannel.sendMessage(messageBuilder.build()).queue();
    }
    private String cutter(JsonElement jsonElement) {
        return jsonElement.toString() != null ? jsonElement.toString().substring(1, jsonElement.toString().length() - 1) : null;
    }
    private int getAttributeColor(String attribute) {
        switch (attribute) {
            case "Pure":
                return 0x3ECC24;
            case "Happy":
                return 0xFF8302;
            case "Cool":
                return 0xF72C53;
            case "Powerful":
                return 0x3E5BD3;
            default:
                return 0x000000;
        }
    }
}
