package ru.bot.mpbot.telegram.commands.callbackquery.returns;

public enum ReturnPeriod {
    TWO_WEEKS,
    ONE_MONTH,
    THREE_MONTHS;
    public static ReturnPeriod getEnum(int i){
        switch (i){
            case 0:
                return TWO_WEEKS;
            case 1:
                return ONE_MONTH;
            case 2:
                return THREE_MONTHS;
            default:
                throw new IllegalArgumentException("No such value: "+i);
        }
    }
}
