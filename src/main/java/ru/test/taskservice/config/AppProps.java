package ru.test.taskservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


@ConfigurationProperties("app")
@Getter
@Setter
@Component
public class AppProps {

    @NonNull
    private Integer poolSize;

    @NonNull
    private Integer maxPoolSize;

}

