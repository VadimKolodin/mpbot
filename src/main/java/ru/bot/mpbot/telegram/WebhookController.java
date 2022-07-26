package ru.bot.mpbot.telegram;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController

public class WebhookController {
    private final MpBot mpBot;

    public WebhookController(MpBot mpBot){
        this.mpBot=mpBot;
    }

    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return mpBot.onWebhookUpdateReceived(update);
    }
}