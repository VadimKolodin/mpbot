package ru.bot.mpbot.telegram.commands.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.bot.mpbot.SpringContext;
import ru.bot.mpbot.messaging.MessagingService;
import ru.bot.mpbot.model.client.Client;
import ru.bot.mpbot.model.client.ClientService;
import ru.bot.mpbot.telegram.commands.BotCommand;
import ru.bot.mpbot.telegram.constants.Colors;
import ru.bot.mpbot.telegram.constants.MessageConst;

import java.io.IOException;
import java.time.LocalDate;

public class SetOzonKeyCommand extends BotCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final Long chatId;
    private final String username;
    private final String oznKey;



    public SetOzonKeyCommand(Long chatId, String username, String oznKey){
        this.chatId=chatId;
        this.username = username;
        this.oznKey=oznKey;
    }

    @Override
    public void execute() throws IOException {
        LOGGER.info("Started execution");
        ClientService clientService = SpringContext.getBean(ClientService.class);
        Client client = clientService.getClientByTgId(chatId);
        if (client==null){
            LOGGER.info(Colors.GREEN.get()+"Registering new user: "+chatId+Colors.RESET.get());
            clientService.createClient(new Client(chatId, oznKey,
                    null, null, username, false, LocalDate.now(), LocalDate.now()));
            this.answer = new SendMessage(chatId.toString(),
                            MessageConst.OZN_API_KEY_SET.getMessage());
        } else {
            clientService.updateClientOznKey(chatId, oznKey);
            clientService.updateUsage(chatId);
            if (client.isNotificationEnabled()){
                MessagingService messagingService = SpringContext.getBean(MessagingService.class);
                messagingService.sendUpdateClient(client);
            }
            this.answer = new SendMessage(chatId.toString(),
                    client.getOznId()==null?
                            MessageConst.OZN_API_KEY_SET.getMessage():
                            MessageConst.OZN_API_KEY_SET_COMPLETE.getMessage());
        }

        super.execute();

        LOGGER.info("Execution complete");
    }


}
