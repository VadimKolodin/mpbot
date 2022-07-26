package ru.bot.mpbot.telegram.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReplyKeyboardMaker {
    private ReplyKeyboardMarkup replyKeyboardMarkup;
    private static final String HELP = "/help";
    private static final String START = "/start";
    private static final String MENU = "/menu";
    private static final String CHECK = "/check";
    private static final String OZNKEY = "/oznkey";
    private static final String OZNID = "/oznid";
    private static final String WBKEY = "/wbkey";
    public ReplyKeyboardMaker(){
        KeyboardRow row1 = new KeyboardRow();
        row1.add( new KeyboardButton(START));
        row1.add(new KeyboardButton(CHECK));
        row1.add(new KeyboardButton(HELP));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(MENU));
        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton(OZNKEY));
        row3.add(new KeyboardButton(OZNID));
        row3.add(new KeyboardButton(WBKEY));
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(false);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setInputFieldPlaceholder("Выберете команду");
    }
    public ReplyKeyboardMarkup getReplyKeyboardMarkup(){
        return replyKeyboardMarkup;
    }
}
