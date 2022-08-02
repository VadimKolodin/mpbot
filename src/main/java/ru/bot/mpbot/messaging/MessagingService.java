package ru.bot.mpbot.messaging;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bot.mpbot.model.client.Client;
import ru.bot.mpbot.model.subscription.Subscription;

@Component
public class MessagingService {
    private static final String BODY_WITH_SUBSCRIPTION = """
            {
                "notify":%S,
                "client":%s,
                "subscription":%s
            }
            """;
    private static final String BODY_WITHOUT_SUBSCRIPTION = """
            {
                "notify":%s,
                "client":%s
            }
            """;
    
    private AmqpTemplate template;

    @Autowired
    public MessagingService(AmqpTemplate template) {
        this.template = template;
    }

    public void sendNotificationsSubscription(Client client, Subscription subscription){
        if (subscription!=null){
            template.convertAndSend("subscribe_queue",
                    String.format(BODY_WITH_SUBSCRIPTION,
                        client.isNotificationEnabled(),
                        client.toString(),
                        subscription.toString()));
        } else {
            template.convertAndSend("subscribe_queue",
                    String.format(BODY_WITHOUT_SUBSCRIPTION,
                        client.isNotificationEnabled(),
                        client.toString()));
        }
    }
    public void sendUpdateClient(Client client){
        template.convertAndSend("subscribe_queue",
                String.format(BODY_WITHOUT_SUBSCRIPTION,
                        client.isNotificationEnabled(),
                        client.toString()));
    }
}
