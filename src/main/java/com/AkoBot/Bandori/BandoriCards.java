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
    private ArrayList<BandoriCard> bandoriCards;
    final org.slf4j.Logger logger = LoggerFactory.getLogger(BandoriCards.class);

    public void controller(MessageReceivedEvent messageReceivedEvent, BandoriMembers bandoriMembers) {
        TextChannel textChannel = messageReceivedEvent.getTextChannel();

        try {
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
                case "membername":
                    searchForCard(textChannel, keyterm, trained, bandoriMembers, "membername");
                    break;
                case "memberid":
                    searchForCard(textChannel, keyterm, trained, bandoriMembers, "memberid");
                    break;
                case "attribute":
                case "i_attribute":
                    searchForCard(textChannel, keyterm, trained, bandoriMembers, "i_attribute");
                    break;
                case "rarity":
                case "i_rarity":
                case "stars":
                    searchForCard(textChannel, keyterm, trained, bandoriMembers, "i_rarity");
                    break;
                case "japanese name":
                case "japanese_name":
                case "jname":
                    searchForCard(textChannel, keyterm, trained, bandoriMembers, "japanese_name");
                    break;
                case "release date":
                case "release_date":
                case "rdate":
                case "date":
                    searchForCard(textChannel, keyterm, trained, bandoriMembers, "release_date");
                    break;
                case "skill name":
                case "skill_name":
                case "skill":
                    searchForCard(textChannel, keyterm, trained, bandoriMembers, "skill_name");
                    break;
                case "japanese skill name":
                case "japanese_skill_name":
                case "jskill":
                case "jskill name":
                case "jskill_name":
                    searchForCard(textChannel, keyterm, trained, bandoriMembers, "japanese_skill_name");
                    break;
                case "skill type":
                case "skill_type":
                case "i_skill_type":
                    searchForCard(textChannel, keyterm, trained, bandoriMembers, "i_skill_type");
                    break;
                case "cameo members":
                case "cameo_members":
                case "cameos":
                case "cameo":
                    searchForCard(textChannel, keyterm, trained, bandoriMembers, "cameo_members");
                    break;
            }
        }
        catch (IndexOutOfBoundsException e) {
            textChannel.sendMessage("$card <category> <search term>").queue();
            logger.debug("" + e);
        }
    }
    public void bandoriRefresh (MessageReceivedEvent messageReceivedEvent, BandoriMembers bandoriMembers) {
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
                    bandoriCards.add(new BandoriCard(page.get(i++).getAsJsonObject(), bandoriMembers));
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
    private void cardPrinter(TextChannel textChannel, BandoriCard bandoriCard, boolean trained, BandoriMembers bandoriMembers) {
        EmbedBuilder embedBuilder = bandoriCard.getEmbedBuilder(bandoriMembers, trained);
        MessageBuilder messageBuilder = new MessageBuilder()
                .setEmbed(embedBuilder.build());
        textChannel.sendMessage(messageBuilder.build()).queue();
    }
    private void searchForCard(TextChannel textChannel, String keyterm, boolean trained, BandoriMembers bandoriMembers, String category) {
        ArrayList<BandoriCard> result = new ArrayList<>();
        int j = 0;
        try {
            for (BandoriCard bandoriCard : bandoriCards) {
                System.out.println(bandoriCard.getName());
                if (bandoriCard.search(category, keyterm)) {
                    result.add(bandoriCard);
                    if (j++ == 24)
                        break;
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {

        }
        if (result.size() > 1)
            multipleResultPrinter(textChannel, result, category, keyterm);
        else if (result.size() == 1)
            cardPrinter(textChannel, result.get(0), trained, bandoriMembers);
        else {
            textChannel.sendMessage("No card was found").queue();
        }
    }
    private void multipleResultPrinter(TextChannel textChannel, ArrayList<BandoriCard> arrayList, String category, String keyterm) {
        String list = "";
        int i = 0;
        for (BandoriCard bandoriCard: arrayList) {
            list = list.concat(category + " - ");
            list = list.concat(bandoriCard.getName());
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

}
