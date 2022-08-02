package ru.bot.mpbot.telegram.commands;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

public abstract class BotMediaCommand extends BotCommand{

    protected SendPhoto mediaAnswer;

    public SendPhoto getMediaAnswer(){
        if (mediaAnswer!=null) {
            SendPhoto temp = new SendPhoto(mediaAnswer.getChatId(), mediaAnswer.getPhoto());
            temp.setCaption(mediaAnswer.getCaption());
            temp.setParseMode("Markdown");
            return temp;
        }
        return mediaAnswer;
    }
}
