package com.biuwsi.starter.error.handler.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@Validated
@ConfigurationProperties("errors")
public class ErrorConfigurationProperties {
    @NotEmpty
    private Map<@NotEmpty String, @Valid ErrorCodeProperties> errorCodes;

    @NotNull
    @Valid
    private ErrorCodeProperties defaultErrorConfiguration;

    @NotNull
    private String defaultLanguage;

    public ErrorCodeProperties getConfiguration(String code) {
        return errorCodes.getOrDefault(code, defaultErrorConfiguration);
    }
}
