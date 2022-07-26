package ru.bot.mpbot.telegram.constants;

public enum ErrorConst {

    WRONG_INPUT_OZN_KEY("Вы неправильно ввели API-key для маркетплейса Ozon\uD83D\uDE14\n" +
            " Попробуйте, пожалуйста, еще раз так, как показано на картинке"),
    WRONG_INPUT_OZN_ID("Вы неправильно ввели ClientID для маркетплейса Ozon\uD83D\uDE14\n" +
                                " Попробуйте, пожалуйста, еще раз так, как показано на картинке"),
    WRONG_INPUT_WB_KEY("Вы неправильно ввели API-key для маркетплейса Wildberries\uD83D\uDE14\n" +
                                " Попробуйте, пожалуйста, еще раз так, как показано на картинке"),
    WRONG_OZN_INFO("Вы неправильно ввели данные для Ozon или они больше недействительны\uD83E\uDD72"),
    OZON_ERROR("Что-то не так с сервисами Ozon\uD83D\uDE31"),
    WRONG_WB_INFO("Вы неправильно ввели данные для Wildberries или они больше недействительны, " +
            "или у API-key недостаточный уровень прав\uD83E\uDD72"),
    WB_ERROR("Что-то не так с сервисами Wildberries\uD83D\uDE31"),
    NO_OZN_INFO("Вы еще не ввели ClientID и API-key для Ozon\uD83D\uDE05"),
    NO_OZN_KEY("Нужно еще ввести API-key для Ozon\uD83D\uDE05"),
    NO_OZN_ID("Нужно еще ввести ClientID для Ozon\uD83D\uDE05"),
    NO_WB_KEY("Вы еще не ввели API-key для Wildberries\uD83D\uDE05"),
    WRONG_CREDENTIALS_OZON("Вы неправильно ввели API-key и/или ClientID для Ozon или они устарели, проверить: /check"),
    WRONG_CREDENTIALS_WB("Вы неправильно ввели API-key для WB или он устарел, проверить: /check"),
    INTERNAL_ERROR("Что-то случилось и возникла непредвиденная ошибка\uD83E\uDD2F\n" +
            "Попробуйте, пожалуйста еще раз, или свяжитесь с нами: @YeahR1ght"),
    TOO_MANY_REQ_OZON("Вы сделали слишком много запросов к серверам Ozon\uD83D\uDE05\n Попробуйте чуть позже"),
    TOO_MANY_REQ_WB("Вы сделали слишком много запросов к серверам WB\uD83D\uDE05\n Попробуйте чуть позже"),
    NO_OZON_KEY_COMMAND("Чтобы узнать эту информацию, нам нужны API-key и ClientID для Ozon"),
    No_WB_KEY_COMMAND("Чтобы узнать эту информацию, нам нужен API-key для Wildberries");
    private final String message;

    ErrorConst(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
