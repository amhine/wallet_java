package config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerConfig {

	private static boolean initialized = false;
    private final static String LOG_FILE = "logs/app.log";
    
    public static void init() {
        if (initialized) return;
        initialized = true;

        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.ALL);

        for (Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }

        try {
            Files.createDirectories(Paths.get(LOG_FILE).getParent());

            FileHandler fileHandler = new FileHandler(LOG_FILE, true);
            fileHandler.setFormatter(new SimpleFormatter());
            rootLogger.addHandler(fileHandler);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }
}
