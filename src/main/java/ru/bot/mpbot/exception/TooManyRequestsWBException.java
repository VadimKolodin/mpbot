package ru.bot.mpbot.exception;

import java.io.IOException;

public class TooManyRequestsWBException extends IOException {
    public TooManyRequestsWBException(){

    }
    public TooManyRequestsWBException(String msg){
        super(msg);
    }
}