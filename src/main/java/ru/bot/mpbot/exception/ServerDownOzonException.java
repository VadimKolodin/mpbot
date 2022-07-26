package ru.bot.mpbot.exception;

public class ServerDownOzonException extends IllegalArgumentException{
    public ServerDownOzonException(){

    }
    public ServerDownOzonException(String msg){
        super(msg);
    }
}