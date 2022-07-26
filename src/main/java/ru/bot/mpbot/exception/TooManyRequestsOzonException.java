package ru.bot.mpbot.exception;

public class TooManyRequestsOzonException extends IllegalArgumentException{
    public TooManyRequestsOzonException(){

    }
    public TooManyRequestsOzonException(String msg){
        super(msg);
    }
}