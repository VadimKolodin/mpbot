package ru.bot.mpbot.exception;

public class NotAuthorizedOzonException extends IllegalArgumentException{
    public NotAuthorizedOzonException(){

    }
    public NotAuthorizedOzonException(String msg){
        super(msg);
    }
}
