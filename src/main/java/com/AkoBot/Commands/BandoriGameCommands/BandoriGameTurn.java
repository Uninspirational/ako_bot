package com.AkoBot.Commands.BandoriGameCommands;

import com.AkoBot.Bandori.BandoriTeam;

public class BandoriGameTurn {
    private int votes1 = 0;
    private int votes2 = 0;
    private int winner = -1;
    private int loser = -1;
    private String message = "";
    private BandoriTeam team1;
    private BandoriTeam team2;

    public BandoriGameTurn(BandoriTeam team1, BandoriTeam team2) {
        this.team1 = team1;
        this.team2 = team2;
    }

    public void runTurn(int card1, int card2) {
        int performance1 = this.team1.getStat(card1, 0);
        int performance2 = this.team2.getStat(card2, 0);

        int technique1 = this.team1.getStat(card1, 1);
        int technique2 = this.team2.getStat(card2, 1);

        int visual1 = this.team1.getStat(card1, 2);
        int visual2 = this.team2.getStat(card2, 2);

        int statTotal1 = performance1 + technique1 + visual1;
        int statTotal2 = performance2 + technique2 + visual2;
        //if false 1 is winner, if true 2 is winner
        boolean performance = performance1 < performance2;
        boolean technique = technique1 < technique2;
        boolean visual = visual1 < visual2;

        int wins1 = 0;
        int wins2 = 0;

        if (performance)
            wins1++;
        else
            wins2++;

        if (technique)
            wins1++;
        else
            wins2++;

        if (visual)
            wins1++;
        else
            wins2++;

        if (wins1 > 1) {
            this.winner = 1;
            this.loser = 2;
        }

        if (wins2 > 1) {
            this.winner = 2;
            this.loser = 1;
        }

        this.votes1 = 1000 * (int) (statTotal1/ (double) (statTotal1 + statTotal2));
        this.votes2 = 1000 * (int) (statTotal2 / (double) (statTotal1 + statTotal2));

        if (wins1 >= 2)
            this.votes1 += 200;
        if (wins1 == 3)
            this.votes1 += 100;
        if (wins2 >= 2)
            this.votes2 += 200;
        if (wins2 == 3)
            this.votes2 += 100;

        //TODO
        //add skill bonuses

        this.message += "Performance: " + performance1 + "\t\t" + "Performance: " + performance2 + "\n";
        this.message += "Technique: " + technique1 + "\t\t" + "Technique: " + technique2 + "\n";
        this.message += "Visual: " + visual1 + "\t\t" + "Visual: " + visual2 + "\n";
    }

    public int getVotes1() {
        return votes1;
    }

    public int getVotes2() {
        return votes2;
    }

    public int getWinner() {
        return winner;
    }

    public int getLoser() {
        return loser;
    }
}
