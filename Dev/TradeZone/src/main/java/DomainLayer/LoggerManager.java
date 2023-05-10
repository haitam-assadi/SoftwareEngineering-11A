package DomainLayer;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerManager {

    private Logger eventLogger;
    private Logger errorLogger;

    private FileHandler eventHandler;
    private FileHandler errorHandler;

    private String evenLoggerFile = "eventLogger.txt";
    private String errorLoggerFile = "errorLogger.txt";

    private static LoggerManager instance = null;


    private LoggerManager(){
        try {

            eventLogger = Logger.getLogger("event_log");
            errorLogger = Logger.getLogger("error_log");

            eventHandler = new FileHandler(evenLoggerFile,true);
            eventHandler.setFormatter(new SimpleFormatter());

            errorHandler = new FileHandler(errorLoggerFile,true);
            errorHandler.setFormatter(new SimpleFormatter());

            eventLogger.addHandler(eventHandler);
            errorLogger.addHandler(errorHandler);

        }
        catch (IOException e){

        }
    }

    public static LoggerManager getInstance(){
        if(instance == null){
            instance =  new LoggerManager();
        }
        return instance;
    }

    public void sendEventLog(String msg){
        eventLogger.info(msg);
    }

    public void sendErrorLog(String msg){
        errorLogger.warning(msg);
    }
}
