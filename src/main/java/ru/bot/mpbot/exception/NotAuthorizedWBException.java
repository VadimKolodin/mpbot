package ru.bot.mpbot.exception;

public class NotAuthorizedWBException extends IllegalArgumentException{
    public NotAuthorizedWBException(){

    }
    public NotAuthorizedWBException(String msg){
        super(msg);
    }
}