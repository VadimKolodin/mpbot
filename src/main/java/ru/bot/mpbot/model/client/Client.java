package ru.bot.mpbot.model.client;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"tgId"})})
public class Client {
    @Id
    @SequenceGenerator(
            name="client_seq",
            sequenceName="client_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "client_seq"
    )
    private Long id;
    private Long tgId;
    private String oznKey;
    private String wbKey;
    private String oznId;
    @Column(columnDefinition = "boolean default false")
    private boolean isNotificationEnabled;
    private LocalDate regDate;
    private LocalDate usageDate;

    public Client() {
    }

    public Client(Long tgId, String oznKey, String wbKey, String oznId, boolean isNotificationEnabled, LocalDate regDate, LocalDate usageDate) {
        this.tgId = tgId;
        this.oznKey = oznKey;
        this.wbKey = wbKey;
        this.oznId = oznId;
        this.isNotificationEnabled = isNotificationEnabled;
        this.regDate = regDate;
        this.usageDate = usageDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTgId() {
        return tgId;
    }

    public void setTgId(Long tgId) {
        this.tgId = tgId;
    }

    public String getOznKey() {
        return oznKey;
    }

    public void setOznKey(String oznKey) {
        this.oznKey = oznKey;
    }

    public String getWbKey() {
        return wbKey;
    }

    public void setWbKey(String wbKey) {
        this.wbKey = wbKey;
    }

    public String getOznId() {
        return oznId;
    }

    public void setOznId(String oznId) {
        this.oznId = oznId;
    }

    public boolean isNotificationEnabled() {
        return isNotificationEnabled;
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        isNotificationEnabled = notificationEnabled;
    }

    public LocalDate getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDate regDate) {
        this.regDate = regDate;
    }

    public LocalDate getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(LocalDate usageDate) {
        this.usageDate = usageDate;
    }

    @Override
    public String toString() {
        String s = "{" + "\"id\":" +
                id +
                ",\"tgId\":" +
                tgId +
                ",\"oznKey\":\"" +
                Objects.toString(oznKey, "") + '\"' +
                ",\"wbKey\":\"" +
                Objects.toString(wbKey, "") + '\"' +
                ",\"oznId\":\"" +
                Objects.toString(oznId, "") + '\"' +
                ",\"isNotificationEnabled\":" +
                isNotificationEnabled +
                ",\"regDate\":" +
                regDate +
                ",\"usageDate\":" +
                usageDate +
                '}';
        return s;
    }
}