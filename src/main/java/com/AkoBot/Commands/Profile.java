package com.AkoBot.Commands;

import com.AkoBot.Bandori.BandoriCard;
import com.AkoBot.Bandori.BandoriCards;
import com.AkoBot.Bandori.BandoriTeam;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Profile {
    private String userId;
    private ArrayList<BandoriCard> cards = null;
    private BandoriTeam team;

    public Profile(String userId) {
        this.cards = new ArrayList<>();
        this.userId = userId;
    }

    public void addCards(BandoriCard bandoriCard) {
        this.cards.add(bandoriCard);
    }

    public boolean loadSave (String userId, BandoriCards bandoriCards) {
        try {
            String line;
            String id, xp, level;
            if (new File(fileNameGetter()).exists()) {
                RandomAccessFile raf = new RandomAccessFile(fileNameGetter(), "r");
                while (!(line = raf.readLine()).equals("TEAM")) {
                    id = line.substring(0, line.indexOf(" ") - 1);
                    line = line.substring(line.indexOf(" "));
                    xp = line.substring(0, line.indexOf(" ") - 1);
                    line = line.substring(line.indexOf(" "));
                    level = line;
                    for (BandoriCard card : bandoriCards.getBandoriCards()) {
                        if (card.getId() == Integer.parseInt(id)) {
                            card.setXp(Integer.parseInt(xp));
                            card.setLevel(Integer.parseInt(level));
                            cards.add(card);
                        }
                    }
                }
                while ((line = raf.readLine()) != null) {
                    id = line.substring(0, line.indexOf(" ") - 1);
                    line = line.substring(line.indexOf(" "));
                    xp = line.substring(0, line.indexOf(" ") - 1);
                    line = line.substring(line.indexOf(" "));
                    level = line;
                    for (BandoriCard card : bandoriCards.getBandoriCards()) {
                        if (card.getId() == Integer.parseInt(id)) {
                            card.setXp(Integer.parseInt(xp));
                            card.setLevel(Integer.parseInt(level));
                            team.addCard(card);
                        }
                    }
                }
            }
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }








    public void saveProfile() {
        try {
            RandomAccessFile raf = new RandomAccessFile(fileNameGetter(), "w");
            String save = "";
            for (BandoriCard bandoriCard : cards) {
                save = save.concat(bandoriCard.getId() + " " + bandoriCard.getXp() + " " + bandoriCard.getLevel() + "\n");
            }
            save = save.concat("TEAM");
            for (BandoriCard bandoriCard: team.getTeam()) {
                save = save.concat(bandoriCard.getId() + " " + bandoriCard.getXp() + " " + bandoriCard.getLevel() + "\n");
            }
            raf.writeChars(save);
        }
        catch (IOException f) {
        }
    }

    private boolean makeNewSaveFile() {
        try {
            if (new File(fileNameGetter()).exists())
                return false;
            File file = new File(fileNameGetter());
            return file.createNewFile();
        }
        catch (IOException e) {
            return false;
        }
    }

    private String fileNameGetter() {
        String directory = "./src/main/resources/profiles/";
        return directory + userId + ".txt";
    }


    public ArrayList<BandoriCard> getCards() {
        return cards;
    }

    public BandoriTeam getTeam() {
        return team;
    }

    public String getUserId() {
        return userId;
    }








}
