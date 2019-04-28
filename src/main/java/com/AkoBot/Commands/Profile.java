package com.AkoBot.Commands;

import com.AkoBot.Bandori.BandoriCard;
import com.AkoBot.Bandori.BandoriCards;
import com.AkoBot.Bandori.BandoriTeam;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.*;
import java.util.ArrayList;

public class Profile {
    private String userId;
    private ArrayList<BandoriCard> cards = null;
    private BandoriTeam team;

    public Profile(String userId, BandoriCards bandoriCards) {
        this.cards = new ArrayList<>();
        this.userId = userId;
        this.team = new BandoriTeam();
        loadSave(userId, bandoriCards);
    }

    public void addCards(BandoriCard bandoriCard) {
        this.cards.add(bandoriCard);
    }

    public void addCardToTeam(MessageReceivedEvent messageReceivedEvent, EventWaiter waiter) {
        this.team.addCardToTeam(messageReceivedEvent, cards, waiter);
        saveProfile();
    }
    public boolean loadSave (String userId, BandoriCards bandoriCards) {
        try {
            String line;
            String id, xp, level;
            if (new File(fileNameGetter(userId)).exists()) {
                //RandomAccessFile raf = new RandomAccessFile(fileNameGetter(), "r");
                File file = new File(fileNameGetter());
                FileInputStream fr = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(fr, "UTF-8"));
                while (((line = br.readLine()) != null && !line.equals("TEAM"))) {
                    id = line.substring(0, line.indexOf(" "));
                    line = line.substring(line.indexOf(" ") + 1);
                    xp = line.substring(0, line.indexOf(" "));
                    line = line.substring(line.indexOf(" ") + 1);
                    level = line;
                    for (BandoriCard card : bandoriCards.getBandoriCards()) {
                        if (card.getId() == Integer.parseInt(id)) {
                            card.setXp(Integer.parseInt(xp));
                            card.setLevel(Integer.parseInt(level));
                            cards.add(card);
                        }
                    }
                }
                while ((line = br.readLine()) != null) {
                    id = line.substring(0, line.indexOf(" "));
                    line = line.substring(line.indexOf(" ") + 1);
                    xp = line.substring(0, line.indexOf(" "));
                    line = line.substring(line.indexOf(" ") + 1);
                    level = line;
                    for (BandoriCard card : bandoriCards.getBandoriCards()) {
                        if (card.getId() == Integer.parseInt(id)) {
                            card.setXp(Integer.parseInt(xp));
                            card.setLevel(Integer.parseInt(level));
                            team.addCard(card);
                        }
                    }
                }
                br.close();
                fr.close();
            }
            else return false;
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }








    public void saveProfile() {
        try {
            makeNewSaveFile();
//            RandomAccessFile raf = new RandomAccessFile(fileNameGetter(), "rw");
            File file = new File(fileNameGetter());
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            String save = "";
            for (BandoriCard bandoriCard : cards) {
                save = save.concat(bandoriCard.getId() + " " + bandoriCard.getXp() + " " + bandoriCard.getLevel() + "\n");
            }
            save = save.concat("TEAM\n");
            if (team.getTeam() != null) {
                for (BandoriCard bandoriCard : team.getTeam()) {
                    if (bandoriCard != null)
                        save = save.concat(bandoriCard.getId() + " " + bandoriCard.getXp() + " " + bandoriCard.getLevel() + "\n");
                }
            }
            bw.write(save);
            bw.close();
            fos.close();
        }
        catch (IOException f) {
            f.printStackTrace();
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
        return fileNameGetter(this.userId);
    }

    private String fileNameGetter(String userId) {
        String directory = "./src/main/resources/profiles/";
        return directory + userId + ".txt";
    }
    public ArrayList<BandoriCard> getCards() {
        return this.cards;
    }

    public BandoriTeam getTeam() {
        return this.team;
    }

    public String getUserId() {
        return this.userId;
    }








}
