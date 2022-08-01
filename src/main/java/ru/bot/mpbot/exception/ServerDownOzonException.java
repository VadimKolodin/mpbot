package ru.bot.mpbot.exception;

import java.io.IOException;

public class ServerDownOzonException extends IOException {
    public ServerDownOzonException(){

    }
    public ServerDownOzonException(String msg){
        super(msg);
    }
}