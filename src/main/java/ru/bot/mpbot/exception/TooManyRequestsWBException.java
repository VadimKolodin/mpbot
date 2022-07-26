package ru.bot.mpbot.exception;

public class TooManyRequestsWBException extends IllegalArgumentException{
    public TooManyRequestsWBException(){

    }
    public TooManyRequestsWBException(String msg){
        super(msg);
    }
}