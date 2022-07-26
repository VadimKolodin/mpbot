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

public class SetWBKeyCommand extends BotCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final Long chatId;
    private final String username;
    private final String wbKey;



    public SetWBKeyCommand(Long chatId, String username, String wbKey){
        this.chatId=chatId;
        this.username = username;
        this.wbKey=wbKey;
    }

    @Override
    public void execute() throws IOException {
        LOGGER.info("Started execution");
        ClientService clientService = SpringContext.getBean(ClientService.class);
        Client client = clientService.getClientByTgId(chatId);
        if (client==null){
            LOGGER.info(Colors.GREEN.get()+"Registering new user: "+chatId+Colors.RESET.get());
            clientService.createClient(new Client(chatId, null,
                    wbKey, null, username, false,  LocalDate.now(), LocalDate.now()));

        } else {
            clientService.updateClientWBKey(chatId, wbKey);
            clientService.updateUsage(chatId);
            if (client.isNotificationEnabled()){
                MessagingService messagingService = SpringContext.getBean(MessagingService.class);
                messagingService.sendUpdateClient(client);
            }
        }
        this.answer = new SendMessage(chatId.toString(),
                MessageConst.WB_API_KEY_SET.getMessage());

        super.execute();

        LOGGER.info("Execution complete");
    }


}
