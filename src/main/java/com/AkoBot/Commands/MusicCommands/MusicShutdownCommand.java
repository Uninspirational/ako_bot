package com.AkoBot.Commands.MusicCommands;

import com.AkoBot.Music.MusicManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MusicShutdownCommand {
    private final Logger logger = LoggerFactory.getLogger(MusicShutdownCommand.class);
    public void shutDownMusic(MusicManager musicManager) {
        try {
            musicManager.getManager().shutdown();
        }
        catch (Exception e) {
            logger.debug("Unable to shutdown music" + e);
        }

    }
}
