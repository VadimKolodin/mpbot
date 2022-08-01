package ru.bot.mpbot.exception;

import java.io.IOException;

public class NotAuthorizedOzonException extends IOException {
    public NotAuthorizedOzonException(){

    }
    public NotAuthorizedOzonException(String msg){
        super(msg);
    }
}
