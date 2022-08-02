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
    private String username;
    @Column(columnDefinition = "boolean default false")
    private boolean isNotificationEnabled;
    private LocalDate regDate;
    private LocalDate usageDate;

    public Client() {
    }

    public Client(Long tgId, String oznKey, String wbKey, String oznId, String username, boolean isNotificationEnabled, LocalDate regDate, LocalDate usageDate) {
        this.tgId = tgId;
        this.oznKey = oznKey;
        this.wbKey = wbKey;
        this.oznId = oznId;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
        final StringBuilder s = new StringBuilder("{");
        s.append("\"id\":")
                .append(id);
        s.append(",\"tgId\":")
                .append(tgId);
        s.append(",\"oznKey\":\"")
                .append(Objects.toString(oznKey, "")).append('\"');
        s.append(",\"wbKey\":\"")
                .append(Objects.toString(wbKey, "")).append('\"');
        s.append(",\"oznId\":\"")
                .append(Objects.toString(oznId, "")).append('\"');
        s.append(",\"isNotificationEnabled\":")
                .append(isNotificationEnabled);
        s.append(",\"regDate\":\"")
                .append(regDate).append('\"');
        s.append(",\"usageDate\":\"")
                .append(usageDate).append('\"');
        s.append('}');
        return s.toString();
    }
}
