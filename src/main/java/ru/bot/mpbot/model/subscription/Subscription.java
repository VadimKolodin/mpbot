package ru.bot.mpbot.model.subscription;

import ru.bot.mpbot.model.client.Client;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
public class Subscription {
    @Id
    @SequenceGenerator(
            name="subs_seq",
            sequenceName = "subs_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "subs_seq"
    )
    private Long id;
    private LocalDate startFrom;
    private LocalDate validThrough;
    @ManyToOne
    @JoinColumn(name="client_id", nullable = false)
    private Client client;

    public Subscription() {
    }

    public Subscription(Long id, LocalDate startFrom, LocalDate validThrough, Client client) {
        this.id = id;
        this.startFrom = startFrom;
        this.validThrough = validThrough;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(LocalDate startFrom) {
        this.startFrom = startFrom;
    }

    public LocalDate getValidThrough() {
        return validThrough;
    }

    public void setValidThrough(LocalDate validThrough) {
        this.validThrough = validThrough;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder("{");
        s.append("\"id\":")
                .append(id);
        s.append(",\"startFrom\":\"")
                .append(startFrom).append("\"");
        s.append(",\"validThrough\":\"")
                .append(validThrough).append("\"");
        s.append(",\"client\":")
                .append(client);
        s.append('}');
        return s.toString();
    }
}
