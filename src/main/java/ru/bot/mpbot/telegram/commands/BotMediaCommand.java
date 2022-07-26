package ru.bot.mpbot.telegram.commands;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

public abstract class BotMediaCommand extends BotCommand{

    protected SendPhoto mediaAnswer;

    public SendPhoto getMediaAnswer(){
        if (mediaAnswer!=null) {
            mediaAnswer.setParseMode("Markdown");
        }
        return mediaAnswer;
    }
}
