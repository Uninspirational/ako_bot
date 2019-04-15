package com.AkoBot.Bandori;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class BandoriTeam {
    //actual team
    private ArrayList<BandoriCard> team;
    //temporary team for running game
    private ArrayList<BandoriCard> gameTeam;
    private int size;
    public BandoriTeam() {
        this.team = new ArrayList<>();
        this.size = 0;
    }
    public void addCardToTeam(MessageReceivedEvent messageReceivedEvent, ArrayList<BandoriCard> cards, EventWaiter waiter) {
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        StringBuilder currentTeam = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            System.out.println(i + " >= " + this.size);

            if (i >= this.size)
                currentTeam.append("[").append(i + 1).append("] - none\n");
            else {
                currentTeam.append("[").append(i + 1).append("] - ").append(team.get(i).getName()).append(" *(").append(team.get(i).getId()).append(")*\n");
            }
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.addField(currentTeam.toString(), "", false);
        currentTeam = new StringBuilder("Which member would you like to replace? Type the number or type $cancel");
        embedBuilder.addField(currentTeam.toString(), "", false);
        textChannel.sendMessage(embedBuilder.build()).queue();
        waitForOldCard(messageReceivedEvent.getAuthor(), textChannel, waiter, cards);
    }
    public void viewTeam(MessageReceivedEvent messageReceivedEvent) {
        TextChannel textChannel = messageReceivedEvent.getTextChannel();
        StringBuilder currentTeam = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (i >= this.size)
                currentTeam.append("[").append(i + 1).append("] - none\n");
            else currentTeam.append("[").append(i + 1).append("] - ").append(team.get(i).getName()).append(" *(").append(team.get(i).getId()).append(")*\n");
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.addField("Your current band", currentTeam.toString(), false);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    public void addCard(BandoriCard bandoriCard) {
        team.add(bandoriCard);
        this.size++;
    }
    public boolean isFull() {
        return this.size == 5;
    }
    private void waitForOldCard(User user, TextChannel channel, EventWaiter waiter, ArrayList<BandoriCard> cards) {
        waiter.waitForEvent(GuildMessageReceivedEvent.class, e -> e.getChannel().equals(channel) && e.getAuthor().equals(user), e -> {
            TextChannel textChannel = e.getChannel();
            String message = e.getMessage().getContentStripped().toLowerCase();
            if (message.equals("$cancel")) {
                textChannel.sendMessage("Cancelled").queue();
            }
            else {
                try {
                    int oldCard = Integer.parseInt(message) - 1;
                    waitForNewCard(user, textChannel, waiter, oldCard, cards);
                } catch (Exception f) {
                    textChannel.sendMessage("Please enter the corresponding number of the card you would like to replace or enter $cancel").queue();
                    this.waitForOldCard(user, channel, waiter, cards);
                }
            }
        }, 25, TimeUnit.SECONDS, () -> channel.sendMessage("Timed out").queue());

    }
    private void waitForNewCard(User user, TextChannel channel, EventWaiter waiter, final int oldCard, ArrayList<BandoriCard> cards) {
        channel.sendMessage("Please enter the number of the card you would like to add to your team").queue();
        waiter.waitForEvent(GuildMessageReceivedEvent.class, e -> e.getChannel().equals(channel) && e.getAuthor().equals(user), e -> {
            TextChannel textChannel = e.getChannel();
            String message = e.getMessage().getContentStripped().toLowerCase();
            if (message.equals("$cancel")) {
                textChannel.sendMessage("Cancelled").queue();
            }
            else {
                try {
                    int newCard = Integer.parseInt(message) - 1;
                    if (checkDuplicate(cards.get(newCard))) {
                        textChannel.sendMessage("This card is already in your band!").queue();
                    } else if (replaceCard(newCard, oldCard, cards))
                        textChannel.sendMessage("Successfully added card to band").queue();
                    else textChannel.sendMessage("Failed to add card to band").queue();
                } catch (Exception f) {
                    textChannel.sendMessage("Please enter the corresponding number for the new card you would like to add or enter $cancel").queue();
                    this.waitForNewCard(user, channel, waiter, oldCard, cards);
                }
            }
        }, 25, TimeUnit.SECONDS, () -> channel.sendMessage("Timed out").queue());
    }
    private boolean replaceCard(int newCard, int oldCard, ArrayList<BandoriCard> cards) {
        try {
            this.size = 0;
            BandoriCard[] cardsArray = new BandoriCard[5];
            for (int i = 0; i < this.team.size(); i++) {
                cardsArray[i] = this.team.get(i);
                if (cardsArray[i] != null)
                    size++;
            }
            if (cardsArray[oldCard] == null)
                size++;
            cardsArray[oldCard] = cards.get(newCard);
            this.team.clear();
            this.team.addAll(Arrays.asList(cardsArray));
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    public ArrayList<BandoriCard> getTeam() {
        return this.team;
    }

    /**
     *
     * @param check new card to be added to team
     * @return true if duplicate exists, false otherwise
     */
    private boolean checkDuplicate(BandoriCard check) {
        for (BandoriCard card : this.team) {
            if (check.getId() == card.getId())
                return true;
        }
        return false;
    }
    public String getString() {
        StringBuilder string = new StringBuilder();
        for (BandoriCard card : this.getTeam()) {
            string.append(card.getCardDescription()).append("\n");
        }
        return string.toString();
    }
    public int getStat(int card, int stat) {
        //0 = performance, 1 = technique, 2 = visual
        BandoriCard bandoriCard = this.gameTeam.remove(card);
        BandoriTypes bandoriTypes = new BandoriTypes();
        BandType type = bandoriTypes.getBandType(this.team.get(card).getBandoriMember().getI_band());

        double multiplier = 1.0;

        if (isHappy3())
            multiplier *= 1.2;
        if (isPure3())
            multiplier *= 1.1;
        if (isHappy5())
            multiplier *= 1.1;
        if (isPure5())
            multiplier *= 1.05;

        double cameoBonus = 0.0;

        if (isAfterglow2())
            cameoBonus += 0.05 * this.team.get(card).getCameos(type);
        if (isHellohappy2())
            cameoBonus += 0.05 * this.team.get(card).getCameos(type);
        if (isPasupare2())
            cameoBonus += 0.05 * this.team.get(card).getCameos(type);
        if (isPpp2())
            cameoBonus += 0.05 * this.team.get(card).getCameos(type);
        if (isRas2())
            cameoBonus += 0.05 * this.team.get(card).getCameos(type);
        if (isRoselia2())
            cameoBonus += 0.05 * this.team.get(card).getCameos(type);

        if (isAfterglow5() && type == BandType.AFTERGLOW)
            multiplier *= 1.1;
        if (isHellohappy5() && type == BandType.HELLOHAPPY)
            multiplier *= 1.1;
        if (isPasupare5() && type == BandType.PASUPARE)
            multiplier *= 1.1;
        if (isRoselia5() && type == BandType.ROSELIA)
            multiplier *= 1.1;
        if (isPpp5() && type == BandType.POPIPA)
            multiplier *= 1.1;
        if (isRas5() && type == BandType.RAS)
            multiplier *= 1.1;

        if (isDiffBand()) {
            for (BandoriCard temp : this.team) {
                if (temp.searchCameo(this.team.get(card)))
                    cameoBonus += 0.15;
            }
            multiplier *= cameoBonus;
        }

        if (stat == 0)
            return (int) (bandoriCard.getPerformance() * multiplier);
        else if (stat == 1)
            return (int) (bandoriCard.getTechnique() * multiplier);
        else
            return (int) (bandoriCard.getVisual() * multiplier);
    }
    public String checkSynergy() {
        //instrument, band, skills, type, 3 stats, trained
        //cool, happy, power, pure
        int[] attribute = new int[4];
        //Afterglow, HelloHappy, Pasupare, PPP, Ras, Roselia
        int[] band = new int[5];
        //bass, dj, drums, guitar, guitar & vocals, keyboard, vocal
        int[] instrument = new int[7];

        for (BandoriCard card : getTeam()) {
            attribute[card.getAttributeInt()]++;
            band[card.getBandoriMember().getId()]++;
            instrument[card.getBandoriMember().getInstrumentInt()]++;
        }
        String teamSynergy = "";
        this.gameTeam = this.team;
        //attribute bonuses
        //x3 bonuses
        if (attribute[0] == 3) {
            this.cool3 = true;
            teamSynergy += "Cool x 3: Technique increases by 20%";
        }
        if (attribute[1] == 3) {
            this.happy3 = true;
            teamSynergy += "Happy x 3: Visual increases by 20%";
        }
        if (attribute[2] == 3) {
            this.power3 = true;
            teamSynergy += "Power x 3: Performance increases by 20%";
        }
        if (attribute[3] == 3) {
            this.pure3 = true;
            teamSynergy += "Pure x 3: All stats increase by 10%";
        }
        //x5 bonuses
        if (attribute[0] == 5) {
            this.cool5 = true;
            teamSynergy += "Cool x 5: Technique increases by an additional 10%";
        }
        if (attribute[1] == 5) {
            this.happy5 = true;
            teamSynergy += "Happy x 5: Visual increases by an additional 10%";
        }
        if (attribute[2] == 5) {
            this.power5 = true;
            teamSynergy += "Power x 5: Performance increases by an additional 10%";
        }
        if (attribute[3] == 5) {
            this.pure5 = true;
            teamSynergy += "Pure x 5: All stats increase by an additional 5%";
        }
        //all bonuses
        if (attribute[0] != 0 && attribute[1] != 0 && attribute[2] != 0 && attribute[3] != 0 && attribute[3] != 0) {
            this.allatts = true;
            teamSynergy += "Every attribute: Skill bonuses increased by 25%";
        }

        //band bonuses
        //x2 bonus
        if (band[0] == 2) {
            this.afterglow2 = true;
            teamSynergy += "Afterglow x 2: All stats increase by 5% for Afterglow cameos";
        }
        if (band[1] == 2) {
            this.hellohappy2 = true;
            teamSynergy += "HelloHappy x 2: All stats increase by 5% for Hello, Happy World cameos";
        }
        if (band[2] == 2) {
            this.pasupare2 = true;
            teamSynergy += "Pastel*Palettes x 2: All stats increase by 5% for Pastel*Palettes cameos";
        }
        if (band[3] == 2) {
            this.ppp2 = true;
            teamSynergy += "Popipa x 2: All stats increase by 5% for Poppin'Party cameos";
        }
        if (band[4] == 2) {
            this.ras2 = true;
            teamSynergy += "RAS x 2: All stats increase by 5% for Raise a Suilen cameos";
        }
        if (band[5] == 2) {
            this.roselia2 = true;
            teamSynergy += "Roselia x 2: All stats increase by 5% for Roselia cameos";
        }

        //x5 bonus
        if (band[0] == 5) {
            this.afterglow5 = true;
            teamSynergy += "Afterglow x 5: All stats increase by 10% for Afterglow cards";
        }
        if (band[1] == 5) {
            this.hellohappy5 = true;
            teamSynergy += "HelloHappy x 5: All stats increase by 10% for Hello, Happy World cards";
        }
        if (band[2] == 5) {
            this.pasupare5 = true;
            teamSynergy += "Pastel*Palettes x 5: All stats increase by 10% for Pastel*Palettes cards";
        }
        if (band[3] == 5) {
            this.ppp5 = true;
            teamSynergy += "Popipa x 5: All stats increase by 10% for Poppin'Party cards";
        }
        if (band[4] == 5) {
            this.ras5 = true;
            teamSynergy += "RAS x 5: All stats increase by 10% for Raise a Suilen cards";
        }
        if (band[5] == 5) {
            this.roselia5 = true;
            teamSynergy += "Roselia x 5: All stats increase by 10% for Roselia cards";
        }

        if (band[0] <= 1 && band[1] <= 1 && band[2] <= 1 && band[3] <= 1 && band[4] <= 1 && band[5] <= 1) {
            this.diffBand = true;
            teamSynergy += "Different band: All stats increase by 15% for all cameos";
        }
        //instrument bonuses
        if (instrument[0] == instrument[2] && (instrument[3] == instrument[5] || instrument[4] == instrument[5]) && instrument[0] == instrument[5]) {
            this.fullBand = true;
            teamSynergy += "Full Band: Skill bonuses increased by 25%";
        }
        if (instrument[1] == 1) {
            this.djBonus = true;
            teamSynergy += "DJ x 1: Skill bonus for DJ increased by 500%";
        }
        return teamSynergy;
    }

    private boolean cool3 = false;
    private boolean happy3 = false;
    private boolean power3 = false;
    private boolean pure3 = false;

    private boolean cool5 = false;
    private boolean happy5 = false;
    private boolean power5 = false;
    private boolean pure5 = false;

    private boolean allatts = false;

    private boolean afterglow2 = false;
    private boolean hellohappy2 = false;
    private boolean pasupare2 = false;
    private boolean ppp2 = false;
    private boolean ras2 = false;
    private boolean roselia2 = false;

    private boolean afterglow5 = false;
    private boolean hellohappy5 = false;
    private boolean pasupare5 = false;
    private boolean ppp5 = false;
    private boolean ras5 = false;
    private boolean roselia5 = false;

    private boolean diffBand = false;

    private boolean fullBand = false;
    private boolean djBonus = false;

    public boolean isAfterglow2() {
        return afterglow2;
    }

    public boolean isAfterglow5() {
        return afterglow5;
    }

    public boolean isDjBonus() {
        return djBonus;
    }

    public boolean isFullBand() {
        return fullBand;
    }

    public boolean isDiffBand() {
        return diffBand;
    }

    public boolean isRoselia5() {
        return roselia5;
    }

    public boolean isRas5() {
        return ras5;
    }

    public boolean isPpp5() {
        return ppp5;
    }

    public boolean isPasupare5() {
        return pasupare5;
    }

    public boolean isHellohappy5() {
        return hellohappy5;
    }

    public boolean isRoselia2() {
        return roselia2;
    }

    public boolean isRas2() {
        return ras2;
    }

    public boolean isPpp2() {
        return ppp2;
    }

    public boolean isPasupare2() {
        return pasupare2;
    }

    public boolean isHellohappy2() {
        return hellohappy2;
    }

    public boolean isAllatts() {
        return allatts;
    }

    public boolean isPure5() {
        return pure5;
    }

    public boolean isHappy5() {
        return happy5;
    }

    public boolean isPower5() {
        return power5;
    }

    public boolean isCool5() {
        return cool5;
    }

    public boolean isPower3() {
        return power3;
    }

    public boolean isHappy3() {
        return happy3;
    }

    public boolean isCool3() {
        return cool3;
    }

    public boolean isPure3() {
        return pure3;
    }
}
