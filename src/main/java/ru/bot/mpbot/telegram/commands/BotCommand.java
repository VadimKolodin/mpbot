package ru.bot.mpbot.telegram.commands;

import org.apache.http.client.HttpResponseException;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public abstract class BotCommand {

    private boolean isExecuted = false;

    protected SendMessage answer;

    public SendMessage getAnswer(){
        if (answer!=null){
            answer.enableMarkdown(true);
        }
        return answer;
    }

    public void execute() {
        isExecuted = true;
    }

    public boolean isExecuted() {
        return isExecuted;
    }

}
