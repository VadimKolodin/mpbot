package ru.bot.mpbot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import ru.bot.mpbot.ngrok.NgrokActivator;
import ru.bot.mpbot.telegram.MpBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@EnableScheduling
@Configuration
public class SpringConfig {
    private final static String WEBHOOK_PATH = "https://6946-178-218-81-141.eu.ngrok.io";
   // @Value("${bot.token}")
    private static String TOKEN = "5536719631:AAHQJxNnxrMkSz0bHAx0N7STeITNTWsH0U0";
   // @Value("${bot.name}")
    private static String NAME = "mpanalyticstest_bot";

    private final Logger LOGGER = LoggerFactory.getLogger(SpringConfig.class);

    public SpringConfig(){

    }

    public static String getBotToken(){
        return TOKEN;
    }

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(WEBHOOK_PATH).build();
    }

    @Bean
    public MpBot springWebhookBot(SetWebhook setWebhook) {
        MpBot bot = new MpBot(setWebhook);

        bot.setBotPath(WEBHOOK_PATH);
        bot.setBotUsername(NAME);
        bot.setBotToken(TOKEN);

        return bot;
    }

    @Scheduled(fixedRate =2*60*60*1000)
    public void launchNgrok(){
        NgrokActivator ngrokActivator = new NgrokActivator();
        ngrokActivator.run(2*60*60*1000L);
    }

}