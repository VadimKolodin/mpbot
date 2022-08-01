package ru.bot.mpbot.exception;

import java.io.IOException;

public class TooManyRequestsOzonException extends IOException {
    public TooManyRequestsOzonException(){

    }
    public TooManyRequestsOzonException(String msg){
        super(msg);
    }
}