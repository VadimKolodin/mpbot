package ru.bot.mpbot.telegram.commands.callbackquery;

import org.apache.http.client.fluent.Response;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.bot.mpbot.SpringContext;
import ru.bot.mpbot.model.client.Client;
import ru.bot.mpbot.requests.Connector;
import ru.bot.mpbot.model.client.ClientService;
import ru.bot.mpbot.telegram.commands.BotCommand;
import ru.bot.mpbot.telegram.constants.ErrorConst;
import ru.bot.mpbot.telegram.constants.MessageConst;

import java.io.IOException;
import java.util.HashMap;

public class CheckWBCommand  extends BotCommand {

    private Long chatId;

    private static final String URI = "https://suppliers-api.wildberries.ru/api/v2/supplies?status=ACTIVE";


    public CheckWBCommand(Long chatId){
        this.chatId=chatId;
    }

    @Override
    public void execute(){
        ClientService clientService = SpringContext.getBean(ClientService.class);
        Client client = clientService.getClientByTgId(chatId);
        if (client==null){
            this.answer = new SendMessage(chatId.toString(), ErrorConst.NO_WB_KEY.getMessage());
            super.execute();
            return;
        }
        if (client.getWbKey()==null){
            this.answer = new SendMessage(chatId.toString(), ErrorConst.NO_WB_KEY.getMessage());
            super.execute();
            return;
        }

        HashMap<String, String> headers = new HashMap<>(2, 1);
        headers.put("Authorization", client.getWbKey());
        try {
            Response response = Connector.getRequest(URI, headers);
            int statusCode = response.returnResponse().getStatusLine().getStatusCode();
            switch (statusCode){
                case 200:
                    this.answer=new SendMessage(chatId.toString(), MessageConst.CHECK_WB_SUCCESS.getMessage());
                    break;
                case 401:
                case 403:
                    this.answer=new SendMessage(chatId.toString(), ErrorConst.WRONG_WB_INFO.getMessage());
                    break;
                default:
                    this.answer=new SendMessage(chatId.toString(), ErrorConst.WB_ERROR.getMessage());
                    break;
            }
        }catch (IOException |NullPointerException e){
            this.answer=new SendMessage(chatId.toString(), ErrorConst.INTERNAL_ERROR.getMessage());
        }
        super.execute();
    }
}
