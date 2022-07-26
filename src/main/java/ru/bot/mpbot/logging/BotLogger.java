package ru.bot.mpbot.logging;

public class BotLogger {
    private static final BotLogger LOGGER = new BotLogger();
    private BotLogger(){

    }
    public static BotLogger getLogger(){
        return LOGGER;
    }
}
