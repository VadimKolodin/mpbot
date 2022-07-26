package ru.bot.mpbot.telegram.constants;

public enum MenuButtons {
    CAPITALIZE("Капитализация склада", "/capitalize"),
    SALES_TODAY("Продажи за сегодня","/sales"),
    RETURNS("Топ возвратов", "/returns");


    private final String name;
    private final String data;

    MenuButtons(String name, String data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }
}
