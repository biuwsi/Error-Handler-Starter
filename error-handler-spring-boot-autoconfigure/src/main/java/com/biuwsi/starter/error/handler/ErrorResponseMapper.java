package com.biuwsi.starter.error.handler;

import com.biuwsi.starter.error.handler.configuration.ErrorCodeProperties;
import com.biuwsi.starter.error.handler.configuration.ErrorConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.UUID;

@RequiredArgsConstructor
public class ErrorResponseMapper {
    private static final String APP_VAR = "APP";
    private static final String APP_NAME = StringUtils.isEmpty(System.getProperty(APP_VAR))
            ? System.getenv(APP_VAR)
            : System.getProperty(APP_VAR);

    private final ErrorConfigurationProperties errorConfigurationProperties;

    public ResponseEntity<ErrorResponse> handleException(String errorCode) {
        ErrorCodeProperties errorConfiguration = errorConfigurationProperties.getConfiguration(errorCode);

        return convert(errorConfiguration, errorCode);
    }

    public ResponseEntity<ErrorResponse> handleException(Throwable throwable, String errorCode) {
        ErrorCodeProperties errorConfiguration = errorConfigurationProperties.getDefaultErrorConfiguration();

        return convert(errorConfiguration, errorCode);
    }

    private ResponseEntity<ErrorResponse> convert(ErrorCodeProperties errorConfiguration, String errorCode) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .applicationName(APP_NAME)
                .errorId(UUID.randomUUID().toString())
                .errorCode(errorConfiguration.getClientCode())
                .message(getMessageByLanguage(errorConfiguration, errorCode))
                .build();

        return ResponseEntity.status(errorConfiguration.getHttpCode())
                .body(errorResponse);
    }

    private String getMessageByLanguage(ErrorCodeProperties properties, String errorCode) {
        String language = LocaleContextHolder.getLocale().getLanguage();
        return properties.getMessage(language, errorConfigurationProperties.getDefaultLanguage())
                .orElse(errorCode);
    }
}
