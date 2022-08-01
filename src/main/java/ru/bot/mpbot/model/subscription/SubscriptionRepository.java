package ru.bot.mpbot.model.subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.bot.mpbot.model.client.Client;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("SELECT s FROM Subscription s WHERE s.client=?1 AND CURRENT_DATE BETWEEN s.startFrom AND s.validThrough")
    List<Subscription> getSubscriptionByClient(Client client);
}
