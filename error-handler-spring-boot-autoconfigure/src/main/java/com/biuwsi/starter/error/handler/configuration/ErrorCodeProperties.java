package com.biuwsi.starter.error.handler.configuration;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Map;
import java.util.Optional;

@Data
public class ErrorCodeProperties {
    public static final String DEFAULT_CLIENT_CODE = "INTERNAL_ERROR";

    @NotEmpty
    private String clientCode = DEFAULT_CLIENT_CODE;

    @Min(100)
    @Max(599)
    private Integer httpCode = 500;

    @NotEmpty
    private Map<@NotEmpty String, @NotEmpty String> messages;

    public Optional<String> getMessage(String language, String defaultLanguage) {
        return Optional.ofNullable(messages.get(language))
                .or(() -> Optional.ofNullable(messages.get(defaultLanguage)));
    }
}
