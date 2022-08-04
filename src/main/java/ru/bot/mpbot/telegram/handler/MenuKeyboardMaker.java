package ru.bot.mpbot.telegram.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bot.mpbot.telegram.constants.CallbackQueryConst;
import ru.bot.mpbot.telegram.constants.MenuButtons;

import java.util.ArrayList;
import java.util.List;

@Component
public class MenuKeyboardMaker {
    private final InlineKeyboardMarkup page1;
    private final InlineKeyboardMarkup checkConnection;
    private final InlineKeyboardMarkup detailedSales;
    private final InlineKeyboardMarkup returnsMpChoice;
    private final InlineKeyboardMarkup returnsPeriodOzon;
    private final InlineKeyboardMarkup returnsPeriodWB;
    private final InlineKeyboardMarkup notificationsEnable;
    private final InlineKeyboardMarkup notificationsDisbale;

    public MenuKeyboardMaker(){
        List<List<InlineKeyboardButton>> rowListCheck = new ArrayList<>();
        rowListCheck.add(createButton("Проверить соединение с Ozon", CallbackQueryConst.CHECK_OZON.getMessage()));
        rowListCheck.add(createButton("Проверить соединение с Wildberries",CallbackQueryConst.CHECK_WB.getMessage()));
        checkConnection = new InlineKeyboardMarkup();
        checkConnection.setKeyboard(rowListCheck);

        returnsMpChoice = generateReturnsMpKeyboards();
        returnsPeriodOzon = generateReturnsPeriodKeyboards(true);
        returnsPeriodWB = generateReturnsPeriodKeyboards(false);

        notificationsEnable = generateEnableNotifications();
        notificationsDisbale = generateDisableNotifications();

        List<List<InlineKeyboardButton>> rowListSales= new ArrayList<>();
        rowListSales.add(createButton("Подробнее", CallbackQueryConst.SALES_DETAILED.getMessage()));
        detailedSales = new InlineKeyboardMarkup();
        detailedSales.setKeyboard(rowListSales);

        List<List<InlineKeyboardButton>> rowListPage1 = new ArrayList<>();
        for (MenuButtons button: MenuButtons.values()){
            rowListPage1.add(createButton(button.getName(), button.getData()));
        }
        page1 = new InlineKeyboardMarkup();
        page1.setKeyboard(rowListPage1);
    }



    private static List<InlineKeyboardButton> createButton(String name, String data){
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(name);
        button.setCallbackData(data);

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(button);
        return keyboardButtonsRow;
    }

    private InlineKeyboardMarkup generateReturnsMpKeyboards(){
        List<List<InlineKeyboardButton>> rowListMp = new ArrayList<>();
        rowListMp.add(createButton("Ozon", "/return_ozn"));
        rowListMp.add(createButton("WB","/return_wb"));
        rowListMp.add(createButton("«Назад",CallbackQueryConst.RETURN_BACK.getMessage()));
        InlineKeyboardMarkup temp = new InlineKeyboardMarkup();
        temp.setKeyboard(rowListMp);
        return temp;
    }
    private InlineKeyboardMarkup generateReturnsPeriodKeyboards(boolean isOzn){
        List<List<InlineKeyboardButton>> rowListPeriod = new ArrayList<>();
        InlineKeyboardMarkup temp = new InlineKeyboardMarkup();
        if (isOzn) {
            rowListPeriod.add(createButton("2 недели", "/r_ozn_1"));
            rowListPeriod.add(createButton("1 месяц", "/r_ozn_2"));
            rowListPeriod.add(createButton("3 месяца", "/r_ozn_3"));
            rowListPeriod.add(createButton("«Назад",CallbackQueryConst.RETURN_SUBGROUP_BACK.getMessage()));
        } else {
            rowListPeriod.add(createButton("2 недели", "/r_wb_1"));
            rowListPeriod.add(createButton("1 месяц", "/r_wb_2"));
            rowListPeriod.add(createButton("3 месяца", "/r_wb_3"));
            rowListPeriod.add(createButton("«Назад",CallbackQueryConst.RETURN_SUBGROUP_BACK.getMessage()));
        }
        temp.setKeyboard(rowListPeriod);
        return temp;
    }

    public InlineKeyboardMarkup generateEnableNotifications(){
        List<List<InlineKeyboardButton>> rowListPeriod = new ArrayList<>();
        InlineKeyboardMarkup temp = new InlineKeyboardMarkup();
        rowListPeriod.add(createButton("Включить уведомления✅", CallbackQueryConst.NOTIFICATIONS_ENABLE.getMessage()));
        rowListPeriod.add(createButton("«Назад", CallbackQueryConst.NOTIFICATIONS_BACK.getMessage()));
        temp.setKeyboard(rowListPeriod);
        return temp;
    }
    public InlineKeyboardMarkup generateDisableNotifications(){
        List<List<InlineKeyboardButton>> rowListPeriod = new ArrayList<>();
        InlineKeyboardMarkup temp = new InlineKeyboardMarkup();
        rowListPeriod.add(createButton("Отключить уведомления❌", CallbackQueryConst.NOTIFICATIONS_DISABLE.getMessage()));
        rowListPeriod.add(createButton("«Назад", CallbackQueryConst.NOTIFICATIONS_BACK.getMessage()));
        temp.setKeyboard(rowListPeriod);
        return temp;
    }

    public InlineKeyboardMarkup getCheckConnection(){ return checkConnection; }
    public InlineKeyboardMarkup getDetailedSales(){
        return detailedSales;
    }
    public InlineKeyboardMarkup getPage1(){
        return page1;
    }
    public InlineKeyboardMarkup getReturnsMpChoice(){
        return  returnsMpChoice;
    }
    public InlineKeyboardMarkup getReturnsPeriodChoice(String s){
        if ("/return_wb".equals(s)){
            return returnsPeriodWB;
        } else {
            return returnsPeriodOzon;
        }
    }

    public InlineKeyboardMarkup getNotificationsEnable() {
        return notificationsEnable;
    }

    public InlineKeyboardMarkup getNotificationsDisbale() {
        return notificationsDisbale;
    }
}
