package ru.bot.mpbot.telegram;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import ru.bot.mpbot.telegram.constants.MessageConst;
import ru.bot.mpbot.telegram.handler.CallbackQueryHandler;
import ru.bot.mpbot.telegram.handler.CommandHandler;

import java.io.IOException;

public class MpBot extends SpringWebhookBot {
    private final Logger LOGGER = LoggerFactory.getLogger(CommandHandler.class);
    private static final Long ADMIN = 433638597L;
    private String botPath;
    private String botUsername;
    private String botToken;

    private CallbackQueryHandler callbackQueryHandler;
    private CommandHandler commandHandler;

    public MpBot(SetWebhook setWebhook) {
        super(setWebhook);
        callbackQueryHandler = new CallbackQueryHandler(this);
        commandHandler = new CommandHandler(this);
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            if (update.hasCallbackQuery()) {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                return callbackQueryHandler.processCallbackQuery(callbackQuery);
            }

            Message message = update.getMessage();
            if (message != null) {
                return commandHandler.processCommand(update.getMessage());
            }
        }catch (Exception e){
            LOGGER.error("While executing: ", e);
            try {
                execute(new SendMessage(ADMIN.toString(), e.getClass()+": "+e.getMessage()));
            } catch (TelegramApiException er) {

            }
        }
        return null;
    }

    /**
     * Gets in the url for the webhook
     *
     * @return path in the url
     */
    @Override
    public String getBotPath() {
        return botPath;
    }

    /**
     * Return username of this bot
     */
    @Override
    public String getBotUsername() {

        return botUsername;
    }

    /**
     * Returns the token of the bot to be able to perform Telegram Api Requests
     *
     * @return Token of the bot
     */
    @Override
    public String getBotToken() {
        return botToken;
    }

    public void setBotPath(String botPath) {
        this.botPath = botPath;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }
}