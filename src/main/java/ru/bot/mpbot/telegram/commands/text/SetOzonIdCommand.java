package ru.bot.mpbot.telegram.commands.text;

import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.bot.mpbot.SpringContext;
import ru.bot.mpbot.entity.Client;
import ru.bot.mpbot.service.ClientService;
import ru.bot.mpbot.telegram.commands.BotCommand;
import ru.bot.mpbot.telegram.constants.Colors;
import ru.bot.mpbot.telegram.constants.MessageConst;

import java.time.LocalDate;

public class SetOzonIdCommand extends BotCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final Long chatId;

    private final String oznId;



    public SetOzonIdCommand(Long chatId, String oznId){
        this.chatId=chatId;
        this.oznId=oznId;
    }

    @Override
    public void execute(){
        LOGGER.info("Started execution");
        ClientService clientService = SpringContext.getBean(ClientService.class);
        Client client = clientService.getClientByTgId(chatId);
        if (client==null){
            LOGGER.info(Colors.GREEN.get()+"Registering new user: "+chatId+Colors.RESET.get());
            clientService.createClient(new Client(chatId, null,
                    null, oznId, LocalDate.now(), LocalDate.now()));
            this.answer = new SendMessage(chatId.toString(),
                            MessageConst.OZN_CLIENT_ID_SET.getMessage());
        } else {
            clientService.updateClientOznId(chatId, oznId);
            clientService.updateUsage(chatId);
            this.answer = new SendMessage(chatId.toString(),
                    client.getOznId()==null?
                            MessageConst.OZN_CLIENT_ID_SET.getMessage():
                            MessageConst.OZN_CLIENT_ID_SET_COMPLETE.getMessage());
        }

        super.execute();
        LOGGER.info("Execution complete");
    }


}
