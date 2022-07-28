package ru.bot.mpbot.telegram.commands.callbackquery.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.bot.mpbot.SpringContext;
import ru.bot.mpbot.model.client.ClientService;
import ru.bot.mpbot.telegram.commands.BotCommand;
import ru.bot.mpbot.telegram.constants.MessageConst;

public class DisableNotificationCommand extends BotCommand {
    private final Logger LOGGER = LoggerFactory.getLogger(EnableNotificationCommand.class);
    private final Long chatId;

    public DisableNotificationCommand(Long chatId) {
        this.chatId = chatId;
    }

    public void execute(){
        LOGGER.info("Кладу в очередь выключение уведомлений для " + chatId);
        SpringContext.getBean(ClientService.class).updateNotifications(chatId, false);
        AmqpTemplate template = SpringContext.getBean(AmqpTemplate.class);
        template.convertAndSend("subscribe_queue", "отписать "+ chatId);
        this.answer = new SendMessage(chatId.toString(), MessageConst.NOTIFICATIONS_DISABLED.getMessage());
        super.execute();
    }
}
