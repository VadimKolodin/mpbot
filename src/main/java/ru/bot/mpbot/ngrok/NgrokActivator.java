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

public class NgrokActivator implements Runnable{

    private final static String SET_URL_WEBHOOK_CARCASE = "https://api.telegram.org/bot%s/setWebhook?url=%s";
    private final Logger LOGGER = LoggerFactory.getLogger(NgrokActivator.class);

    public static final long SESSION_TIME = 1000*60*120L;
    private static final String COMMAND = "src/main/resources/programs/ngrok.exe http 8080 --log stdout";
    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        LOGGER.info("Started execution");
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(COMMAND);
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
            LOGGER.info("Trying to sleep, waking up in "+(SESSION_TIME-500)+" ms");
            Thread.sleep(SESSION_TIME-500);
            LOGGER.info("Have waken up, killing ngrok");
            killProcess(process);
            LOGGER.info("Finished work");
        } catch (IOException e) {
           LOGGER.error("Got error while launching ngrok", e);
        } catch (InterruptedException e) {
            LOGGER.error("Got error while sleeping", e);
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
