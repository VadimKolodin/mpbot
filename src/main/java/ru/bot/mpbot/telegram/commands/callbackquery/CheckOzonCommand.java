package ru.bot.mpbot.telegram.commands.callbackquery;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Response;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.bot.mpbot.SpringContext;
import ru.bot.mpbot.entity.Client;
import ru.bot.mpbot.requests.Connector;
import ru.bot.mpbot.service.ClientService;
import ru.bot.mpbot.telegram.commands.BotCommand;
import ru.bot.mpbot.telegram.constants.ErrorConst;
import ru.bot.mpbot.telegram.constants.MessageConst;

import java.io.IOException;
import java.util.HashMap;

public class CheckOzonCommand extends BotCommand {

    private Long chatId;

    private static final String URI = "https://api-seller.ozon.ru/v2/category/tree";
    private static final String BODY = "{"
            +"\"category_id\": 17034410,"
            +"\"language\": \"DEFAULT\""
            +"}";

    public CheckOzonCommand(Long chatId){
        this.chatId=chatId;
    }

    @Override
    public void execute()  {
        ClientService clientService = SpringContext.getBean(ClientService.class);
        Client client = clientService.getClientByTgId(chatId);
        if (client==null||(client.getOznKey()==null&&client.getOznId()==null)){
            this.answer = new SendMessage(chatId.toString(), ErrorConst.NO_OZN_INFO.getMessage());
            super.execute();
            return;
        } 
        if (client.getOznId()==null){
            this.answer = new SendMessage(chatId.toString(), ErrorConst.NO_OZN_ID.getMessage());
            super.execute();
            return;
        }
        if (client.getOznKey()==null){
            this.answer = new SendMessage(chatId.toString(), ErrorConst.NO_OZN_KEY.getMessage());
            super.execute();
            return;
        }

        HashMap<String, String> headers = new HashMap<>(3, 1);
        headers.put("Client-Id", client.getOznId());
        headers.put("Api-Key", client.getOznKey());
        try {
            Response response = Connector.postRequest(URI, headers, BODY);
            int statusCode = response.returnResponse().getStatusLine().getStatusCode();
            switch (statusCode){
                case 200:
                    this.answer=new SendMessage(chatId.toString(), MessageConst.CHECK_OZON_SUCCESS.getMessage());
                    break;
                case 401:
                case 403:
                    this.answer=new SendMessage(chatId.toString(), ErrorConst.WRONG_OZN_INFO.getMessage());
                    break;
                default:
                    this.answer=new SendMessage(chatId.toString(), ErrorConst.OZON_ERROR.getMessage());
                    break;
            }
        }catch (IOException|NullPointerException e){
            this.answer=new SendMessage(chatId.toString(), ErrorConst.INTERNAL_ERROR.getMessage());
        }
        super.execute();
    }
}
