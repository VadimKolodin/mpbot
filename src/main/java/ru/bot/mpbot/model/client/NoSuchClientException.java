package ru.bot.mpbot.model.client;

public class NoSuchClientException extends NullPointerException{
    public NoSuchClientException(){}
    public NoSuchClientException(String msg){
        super(msg);
    }
}
