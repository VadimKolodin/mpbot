package ru.bot.mpbot.model.commandrecord;

import ru.bot.mpbot.model.client.Client;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class CommandRecord {
    @Id
    @SequenceGenerator(
            name="record_seq",
            sequenceName="record_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "record_seq"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(name="client_id", nullable = false)
    private Client client;
    private String input;

    public CommandRecord() {
    }

    public CommandRecord(Long id, Client user, String input) {
        this.id = id;
        this.client = user;
        this.input = input;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }


    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder("{");
        s.append("\"id\":")
                .append(id);
        s.append(",\"client\":")
                .append(client);
        s.append(",\"input\":\"")
                .append(Objects.toString(input, "")).append('\"');
        s.append('}');
        return s.toString();
    }
}
