package ru.bot.mpbot.telegram.commands.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.bot.mpbot.SpringContext;
import ru.bot.mpbot.model.client.Client;
import ru.bot.mpbot.model.client.ClientService;
import ru.bot.mpbot.telegram.commands.BotCommand;
import ru.bot.mpbot.telegram.constants.Colors;
import ru.bot.mpbot.telegram.constants.MessageConst;
import ru.bot.mpbot.telegram.handler.ReplyKeyboardMaker;

import java.io.IOException;
import java.time.LocalDate;

public class StartCommand extends BotCommand {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public StartCommand(Long chatId) throws IOException {
        LOGGER.info("Started execution");
        ClientService clientService = SpringContext.getBean(ClientService.class);
        Client client = clientService.getClientByTgId(chatId);
        if (client==null){
            LOGGER.info(Colors.GREEN.get()+"Registering new user: "+chatId+Colors.RESET.get());
            clientService.createClient(new Client(chatId, null,
                    null, null, false, LocalDate.now(), LocalDate.now()));
        } else {
            clientService.updateUsage(chatId);
        }
        this.answer = new SendMessage(chatId.toString(),
                String.format(MessageConst.START.getMessage(), "1)","2)", "3)"));

        super.execute();

        ((SendMessage)this.answer).setReplyMarkup(
                SpringContext.getBean(ReplyKeyboardMaker.class).getReplyKeyboardMarkup());
        LOGGER.info("Execution complete");
    }
}
