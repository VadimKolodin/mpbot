package ru.bot.mpbot.exception;

public class ServerDownWBException extends IllegalArgumentException{
    public ServerDownWBException(){

    }
    public ServerDownWBException(String msg){
        super(msg);
    }
}