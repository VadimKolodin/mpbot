package ru.bot.mpbot.telegram.commands;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import ru.bot.mpbot.marketing.AdvertMaker;

public abstract class BotMediaCommand extends BotCommand{

    protected SendPhoto mediaAnswer;

    public SendPhoto getMediaAnswer(){
        if (mediaAnswer!=null) {
            SendPhoto temp = new SendPhoto(mediaAnswer.getChatId(), mediaAnswer.getPhoto());
            temp.setCaption(mediaAnswer.getCaption()
                    +"\n———————————————\n _Рекламная интеграция_\n\n"
                    + AdvertMaker.getRandomAdvert());
            temp.setParseMode("Markdown");
            temp.setReplyMarkup(mediaAnswer.getReplyMarkup());
            return temp;
        }
        return mediaAnswer;
    }
}
