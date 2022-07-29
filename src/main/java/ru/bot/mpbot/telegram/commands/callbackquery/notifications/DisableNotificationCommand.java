package ru.bot.mpbot.telegram.commands.callbackquery.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.bot.mpbot.SpringContext;
import ru.bot.mpbot.model.client.Client;
import ru.bot.mpbot.model.client.ClientService;
import ru.bot.mpbot.telegram.commands.BotCommand;
import ru.bot.mpbot.telegram.constants.MessageConst;

public class DisableNotificationCommand extends BotCommand {
    private final Logger LOGGER = LoggerFactory.getLogger(EnableNotificationCommand.class);
    private final Long chatId;

    private static final String BODY = """
            {
                "notify":false,
                "client":%s
            }
            """;

    public DisableNotificationCommand(Long chatId) {
        this.chatId = chatId;
    }

    public void execute(){
        LOGGER.info("Кладу в очередь выключение уведомлений для " + chatId);
        ClientService clientService = SpringContext.getBean(ClientService.class);
        clientService.updateNotifications(chatId, false);
        Client client = clientService.getClientByTgId(chatId);
        AmqpTemplate template = SpringContext.getBean(AmqpTemplate.class);
        template.convertAndSend("subscribe_queue", String.format(BODY,
                client.toString()));
        this.answer = new SendMessage(chatId.toString(), MessageConst.NOTIFICATIONS_DISABLED.getMessage());
        super.execute();
    }
}
