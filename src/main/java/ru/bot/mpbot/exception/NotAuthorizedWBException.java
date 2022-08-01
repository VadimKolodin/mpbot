package ru.bot.mpbot.exception;

import java.io.IOException;

public class NotAuthorizedWBException extends IOException {
    public NotAuthorizedWBException(){

    }
    public NotAuthorizedWBException(String msg){
        super(msg);
    }
}