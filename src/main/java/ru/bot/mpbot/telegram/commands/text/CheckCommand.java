package ru.bot.mpbot.telegram.commands.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.bot.mpbot.SpringContext;
import ru.bot.mpbot.model.client.Client;
import ru.bot.mpbot.model.client.ClientService;
import ru.bot.mpbot.telegram.commands.BotCommand;
import ru.bot.mpbot.telegram.constants.MessageConst;
import ru.bot.mpbot.telegram.handler.MenuKeyboardMaker;

import java.io.IOException;

public class CheckCommand extends BotCommand {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public CheckCommand(Long chatId) throws IOException {
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
        super.execute();

        this.answer.setReplyMarkup(
                SpringContext.getBean(MenuKeyboardMaker.class).getCheckConnection());
        this.answer.enableMarkdown(false);
        LOGGER.info("Execution complete");
    }
}
