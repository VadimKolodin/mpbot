package ru.bot.mpbot.telegram.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.bot.mpbot.model.client.Client;
import ru.bot.mpbot.telegram.constants.ErrorConst;

public class ClientValidator {

    private final Client client;

    public ClientValidator(Client client) {
        this.client = client;
    }

    /**
     * Checks if client has apikey and clientid
     * @return true - if client has apikey and clientID, false if not
     */
    public boolean validateOzon(){
        return client.getOznKey()!=null&&client.getOznId()!=null;
    }
    /**
     * Checks if client has apikey
     * @return true - if client has apikey, false if not
     */
    public boolean validateWB(){
        return client.getWbKey()!=null;
    }
    /**
     * Returns msg that shows what client still doesn`t have
     * @return msg
     */
    public String getErrorMessageOzon(){
        if (client.getOznId() == null && client.getOznKey() == null) {
            return ErrorConst.NO_OZN_INFO.getMessage();
        } else {
            if (client.getOznId() == null) {
                return ErrorConst.NO_OZN_ID.getMessage();
            } else {
                return ErrorConst.NO_OZN_KEY.getMessage();
            }
        }
    }
    /**
     * Returns msg that shows what client still doesn`t have
     * @return msg
     */
    public String getErrorMessageWB(){
        return ErrorConst.NO_WB_KEY.getMessage();
    }
}
