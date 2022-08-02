package ru.bot.mpbot.telegram.commands;

import org.apache.http.client.HttpResponseException;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.bot.mpbot.marketing.AdvertMaker;

import java.io.IOException;

public abstract class BotCommand {

    private boolean isExecuted = false;

    protected SendMessage answer;

    public SendMessage getAnswer(){
        if (answer!=null){
            SendMessage temp = new SendMessage(
                    answer.getChatId(),
                    answer.getText()+"\n=+=+=+=+=Реклама=+=+=+=+=\n" +AdvertMaker.getRandomAdvert());
            temp.enableMarkdown(true);
            return temp;
        }
        return answer;
    }

    public void execute() throws IOException {
        isExecuted = true;
    }

    public boolean isExecuted() {
        return isExecuted;
    }

}
