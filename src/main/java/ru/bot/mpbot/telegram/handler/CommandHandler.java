package ru.bot.mpbot.telegram.handler;

import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bot.mpbot.SpringContext;
import ru.bot.mpbot.exception.NoSuchClientException;
import ru.bot.mpbot.service.CommandRecordService;
import ru.bot.mpbot.telegram.MpBot;
import ru.bot.mpbot.telegram.commands.*;
import ru.bot.mpbot.telegram.commands.text.*;
import ru.bot.mpbot.telegram.constants.Colors;
import ru.bot.mpbot.telegram.constants.ErrorConst;
import ru.bot.mpbot.telegram.constants.MessageConst;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CommandHandler {

    private static final Long ADMIN = 433638597L;

    private final Logger LOGGER = LoggerFactory.getLogger(CommandHandler.class);

    private final MpBot bot;

    public CommandHandler(MpBot mpBot) {
        bot=mpBot;
    }

    public BotApiMethod<?> processCommand(Message message) {
        /* try {
            bot.execute(new SendMessage(ADMIN.toString(), message.getChat().getUserName()+" ("+message.getChatId()+"): "+message.getText()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }*//////////////////////////////
        LOGGER.info(message.getChat().getUserName()+" ("+message.getChatId()+"): "+message.getText());
        String text = message.getText();
        String[] args = message.getText().trim().split(" ");
        BotCommand command = null;
        try {
            saveCommandInput(message.getChatId(), args[0], text.substring(args[0].length()));
        } catch (NoSuchClientException e){}
        switch (args[0]){
            case "/menu":
                SendMessage menuMessage = new SendMessage(message.getChatId().toString(),
                        MessageConst.MENU_TEXT.getMessage());
                menuMessage.setReplyMarkup(SpringContext.getBean(MenuKeyboardMaker.class).getPage1());
                return menuMessage;
            case "/help":
                //TODO
                return new SendMessage(message.getChatId().toString(),
                        "/help");

            case "/oznkey":
                if (args.length!=2){
                   return wrongInput(message.getChatId(), ErrorConst.WRONG_INPUT_OZN_KEY);
                } else {
                    command = new SetOzonKeyCommand(message.getChatId(), args[1]);
                }
                break;
            case "/oznid":
                if (args.length!=2){
                    return wrongInput(message.getChatId(), ErrorConst.WRONG_INPUT_OZN_ID);
                } else {
                    command = new SetOzonIdCommand(message.getChatId(), args[1]);
                }
                break;
            case "/wbkey":
                if (args.length!=2){
                    return wrongInput(message.getChatId(), ErrorConst.WRONG_INPUT_WB_KEY);
                } else {
                    command = new SetWBKeyCommand(message.getChatId(), args[1]);
                }
                break;
            case "/check":
                command = new CheckCommand(message.getChatId());
                break;
            case "/start":
                command = new StartCommand(message.getChatId());
                break;
            default:
                return new SendMessage(message.getChatId().toString(),
                        MessageConst.UNDEFINED_REPLY.getMessage());
        }

        command.execute();

        if (command.isExecuted()) {
            return command.getAnswer();
        } else {
            return new SendMessage(message.getChatId().toString(),
                    ErrorConst.INTERNAL_ERROR.getMessage());
        }
    }
    private BotApiMethod<?> wrongInput(Long chatId, ErrorConst msg){
        LOGGER.info(chatId + " wrong input:"+ Colors.BLUE.get() +msg.getMessage()+Colors.RESET.get());
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setCaption(msg.getMessage());
        sendPhoto.setChatId(chatId.toString());
        File photo = null;

        switch (msg){
            case WRONG_INPUT_OZN_ID:
                photo = new File("src/main/resources/photos/ozn_id_example.png");
                break;
            case WRONG_INPUT_OZN_KEY:
                photo = new File("src/main/resources/photos/ozn_key_example.png");
                break;
            case WRONG_INPUT_WB_KEY:
                photo = new File("src/main/resources/photos/wb_key_example.png");
                break;
            default:
                return new SendMessage(chatId.toString(), msg.getMessage());
        }
        try {
            sendPhoto.setPhoto(new InputFile(new FileInputStream(photo), photo.getName()));
            bot.execute(sendPhoto);
            return null;
        } catch (TelegramApiException | FileNotFoundException e) {
            LOGGER.error("Exception during SendPhoto: ", e);
            return new SendMessage(chatId.toString(), msg.getMessage());
        }
    }

    private void saveCommandInput(Long chatId, String input, String args){
        CommandRecordService commandRecordService = SpringContext.getBean(CommandRecordService.class);
        commandRecordService.saveCommand(chatId, input, args);
    }
}
