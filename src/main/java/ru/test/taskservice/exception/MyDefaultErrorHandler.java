package ru.test.taskservice.exception;

import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

public class MyDefaultErrorHandler {
    @Bean
    public DefaultErrorHandler errorHandler(ConsumerRecordRecoverer recoverer) {
        DefaultErrorHandler handler = new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 2L));
        handler.addNotRetryableExceptions(JsonParseException.class);
        return handler;
    }
}
