package com.AkoBot.Commands.BandoriGameCommands;

import com.AkoBot.Bandori.BandoriCard;
import com.AkoBot.Commands.Profile;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

public class BandoriGachaCommand {
    ArrayList<BandoriCard> fourStars;
    ArrayList<BandoriCard> threeStars;
    ArrayList<BandoriCard> twoStars;
    ArrayList<BandoriCard> oneStars;
    public BandoriGachaCommand(ArrayList<BandoriCard> allCards) {
        this.fourStars = new ArrayList<>();
        this.threeStars = new ArrayList<>();
        this.twoStars = new ArrayList<>();
        this.oneStars = new ArrayList<>();
        for (BandoriCard card: allCards) {
            if (card.getI_rarity() == 4) {
                fourStars.add(card);
            }
            else if (card.getI_rarity() == 3) {
                threeStars.add(card);
            }
            else if (card.getI_rarity() == 2) {
                twoStars.add(card);
            }
            else if (card.getI_rarity() == 1) {
                oneStars.add(card);
            }
        }
    }
    public int rollNumber(boolean guaranteed) {
        //3, 8.5, 88.5
        Random r = new Random();
        r.setSeed(System.currentTimeMillis());
        int roll = r.nextInt(1000) + 1;
        if (guaranteed) {
            if (roll <= 30) {
                return 4;
            }
            else return 3;
        }
        if (roll <= 30) {
            return 4;
        }
        else if (roll < 85) {
            return 3;
        }
        else {
            return 2;
        }
    }
    public Profile rollGacha(MessageReceivedEvent messageReceivedEvent, Profile profile) {
        try {
            BandoriCard bandoriCard;
            String tempFP = "./src/main/resources/GachaTemp.png", templateFP = "./src/main/resources/GachaTemplate.png";
            BufferedImage image = ImageIO.read(new File(templateFP));
            TextChannel textChannel = messageReceivedEvent.getTextChannel();
            Graphics2D g = image.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            boolean guaranteed = true;
            BandoriCard bandoriCard1;
            BufferedImage bufferedImage;
            int roll;
            String four = "", three = "", two = "";
            EmbedBuilder embedBuilder = new EmbedBuilder();
            for (int i = 0; i < 10; i++) {
                roll = rollNumber((i == 9) && guaranteed);
                if (roll >= 3)
                    guaranteed = false;
                bandoriCard = getGachaCard(roll);
                bufferedImage = getGachaImage(bandoriCard);
                profile.addCards(bandoriCard);
                if (roll == 4)
//                    four = four.concat(bandoriCard.getName()) + (i != 9 ? ", " : "");
                    four = four.concat((!four.equals("") ? ", " : "") + "" + bandoriCard.getId());
                else if (roll == 3) {
//                    three = three.concat(bandoriCard.getName()) + (i != 9 ? ", " : "");
                    three = three.concat((!three.equals("") ? ", " : "") + "" + bandoriCard.getId());
                }
                else if (roll == 2) {
//                    two = two.concat(bandoriCard.getName()) + (i != 9 ? ", " : "");
                    two = two.concat((!two.equals("") ? ", " : "") + "" + bandoriCard.getId());
                }
                g.drawImage(bufferedImage, i * 180 + ((i / 5) * (-180 * 5)), (i / 5) * 180, null);
            }
            g.dispose();
            ImageIO.write(image, "png", new File(tempFP));
            embedBuilder.addField("Two Stars", two, false);
            embedBuilder.addField("Three Stars", three, false);
            embedBuilder.addField("Four Stars", four, false);


            MessageBuilder messageBuilder = new MessageBuilder().setEmbed(embedBuilder.build());
            textChannel.sendFile(new File(tempFP), messageBuilder.build()).queue();


            return profile;
        }
        catch (IOException e) {
            e.printStackTrace();
            return profile;
        }
    }
    private BandoriCard getGachaCard(int rarity) {
        Random r = new Random();
        r.setSeed(System.currentTimeMillis());
        BandoriCard bandoriCard;
        try {
            switch (rarity) {
                case 4:
                    bandoriCard = fourStars.get(r.nextInt(fourStars.size()));
                    ImageIO.read(new URL(bandoriCard.getSmall_image()));
                    return bandoriCard;

                case 3:
                    bandoriCard = threeStars.get(r.nextInt(threeStars.size()));
                    ImageIO.read(new URL(bandoriCard.getSmall_image()));
                    return bandoriCard;
                case 2:
                    bandoriCard = twoStars.get(r.nextInt(twoStars.size()));
                    ImageIO.read(new URL(bandoriCard.getSmall_image()));
                    return bandoriCard;
                default:
                    return null;
            }
        }
        catch (IOException f) {
            return getGachaCard(rarity);
        }
    }
    private BufferedImage getGachaImage(BandoriCard bandoriCard) {
        try {
            return ImageIO.read(new URL(bandoriCard.getSmall_image()));
        }
        catch (IOException f) {
            return null;
        }
    }
}
