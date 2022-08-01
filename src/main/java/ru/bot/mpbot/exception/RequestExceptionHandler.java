package ru.bot.mpbot.exception;

import ru.bot.mpbot.telegram.constants.ErrorConst;

import java.io.IOException;

public class RequestExceptionHandler {
    public String handle(IOException e){
        if (e instanceof NotAuthorizedOzonException){
            return ErrorConst.WRONG_CREDENTIALS_OZON.getMessage();
        } else if (e instanceof NotAuthorizedWBException){
            return ErrorConst.WRONG_CREDENTIALS_WB.getMessage();
        } else if (e instanceof ServerDownOzonException){
            return ErrorConst.OZON_ERROR.getMessage();
        } else if (e instanceof ServerDownWBException){
            return ErrorConst.WB_ERROR.getMessage();
        } else if (e instanceof  TooManyRequestsOzonException){
            return ErrorConst.TOO_MANY_REQ_OZON.getMessage();
        } else if (e instanceof TooManyRequestsWBException){
            return ErrorConst.TOO_MANY_REQ_WB.getMessage();
        } else {
            return ErrorConst.INTERNAL_ERROR.getMessage();
        }
    }
}
