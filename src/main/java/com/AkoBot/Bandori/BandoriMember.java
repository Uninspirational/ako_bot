package com.AkoBot.Bandori;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dv8tion.jda.core.EmbedBuilder;

public class BandoriMember {
    private int id;
    private String name;
    private String japanese_name;
    private String image;
    private String square_image;
    private String i_band;
    private String school;
    private String i_school_year;
    private String romaji_CV;
    private String CV;
    private String birthday;
    private String food_like;
    private String food_dislike;
    private String i_astrological_sign;
    private String instrument;
    private String description;
    private JsonObject jsonObject;
    public BandoriMember(JsonObject jsonObject) {
        jsonObject = jsonObject.getAsJsonObject();
        this.jsonObject = jsonObject;
        this.id = jsonObject.get("id").getAsInt();
        this.name = cutter(jsonObject.get("name"));
        this.japanese_name = cutter(jsonObject.get("japanese_name"));
        this.image = cutter(jsonObject.get("image"));
        this.square_image = cutter(jsonObject.get("square_image"));
        this.i_band = cutter(jsonObject.get("i_band"));
        this.school = cutter(jsonObject.get("school"));
        this.i_school_year = cutter(jsonObject.get("i_school_year"));
        this.romaji_CV = cutter(jsonObject.get("romaji_CV"));
        this.CV = cutter(jsonObject.get("CV"));
        this.birthday = cutter(jsonObject.get("birthday"));
        this.food_like = cutter(jsonObject.get("food_like"));
        this.food_dislike = cutter(jsonObject.get("food_dislike"));
        this.i_astrological_sign = cutter(jsonObject.get("i_astrological_sign"));
        this.instrument = cutter(jsonObject.get("instrument"));
        this.description = cutter(jsonObject.get("description"));
    }
    public boolean search(String category, String term) {
        if (category.equals("id")) {
            return this.jsonObject.get(category).toString().toLowerCase().equals(term);
        }
        return this.jsonObject.get(category).toString().toLowerCase().contains(term);
    }

    public String getName() {
        return name;
    }

    public EmbedBuilder getEmbedMessage() {
        BandoriTypes bandoriTypes = new BandoriTypes();
        return new EmbedBuilder()
                        .setTitle("Member ID: " + this.id + " **" + this.name + "**" + "\n" + this.japanese_name)
                        .setDescription(this.i_school_year + " year " + "at **" + this.school + "**\n" + "*" + this.description + "*")
                        .setColor(bandoriTypes.getColor(bandoriTypes.getMemberType(this.name)))
                        .setThumbnail(this.square_image)
                        .setImage(this.image)
                        .addField(this.instrument, "", false)
                        .setAuthor(this.i_band, null, bandoriTypes.getBandIcon(i_band))
                        .addField("Voice Actor", this.romaji_CV + "\n" + this.CV, false)
                        .addField("Favorite foods", this.food_like, true)
                        .addField("Least favorite foods", this.food_dislike, true)
                        .addField("Birthday", this.birthday, true)
                        .addField("Astrological sign", this.i_astrological_sign, true);
    }

    private String cutter(JsonElement jsonElement) {
        return jsonElement.toString().substring(1, jsonElement.toString().length() - 1);
    }
}
