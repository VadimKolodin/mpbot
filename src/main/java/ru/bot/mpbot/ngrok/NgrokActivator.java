package ru.bot.mpbot.ngrok;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bot.mpbot.config.SpringConfig;
import ru.bot.mpbot.requests.Connector;
import ru.bot.mpbot.telegram.constants.Colors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class NgrokActivator{

    private final static String SET_URL_WEBHOOK_CARCASE = "https://api.telegram.org/bot%s/setWebhook?url=%s";
    private final Logger LOGGER = LoggerFactory.getLogger(NgrokActivator.class);

    private static final String COMMAND = "src/main/resources/programs/ngrok.exe http 8080 --log stdout";

    public void run(Long sessionTime){
        LOGGER.info(Colors.PURPLE.get()+"Started execution"+Colors.RESET.get());
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(COMMAND);
            LOGGER.info("Launched ngrok");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String s = null;
            boolean wasFound = false;
            while (!wasFound) {
                s = reader.readLine();
                wasFound = s.contains("started tunnel");
            }
            LOGGER.info("Got output from ngrok: "+ Colors.GREEN.get() +s+Colors.RESET.get());
            reader.close();
            LOGGER.info("Setting webhook");
            URL url = new URL(String.format(SET_URL_WEBHOOK_CARCASE,
                    SpringConfig.getBotToken(),
                    s.substring(s.indexOf("url=")+4)));
            Connector.createSimpleRequest(url);
            LOGGER.info(Colors.PURPLE.get()+"Trying to sleep, waking up in "+(sessionTime-500)+" ms"+Colors.RESET.get());
            Thread.sleep(sessionTime-1000);
            LOGGER.info("Have waken up, killing ngrok");
            killProcess(process);
            LOGGER.info(Colors.PURPLE.get()+"Finished work"+Colors.RESET.get());
        } catch (IOException e) {
            LOGGER.error("Got error while launching ngrok", e);
        } catch (InterruptedException e) {
            LOGGER.error("Got error while sleeping", e);
            if (process.isAlive()){
                killProcess(process);
            }
            LOGGER.info(Colors.PURPLE.get()+"Killed ngrok"+Colors.RESET.get());
        }
    }
    private void killProcess(Process process){
        for (ProcessHandle child: process.children().toList()){
            killChildProcess(child);
        }
        process.destroy();
    }
    private void killChildProcess(ProcessHandle process){
        for (ProcessHandle child: process.children().toList()){
            killChildProcess(child);
        }
        process.destroy();
    }
}
