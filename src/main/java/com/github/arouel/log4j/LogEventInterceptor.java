package com.github.arouel.log4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Property;

/**
 * A utility which helps intercepting Apache Log4j 2 events from any given logger.
 */
public final class LogEventInterceptor implements AutoCloseable {

    private final LogEventAppender _appender;

    private final String _loggerConfigName;

    private final LoggerContext _loggerContext = (LoggerContext) LogManager.getContext(false);

    private final Optional<LoggerConfig> _registeredLogConfig;

    private LogEventInterceptor(String loggerConfigName, LogEventAppender appender) {
        _loggerConfigName = loggerConfigName;
        _appender = appender;
        _loggerContext.getConfiguration().addAppender(appender);

        LoggerConfig loggerConfig = _loggerContext.getConfiguration().getLoggerConfig(loggerConfigName);
        if (!loggerConfig.getName().equals(_loggerConfigName)) {
            LoggerConfig newLogger = new LoggerConfig(loggerConfigName, Level.ALL, true);
            _loggerContext.getConfiguration().addLogger(loggerConfigName, newLogger);
            _loggerContext.updateLoggers();
            _registeredLogConfig = Optional.of(newLogger);
        } else {
            _registeredLogConfig = Optional.empty();
        }
        _loggerContext.getConfiguration().getLoggerConfig(loggerConfigName).addAppender(_loggerContext.getConfiguration().getAppender(appender.getName()), null, null);
    }

    public static LogEventInterceptor register(Class<?> classToInterceptLogEventsFrom) {
        return register(classToInterceptLogEventsFrom.getName());
    }

    public static LogEventInterceptor register(String loggerConfigName) {
        LogEventAppender appender = new LogEventAppender("logMessageInterceptor-" + UUID.randomUUID().toString());
        appender.start();
        return new LogEventInterceptor(loggerConfigName, appender);
    }

    @Override
    public void close() {
        _loggerContext.getConfiguration().getLoggerConfig(_loggerConfigName).removeAppender(_appender.getName());
        _registeredLogConfig.ifPresent(logConfig -> _loggerContext.getConfiguration().removeLogger(logConfig.getName()));
    }

    public List<LogEvent> events() {
        return Collections.unmodifiableList(_appender.events());
    }

    public List<String> messages() {
        return _appender
                .events()
                .stream()
                .map(e -> e.getMessage().getFormattedMessage())
                .collect(Collectors.toList());
    }

    private static class LogEventAppender extends AbstractAppender {

        private final List<LogEvent> _events = new ArrayList<>();

        LogEventAppender(String name) {
            super(name, null, null, true, new Property[0]);
        }

        @Override
        public void append(LogEvent logEvent) {
            _events.add(logEvent.toImmutable());
        }

        List<LogEvent> events() {
            return _events;
        }

    }
}
