package com.biuwsi.starter.error.handler.configuration;

import com.biuwsi.starter.error.handler.BaseErrorHandler;
import com.biuwsi.starter.error.handler.ErrorResponseMapper;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Configuration
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
public class ErrorHandlerConfiguration {
    @Bean
    public ErrorResponseMapper errorResponseMapper(ErrorConfigurationProperties errorConfigurationProperties) {
        return new ErrorResponseMapper(errorConfigurationProperties);
    }

    @ControllerAdvice
    public class Adviser extends BaseErrorHandler {
    }
}
