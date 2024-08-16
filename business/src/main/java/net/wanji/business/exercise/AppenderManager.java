package net.wanji.business.exercise;

/**
 * @author: jenny
 * @create: 2024-08-14 5:16 下午
 */
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.encoder.Encoder;
import net.wanji.business.exercise.enums.LogTypeEnum;
import net.wanji.common.config.WanjiConfig;
import net.wanji.common.utils.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class AppenderManager {

    private static final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    private static final ConcurrentHashMap<String, Appender<ILoggingEvent>> appenderMap = new ConcurrentHashMap<>();
    private static final String logPattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{20} - [%method,%line] - %msg%n";
    private static final String loggerNameTemplate = "logger-{}-{}";
    private static final String filePathTemplate = "{}{}.log";
    private static final String appenderNameTemplate = "FileAppender-{}-{}";

    // 根据 taskId 创建 appender
    public static Logger createAppender(Long taskId, String logTypeName, boolean isCompetition) {
        String loggerName = StringUtils.format(loggerNameTemplate, taskId, logTypeName);
        String appenderName = StringUtils.format(appenderNameTemplate, taskId, logTypeName);
        if (appenderMap.containsKey(appenderName)) {
            return loggerContext.getLogger(loggerName); // 已经存在，不需要重新创建
        }
        String taskTracePath = WanjiConfig.getTaskTracePath(taskId, isCompetition);
        String filePath = StringUtils.format(filePathTemplate, taskTracePath, logTypeName);
        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setName(appenderName);
        fileAppender.setFile(filePath);
        fileAppender.setContext(loggerContext);
        fileAppender.setEncoder(createEncoder());
        fileAppender.start();

        //获取指定名称的logger
        Logger logger = loggerContext.getLogger(loggerName);
        logger.setLevel(Level.INFO);
        logger.addAppender(fileAppender);
        if(!logTypeName.equals(LogTypeEnum.COMMAND.getName())){
            logger.setAdditive(false);
        }
        appenderMap.put(appenderName, fileAppender);
        return logger;
    }

    // 创建 encoder
    private static Encoder<ILoggingEvent> createEncoder() {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern(logPattern);
        encoder.start();
        return encoder;
    }

    // 根据 taskId 销毁 appender
    public static void destroyAppender(Long taskId, String logTypeName) {
        String loggerName = StringUtils.format(loggerNameTemplate, taskId, logTypeName);
        String appenderName = StringUtils.format(appenderNameTemplate, taskId, logTypeName);
        Appender<ILoggingEvent> appender = appenderMap.remove(appenderName);
        if (appender != null) {
            appender.stop();
            loggerContext.getLogger(loggerName).detachAppender(appender);
        }
    }
}
