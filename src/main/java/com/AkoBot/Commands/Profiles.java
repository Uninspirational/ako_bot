package com.AkoBot.Commands;

import java.util.ArrayList;

public class Profiles {
    private ArrayList<Profile> profiles;
    public Profiles () {
        this.profiles = new ArrayList<>();
    }
    public Profile getProfile(String id) {
        for (Profile profile : this.profiles) {
            if (profile.getUserId().equals(id)) {
                return profile;
            }
        }
        return new Profile(id);
    }

    public void addProfile(Profile profile) {
        this.profiles.add(profile);
    }
    public void saveAll() {
        for (Profile profile : this.profiles) {
            profile.saveProfile();
        }
    }
}
