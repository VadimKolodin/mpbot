package ru.bot.mpbot.telegram.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bot.mpbot.SpringContext;
import ru.bot.mpbot.exception.*;
import ru.bot.mpbot.model.client.ClientService;
import ru.bot.mpbot.model.client.NoSuchClientException;
import ru.bot.mpbot.model.commandrecord.CommandRecordService;
import ru.bot.mpbot.telegram.MpBot;
import ru.bot.mpbot.telegram.commands.BotCommand;
import ru.bot.mpbot.telegram.commands.BotMediaCommand;
import ru.bot.mpbot.telegram.commands.callbackquery.CheckOzonCommand;
import ru.bot.mpbot.telegram.commands.callbackquery.CheckWBCommand;
import ru.bot.mpbot.telegram.commands.callbackquery.capitalize.CapitalizeCommand;
import ru.bot.mpbot.telegram.commands.callbackquery.notifications.DisableNotificationCommand;
import ru.bot.mpbot.telegram.commands.callbackquery.notifications.EnableNotificationCommand;
import ru.bot.mpbot.telegram.commands.callbackquery.returns.ReturnPeriod;
import ru.bot.mpbot.telegram.commands.callbackquery.returns.ReturnsCommand;
import ru.bot.mpbot.telegram.commands.callbackquery.sales.SalesTodayCommand;
import ru.bot.mpbot.telegram.constants.CallbackQueryConst;
import ru.bot.mpbot.telegram.constants.ErrorConst;
import ru.bot.mpbot.telegram.constants.MenuButtons;
import ru.bot.mpbot.telegram.constants.MessageConst;

import java.io.IOException;


public class CallbackQueryHandler {
    private static final Long ADMIN = 433638597L;
    private final Logger LOGGER = LoggerFactory.getLogger(CallbackQueryHandler.class);

    private MpBot bot;


    public CallbackQueryHandler(MpBot mpBot) {
        this.bot=mpBot;
    }

    public BotApiMethod<?> processCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String queryData = callbackQuery.getData();
        LOGGER.info(callbackQuery.getMessage().getChat().getUserName()+
                " ("+chatId+"): "
                +queryData);
         try {
            bot.execute(new SendMessage(ADMIN.toString(),
             callbackQuery.getMessage().getChat().getUserName()+" ("+chatId+"): "+queryData));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        try {
            SpringContext.getBean(ClientService.class).updateUsage(chatId);
        } catch (NoSuchClientException e){
            return new SendMessage(chatId.toString(),
                    ErrorConst.NO_ANY_CLIENT_INFO.getMessage());
        }
        try {
            saveCommandInput(callbackQuery.getMessage().getChatId(), callbackQuery.getData(), null);
        } catch (NoSuchClientException e){}

        BotCommand command = null;
        if (MenuButtons.CAPITALIZE.getData().equals(queryData)){
            command = new CapitalizeCommand(chatId);
        } else if (CallbackQueryConst.CHECK_OZON.getMessage().equals(queryData)){
            command = new CheckOzonCommand(chatId);
        } else if (CallbackQueryConst.CHECK_WB.getMessage().equals(queryData)){
            command = new CheckWBCommand(chatId);
        } else if (MenuButtons.SALES_TODAY.getData().equals(queryData)){
            command = new SalesTodayCommand(chatId, false);
        } else if (CallbackQueryConst.SALES_DETAILED.getMessage().equals(queryData)){
            command = new SalesTodayCommand(chatId, true);
        } else if (MenuButtons.RETURNS.getData().equals(queryData)){
            EditMessageText returnsMsg = new EditMessageText();
            returnsMsg.enableMarkdown(true);
            returnsMsg.setChatId(chatId.toString());
            returnsMsg.setMessageId(callbackQuery.getMessage().getMessageId());
            returnsMsg.setText(MessageConst.RETURNS_MP_CHOICE.getMessage());
            returnsMsg.setReplyMarkup(SpringContext.getBean(MenuKeyboardMaker.class).getReturnsMpChoice());
            return returnsMsg;
        } else if (queryData.contains(CallbackQueryConst.RETURN_GROUP.getMessage())){
            EditMessageText msg = new EditMessageText();
            msg.enableMarkdown(true);
            msg.setChatId(chatId.toString());
            msg.setMessageId(callbackQuery.getMessage().getMessageId());
            if (CallbackQueryConst.RETURN_BACK.getMessage().equals(queryData)){
                msg.setReplyMarkup(SpringContext.getBean(MenuKeyboardMaker.class)
                        .getPage1());
                msg.setText(MessageConst.MENU_TEXT.getMessage());
            } else {
                msg.setReplyMarkup(SpringContext.getBean(MenuKeyboardMaker.class)
                        .getReturnsPeriodChoice(queryData));
                msg.setText(MessageConst.RETURNS_PERIOD_CHOICE.getMessage());
            }
            return msg;
        } else if (queryData.contains(CallbackQueryConst.RETURN_SUBGROUP.getMessage())){
            if (CallbackQueryConst.RETURN_SUBGROUP_BACK.getMessage().equals(queryData)) {
                EditMessageText msg = new EditMessageText();
                msg.enableMarkdown(true);
                msg.setChatId(chatId.toString());
                msg.setMessageId(callbackQuery.getMessage().getMessageId());
                msg.setReplyMarkup(SpringContext.getBean(MenuKeyboardMaker.class)
                        .getReturnsMpChoice());
                msg.setText(MessageConst.RETURNS_MP_CHOICE.getMessage());
                return msg;
            } else {
                boolean isOzn = queryData.contains("ozn");
                ReturnPeriod period = ReturnPeriod.getEnum(
                Integer.parseInt(queryData.substring(queryData.length()-1))-1);
                command = new ReturnsCommand(isOzn, period, chatId);
            }
        } else if (MenuButtons.NOTIFICATIONS.getData().equals(queryData)){
            EditMessageText msg = new EditMessageText();
            msg.enableMarkdown(true);
            msg.setChatId(chatId.toString());
            msg.setMessageId(callbackQuery.getMessage().getMessageId());
            if (SpringContext.getBean(ClientService.class).getClientByTgId(chatId).isNotificationEnabled()){
                msg.setReplyMarkup(SpringContext.getBean(MenuKeyboardMaker.class)
                        .getNotificationsDisbale());
            } else {
                msg.setReplyMarkup(SpringContext.getBean(MenuKeyboardMaker.class)
                        .getNotificationsEnable());
            }
            msg.setText(MessageConst.NOTIFICATIONS.getMessage());
            return msg;
        } else if (queryData.contains(CallbackQueryConst.NOTIFY_GROUP.getMessage())){
            if (CallbackQueryConst.NOTIFICATIONS_ENABLE.getMessage().equals(queryData)){
                    command = new EnableNotificationCommand(chatId);
                    EditMessageText msg = new EditMessageText();
                    msg.enableMarkdown(true);
                    msg.setChatId(chatId.toString());
                    msg.setMessageId(callbackQuery.getMessage().getMessageId());
                    msg.setText(MessageConst.NOTIFICATIONS.getMessage());
                    msg.setReplyMarkup(SpringContext.getBean(MenuKeyboardMaker.class)
                            .getNotificationsDisbale());
                    try {
                        bot.execute(msg);
                    } catch (TelegramApiException e) {
                        LOGGER.error("error changing notification button to disable", e);
                    }
                } else if (CallbackQueryConst.NOTIFICATIONS_DISABLE.getMessage().equals(queryData)){
                    command = new DisableNotificationCommand(chatId);
                    EditMessageText msg = new EditMessageText();
                    msg.enableMarkdown(true);
                    msg.setChatId(chatId.toString());
                    msg.setMessageId(callbackQuery.getMessage().getMessageId());
                    msg.setText(MessageConst.NOTIFICATIONS.getMessage());
                    msg.setReplyMarkup(SpringContext.getBean(MenuKeyboardMaker.class)
                            .getNotificationsEnable());
                    try {
                        bot.execute(msg);
                    } catch (TelegramApiException e) {
                        LOGGER.error("error changing notification button to enable", e);
                    }
                } else if (CallbackQueryConst.NOTIFICATIONS_BACK.getMessage().equals(queryData)){
                    EditMessageText msg = new EditMessageText();
                    msg.setChatId(chatId.toString());
                    msg.enableMarkdown(true);
                    msg.setMessageId(callbackQuery.getMessage().getMessageId());
                    msg.setReplyMarkup(SpringContext.getBean(MenuKeyboardMaker.class)
                            .getPage1());
                    msg.setText(MessageConst.MENU_TEXT.getMessage());
                    return msg;
                }
            }


        if (command== null){
            return new SendMessage(chatId.toString(),
                    ErrorConst.INTERNAL_ERROR.getMessage());
        }
        try {
            command.execute();
        }catch (IOException e){
            LOGGER.error("Error executing command.execute on "+command.getClass().getSimpleName(), e);
            return new SendMessage(chatId.toString(),
                    new RequestExceptionHandler().handle(e));
        }

        if (command.isExecuted()) {
            if (command instanceof BotMediaCommand){
                if (((BotMediaCommand) command).getMediaAnswer()!=null){
                    try {
                        bot.execute(((BotMediaCommand) command).getMediaAnswer());
                    } catch (TelegramApiException e) {
                        LOGGER.error("Error answering "+chatId, e);
                        return new SendMessage(chatId.toString(),
                                ErrorConst.INTERNAL_ERROR.getMessage());
                    }
                }
            }
            return command.getAnswer();
        } else {
            LOGGER.error("Command isExecuted=false after execution "+command.getClass().getSimpleName());
            return new SendMessage(chatId.toString(),
                    ErrorConst.INTERNAL_ERROR.getMessage());
        }
    }
    private void saveCommandInput(Long chatId, String input, String args){
        CommandRecordService commandRecordService = SpringContext.getBean(CommandRecordService.class);
        commandRecordService.saveCommand(chatId, input, args);
    }

}
