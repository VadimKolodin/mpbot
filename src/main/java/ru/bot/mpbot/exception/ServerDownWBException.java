package ru.bot.mpbot.exception;

import java.io.IOException;

public class ServerDownWBException extends IOException {
    public ServerDownWBException(){

    }
    public ServerDownWBException(String msg){
        super(msg);
    }
}