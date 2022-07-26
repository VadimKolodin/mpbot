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
import ru.bot.mpbot.telegram.handler.MenuKeyboardMaker;
import ru.bot.mpbot.telegram.handler.ReplyKeyboardMaker;

import java.time.LocalDate;

public class CheckCommand extends BotCommand {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public CheckCommand(Long chatId) {
        LOGGER.info("Started execution");
        ClientService clientService = SpringContext.getBean(ClientService.class);
        Client client = clientService.getClientByTgId(chatId);
        if (client==null){
            this.answer = new SendMessage(chatId.toString(),
                    String.format(MessageConst.START.getMessage(), "1)","2)", "3)"));
        } else {
            clientService.updateUsage(chatId);
            this.answer = new SendMessage(chatId.toString(),
                    String.format(MessageConst.START.getMessage(),
                            client.getOznKey()==null?"1)":"✅",
                            client.getOznId()==null?"2)":"✅",
                            client.getWbKey()==null?"1)":"✅"));
        }
        try {
            super.execute();
        } catch (Exception e){}
        ((SendMessage)this.answer).setReplyMarkup(
                SpringContext.getBean(MenuKeyboardMaker.class).getCheckConnection());
        LOGGER.info("Execution complete");
    }
}
