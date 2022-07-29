package ru.bot.mpbot.model.subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bot.mpbot.model.client.Client;

import java.time.LocalDate;
import java.util.List;

@Service
public class SubscriptionService {

    private SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository){
        this.subscriptionRepository=subscriptionRepository;
    }

    public void createSubscription(Subscription subscription){
        if (subscription.getValidThrough().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Попытка добавить уже истекшую подписку");
        }
        subscriptionRepository.save(subscription);
    }
    public boolean isSubscriptionValid(Client client, Subscription.SubscriptionType type){
        List<Subscription> subscriptions = subscriptionRepository.getSubscriptionByType(client, type);
        if (subscriptions.isEmpty()){
            return false;
        }
        return LocalDate.now().isBefore(subscriptions.get(0).getValidThrough());
    }
    public Subscription findSubscriptionByType(Client client, Subscription.SubscriptionType type){
        List<Subscription> subscriptions = subscriptionRepository.getSubscriptionByType(client, type);
        if (subscriptions.isEmpty()){
            return null;
        }
        return subscriptions.get(0);
    }
}
