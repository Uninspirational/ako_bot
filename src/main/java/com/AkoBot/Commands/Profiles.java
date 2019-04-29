package com.AkoBot.Commands;

import com.AkoBot.Bandori.BandoriCards;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class Profiles {
    private ArrayList<Profile> profiles;
    public Profiles () {
        this.profiles = new ArrayList<>();
    }
    public Profile getProfile(String id, BandoriCards bandoriCards) {
        for (Profile profile : this.profiles) {
            if (profile.getUserId().equals(id)) {
                return profile;
            }
        }
        return addProfile(new Profile(id, bandoriCards));
    }

    private Profile addProfile(Profile profile) {
        this.profiles.add(profile);
        return profile;
    }
    public void saveAll() {
        for (Profile profile : this.profiles) {
            System.out.println(profile.getUserId());
            profile.saveProfile();
        }
    }
}
