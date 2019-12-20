package com.biuwsi.starter.error.handler.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(value = ErrorConfigurationProperties.class)
@Import(ErrorHandlerConfiguration.class)
public class ErrorStarterAutoConfiguration {
}
