package ru.bot.mpbot.entity;

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
    private LocalDate regDate;
    private LocalDate usageDate;

    public Client() {
    }

    public Client(Long tgId, String oznKey, String wbKey, String oznId, LocalDate regDate, LocalDate usageDate) {
        this.tgId = tgId;
        this.oznKey = oznKey;
        this.wbKey = wbKey;
        this.oznId = oznId;
        this.regDate = regDate;
        this.usageDate = usageDate;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client user = (Client) o;
        return tgId.equals(user.tgId) && oznKey.equals(user.oznKey) && wbKey.equals(user.wbKey) && oznId.equals(user.oznId) && regDate.equals(user.regDate) && usageDate.equals(user.usageDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tgId, oznKey, wbKey, oznId, regDate, usageDate);
    }

    @Override
    public String toString() {
        return "{\"User\":{"
                + "                        \"tgId\":\"" + tgId + "\""
                + ",                         \"oznKey\":\"" + oznKey + "\""
                + ",                         \"wbKey\":\"" + wbKey + "\""
                + ",                         \"oznId\":\"" + oznId + "\""
                + ",                         \"regDate\":" + regDate
                + ",                         \"usageDate\":" + usageDate
                + "}}";
    }
}
