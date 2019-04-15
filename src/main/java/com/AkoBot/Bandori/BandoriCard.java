package com.AkoBot.Bandori;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;

public class BandoriCard {
    private JsonObject jsonObject;
    private int member;
    private BandoriMember bandoriMember;
    private int id;
    private String name;
    private String cameo_members;
    private String i_attribute;
    private int i_rarity;
    private boolean isTrained = false;
    private String rarityStars;
    private String japanese_name;
    private String release_date;
    private String small_image;
    private String small_image_trained;
    private String large_image;
    private String large_image_trained;
    private String large_image_transparent;
    private String large_image_transparent_trained;
    private String skill_name;
    private String japanese_skill_name;
    private String i_skill_type;
    //private String i_side_skill_type;
    //private String skill_template;
    //private String skill_variables;
    //private String side_skill_template;
    private String full_skill;
    private int performance_min;
    private int performance_max;
    private int performance_trained_max;
    private int technique_min;
    private int technique_max;
    private int technique_trained_max;
    private int visual_min;
    private int visual_max;
    private int visual_trained_max;
    private int xp;
    private int level;
    @SuppressWarnings("ConstantConditions")
    BandoriCard(JsonObject jsonObject, BandoriMembers bandoriMembers) {
        this.xp = 0;
        this.level = 1;
        this.jsonObject = jsonObject.getAsJsonObject();
        this.member = jsonObject.get("member").getAsInt();
        this.id = jsonObject.get("id").getAsInt();
        this.bandoriMember = bandoriMembers.getMemberById(this.member);
        this.name = cutter(jsonObject.get("name"));
        this.cameo_members = cutter(jsonObject.get("cameo_members"));
        this.i_attribute = cutter(jsonObject.get("i_attribute"));
        this.i_rarity = jsonObject.get("i_rarity").getAsInt();
        this.rarityStars = "☆☆☆☆".substring(0, i_rarity);
        this.japanese_name = cutter(jsonObject.get("japanese_name"));
        this.release_date = cutter(jsonObject.get("release_date"));
        this.small_image = cutter(jsonObject.get("image"));
        this.small_image_trained = jsonObject.get("image_trained") != null ? cutter(jsonObject.get("image_trained")) : null;
        this.large_image = cutter(jsonObject.get("art"));
        this.large_image_trained = jsonObject.get("art_trained") != null ? cutter(jsonObject.get("art_trained")) : null;
        this.large_image_transparent = cutter(jsonObject.get("transparent"));
        this.large_image_transparent_trained = jsonObject.get("transparent_trained") != null ? cutter(jsonObject.get("transparent_trained")) : null;
        this.skill_name = cutter(jsonObject.get("skill_name"));
        this.japanese_skill_name = cutter(jsonObject.get("japanese_skill_name"));
        this.i_skill_type = cutter(jsonObject.get("i_skill_type"));
        //this.i_side_skill_type = jsonObject.get("i_side_skill_type") != null ? cutter(jsonObject.get("i_side_skill_type")) : null;
        //this.skill_template = cutter(jsonObject.get("skill_template"));
        //this.skill_variables= jsonObject.get("skill_variables").getAsString();
        //this.side_skill_template = jsonObject.get("side_skill_template") != null ? cutter(jsonObject.get("side_skill_template")) : null;
        this.full_skill = cutter(jsonObject.get("full_skill"));
        this.full_skill = full_skill.substring(0, full_skill.length() - 1);
        this.performance_min = jsonObject.get("performance_min").getAsInt();
        this.performance_max = jsonObject.get("performance_max").getAsInt();
        this.performance_trained_max = jsonObject.get("performance_trained_max").getAsInt();
        this.technique_min = jsonObject.get("technique_min").getAsInt();
        this.technique_max = jsonObject.get("technique_max").getAsInt();
        this.technique_trained_max = jsonObject.get("technique_trained_max").getAsInt();
        this.visual_min = jsonObject.get("visual_min").getAsInt();
        this.visual_max = jsonObject.get("visual_max").getAsInt();
        this.visual_trained_max = jsonObject.get("visual_trained_max").getAsInt();
        if (cameo_members != null && cameo_members.contains(",")) {
            String[] cams = cameo_members.split(",");
            cameo_members = "";
            for (int i = 0; i < cams.length; i++) {
                cameo_members = cameo_members.concat(bandoriMembers.searchById(Integer.parseInt(cams[i])));
                if (i != cams.length - 1)
                    cameo_members = cameo_members.concat(", ");
            }
        }


    }
    @SuppressWarnings("ConstantConditions")
    EmbedBuilder getEmbedBuilder(BandoriMembers bandoriMembers, boolean trained) {
        BandoriTypes bandoriTypes = new BandoriTypes();
        EmbedBuilder embedBuilder = null;
        try {
            if (!trained)
                embedBuilder = new EmbedBuilder()
                        .setTitle(rarityStars + "\n" + "**" + name + "**" + "\n" + japanese_name)
                        .setDescription("**" + skill_name + "**" + "\n" + japanese_skill_name + "\n" + "*" + i_skill_type + "*" + "\n" + "*" + full_skill + "*\n" +
                                "[Want a closer look?]" + "(" + large_image_transparent + ")")
                        .setColor(new Color(bandoriTypes.getAttributeColor(i_attribute)))
                        .setThumbnail(small_image)
                        .setImage(large_image)
                        .setAuthor(bandoriMembers.searchById(member) + "", null, bandoriTypes.getBandIcon(bandoriMembers.searchById(member)))
                        .addField("Card ID", "" + id, true)
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
                            .setColor(new Color(bandoriTypes.getAttributeColor(i_attribute)))
                            .setThumbnail(small_image_trained)
                            .setImage(large_image_trained)
                            .setAuthor(bandoriMembers.searchById(member) + "", null, bandoriTypes.getBandIcon(bandoriMembers.searchById(member)))
                            .addField("Card ID", "" + id, true)
                            .addField("Released", release_date, true)
                            .addField("Cameo members", cameo_members, true)
                            .addField(null, null, true)
                            .addField("Performance", performance_trained_max + "", true)
                            .addField("Technique", technique_trained_max + "", true)
                            .addField("Visual", visual_trained_max + "", true);
                else {
                    embedBuilder.setTitle("That card cannot be trained!");
                    return embedBuilder;
                }
            }
            return embedBuilder;

        }
        catch (IllegalArgumentException e) {
            embedBuilder.setTitle("That card cannot be trained!");
            return embedBuilder;
        }
    }
    public boolean search(String category, String term) {
        BandoriTypes bandoriTypes = new BandoriTypes();
        switch (category) {
            case "membername":
                return bandoriTypes.getMemberType(term) == bandoriTypes.getMemberType(member);
            case "memberid":
                int id = bandoriTypes.getMemberId(term);
                return this.id == id;
            default:
                return this.jsonObject.get(category).toString().toLowerCase().contains(term);
        }
    }

    public String getName() {
        return name;
    }

    public String getSmall_image() {
        return small_image;
    }

    public int getId() {
        return id;
    }

    public int getI_rarity() {
        return i_rarity;
    }

    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getMember() {
        return member;
    }

    @SuppressWarnings("unused")
    public String getI_skill_type() {
        return i_skill_type;
    }

    @SuppressWarnings("unused")
    public String getI_attribute() {
        return i_attribute;
    }

    public int getAttributeInt() {
        if (i_attribute.equals("Cool")) {
            return 0;
        }
        else if (i_attribute.equals("Happy")) {
            return 1;
        }
        else if (i_attribute.equals("Power")) {
            return 2;
        }
        else if (i_attribute.equals("Pure")) {
            return 3;
        }
        else return -1;
    }

    public BandoriMember getBandoriMember() {
        return bandoriMember;
    }
    @SuppressWarnings("unused")
    private boolean addXp(int xp) {
        this.xp += xp;
        if (this.xp >= level * 100) {
            this.xp -= level * 100;
            xp += 1;
            if (this.xp >= level * 100) {
                return addXp(0);
            }
            else return false;
        }
        return false;
    }


    private String cutter(JsonElement jsonElement) {
        return jsonElement.toString() != null ? jsonElement.toString().substring(1, jsonElement.toString().length() - 1) : null;
    }

    public boolean searchCameo(BandoriCard bandoriCard) {
        if (this.cameo_members == null || !this.cameo_members.equals("")) {
            return false;
        }
        String id = bandoriCard.getId() + "";
        for (String search : this.cameoArray()) {
            if (id.equals(search)) {
                return true;
            }
        }
        return false;
    }
    public String getCardDescription() {
        return getName() + " (" + getBandoriMember().getName() + ") - " +
                "P: " + getPerformance() + " T: " + getTechnique() + " V: " + getVisual()
                + getI_skill_type();
    }
    public int getCameos(BandType type) {
        int total = 0;
        if (this.cameo_members != null && this.cameo_members.equals("")) {
            for (String search : this.cameoArray()) {
                if (new BandoriTypes().getBandTypeFromMemberId(Integer.parseInt(search)).equals(type))
                    total++;
            }
        }
        return total;
    }

    private String[] cameoArray() {
        String temp = cameo_members.replace(" ", "");
        return temp.split(",");
    }
    //FIXME
    public int getVisual() {
        int increase = this.visual_max - this.visual_min;
        if (isTrained)
            increase  = this.visual_trained_max - this.visual_min;
        double level = this.level /= 100.0;
        return this.visual_min + (int) (increase * level);
    }
    public int getTechnique() {
        int increase = this.technique_max - this.technique_min;
        if (isTrained)
            increase  = this.technique_max - this.technique_min;
        double level = this.level /= 100.0;
        return this.technique_min + (int) (increase * level);
    }
    public int getPerformance() {
        int increase = this.performance_max - this.performance_min;
        if (isTrained)
            increase  = this.performance_trained_max- this.performance_min;
        double level = this.level /= 100.0;
        return this.performance_min + (int) (increase * level);
    }
}