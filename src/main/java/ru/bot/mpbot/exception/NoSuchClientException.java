package ru.bot.mpbot.exception;

public class NoSuchClientException extends NullPointerException{
    public NoSuchClientException(){}
    public NoSuchClientException(String msg){
        super(msg);
    }
}
