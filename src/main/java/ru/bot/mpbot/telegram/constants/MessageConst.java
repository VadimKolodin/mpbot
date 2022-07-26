package ru.bot.mpbot.telegram.constants;

public enum MessageConst {

    UNDEFINED_REPLY("Такой команды нет\uD83D\uDE2F\nВведите /help чтобы узнать команды"),
    START("""
            Для использования бота необходимо:
            Либо данные для маркетплейса Ozon:
            \t%sAPI-ключ (команда /oznkey)\s
            \t%sИдентификатор клиента (Client ID) (команда /oznid)\s
            Либо данные для маркетплеса Wildberries:
            \t%sAPI-ключ (команда /wbkey)
            По всем вопросам обращаться @aartsko"""),
    MENU_TEXT(" Текст меню Текст меню Текст меню Текст меню Текст меню Текст меню Текст меню Текст меню"),
    OZN_API_KEY_SET("Ваш API-key для маркетплейса Ozon установлен успешно\uD83E\uDD73\n" +
            "Осталось только установить ClientID для Ozon"),
    OZN_API_KEY_SET_COMPLETE("Ваш API-key для маркетплейса Ozon установлен успешно\uD83E\uDD73\n" +
            "Теперь Вы можете воспользоваться всеми командами для Ozon"),
    OZN_CLIENT_ID_SET("Ваш ClientID для маркетплейса Ozon установлен успешно\uD83E\uDD73\n" +
            "Осталось только установить API-key для Ozon"),
    OZN_CLIENT_ID_SET_COMPLETE("Ваш ClientID для маркетплейса Ozon установлен успешно\uD83E\uDD73\n" +
            "Теперь Вы можете воспользоваться всеми командами для Ozon"),
    WB_API_KEY_SET("Ваш API-key для маркетплейса Wildberries установлен успешно\uD83E\uDD73\n" +
            " Теперь Вы можете воспользоваться всеми командами для Wildberries!"),
    CHECK_OZON_SUCCESS("Удалось подключиться к сервисам Ozon\uD83E\uDD73\n Ваши данные введены правильно"),
    CHECK_WB_SUCCESS("Удалось подключиться к сервисам Wildberries🥳\n Ваши данные введены правильно"),
    CAPITALIZE_OZON("Капитализация склада Ozon:\n\tFBO: *%.2f* руб.\n\tFBS: *%.2f* руб."),
    CAPITALIZE_WB("Капитализация склада Wildberries: *%.2f* руб."),
    SALES_TODAY_OZON_TOTAL("Сегодня на *Ozon* было продано *%d* товаров на *%.2f* руб."),
    SALES_TODAY_OZON_ALL("*Список товаров, проданных на Ozon сегодня:*"),
    SALES_TODAY_OZON_ALL_EMPTY("За сегодня на Ozon еще ничего не продали\uD83D\uDE22"),
    SALES_TODAY_WB_TOTAL("Сегодня на *WB* было продано *%d* товаров на *%.2f* руб."),
    SALES_TODAY_WB_ALL("*Список товаров, проданных на WB сегодня:*"),
    SALES_TODAY_WB_ALL_EMPTY("За сегодня на WB еще ничего не продали\uD83D\uDE22"),
    RETURNS_MP_CHOICE("Выберете маркетплейс:"),
    RETURNS_PERIOD_CHOICE("Выберете период:"),
    RETURN_TOP_TEMPLATE("Топ возвратов на *%s* за период от *%s* до *%s*:\n"),
    RETURN_TOP_EMPTY("За период от *%s* до *%s* на *%s* возвратов не было\uD83D\uDE0E");
    private final String message;

    MessageConst(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
