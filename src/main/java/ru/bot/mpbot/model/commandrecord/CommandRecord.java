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
    private String args;

    public CommandRecord() {
    }

    public CommandRecord(Long id, Client user, String input, String args) {
        this.id = id;
        this.client = user;
        this.input = input;
        this.args = args;
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

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandRecord that = (CommandRecord) o;
        return id.equals(that.id) && client.equals(that.client) && input.equals(that.input) && args.equals(that.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, input, args);
    }

    @Override
    public String toString() {
        return "{\"CommandRecord\":{"
                + "                        \"id\":\"" + id + "\""
                + ",                         \"user\":" + client
                + ",                         \"input\":\"" + input + "\""
                + ",                         \"args\":\"" + args + "\""
                + "}}";
    }
}
