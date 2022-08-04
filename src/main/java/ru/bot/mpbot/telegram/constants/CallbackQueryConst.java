package ru.bot.mpbot.telegram.constants;

public enum CallbackQueryConst {
    CHECK_OZON("/checkozn"),
    CHECK_WB("/checkwb"),
    SALES_DETAILED("/sales_detailed"),
    RETURN_GROUP("/return_"),
    RETURN_BACK("/return_back"),
    RETURN_SUBGROUP("/r_"),
    RETURN_SUBGROUP_BACK("/r_back"),
    NOTIFY_GROUP("/notif_"),
    NOTIFICATIONS_ENABLE("/notif_enable"),
    NOTIFICATIONS_DISABLE("/notif_disable"),
    NOTIFICATIONS_BACK("/notif_back");

    private final String message;

    CallbackQueryConst(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
