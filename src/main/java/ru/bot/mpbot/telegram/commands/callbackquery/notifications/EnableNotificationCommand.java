package ru.bot.mpbot.telegram.commands.callbackquery.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.bot.mpbot.SpringContext;
import ru.bot.mpbot.messaging.MessagingService;
import ru.bot.mpbot.model.client.Client;
import ru.bot.mpbot.model.client.ClientService;
import ru.bot.mpbot.model.subscription.Subscription;
import ru.bot.mpbot.model.subscription.SubscriptionService;
import ru.bot.mpbot.telegram.commands.BotCommand;
import ru.bot.mpbot.telegram.constants.MessageConst;

import java.io.IOException;
import java.util.Objects;

public class EnableNotificationCommand extends BotCommand {
    private final Logger LOGGER = LoggerFactory.getLogger(EnableNotificationCommand.class);
    private final Long chatId;

    private static final String BODY = """
            {
                "notify":true,
                "client":%s,
                "subscription":%s
            }
            """;

    public EnableNotificationCommand(Long chatId) {
        this.chatId = chatId;
    }

    public void execute() throws IOException {
        LOGGER.info("Sending notification enable to subscribe_queue " + chatId);
        ClientService clientService = SpringContext.getBean(ClientService.class);
        clientService.updateNotifications(chatId, true);
        Client client = clientService.getClientByTgId(chatId);
        Subscription subscription = SpringContext.getBean(SubscriptionService.class)
                .findValidSubscriptionByClient(client);

        MessagingService messagingService = SpringContext.getBean(MessagingService.class);
        messagingService.sendNotificationsSubscription(client, subscription);
        this.answer = new SendMessage(chatId.toString(), MessageConst.NOTIFICATIONS_ENABLED.getMessage());
        super.execute();
    }
}
