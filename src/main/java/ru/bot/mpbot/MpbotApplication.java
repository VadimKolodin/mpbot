package ru.bot.mpbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.bot.mpbot.ngrok.NgrokActivator;
import ru.bot.mpbot.telegram.constants.Colors;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class MpbotApplication {
	private static Logger LOGGER = LoggerFactory.getLogger(MpbotApplication.class);
	public static void main(String[] args) {

		SpringApplication.run(MpbotApplication.class, args);
	}

	/*private static void startDaemonExecutor(){
		LOGGER.info(Colors.CYAN+"Creating Scheduled executor service"+Colors.RESET);
		ScheduledExecutorService scheduler
				= Executors.newScheduledThreadPool(1, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread t = Executors.defaultThreadFactory().newThread(r);
				t.setDaemon(true);
				return t;
			}
		});
		LOGGER.info("Created Scheduled executor service");
		scheduler.scheduleWithFixedDelay(new NgrokActivator(),
				1, NgrokActivator.SESSION_TIME, TimeUnit.MILLISECONDS);
		LOGGER.info(Colors.CYAN+"Launched first thread, next will be launched in "+NgrokActivator.SESSION_TIME+" ms"+Colors.RESET);
	}*/

}
