package com.biuwsi.starter.error.handler;

import com.biuwsi.common.exceptions.BusinessException;
import com.biuwsi.starter.error.handler.configuration.ErrorCodeProperties;
import com.biuwsi.starter.error.handler.configuration.ErrorConfigurationProperties;
import com.biuwsi.starter.error.handler.configuration.ErrorStarterAutoConfiguration;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.Locale;

import static com.biuwsi.common.exceptions.DefaultBaseErrorCodes.NULL_POINTER_EXCEPTION;
import static com.biuwsi.starter.error.handler.TestErrorCodes.TEST_ERROR_CODE;
import static com.biuwsi.starter.error.handler.TestErrorCodes.TEST_ERROR_MISSING_MESSAGES;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestExceptionController.Application.class)
@ContextConfiguration(classes = {ErrorStarterAutoConfiguration.class, ErrorConfigurationProperties.class, WebMvcAutoConfiguration.class})
@EnableConfigurationProperties
public class TestExceptionController {

    private static final String NULL_POINTER_URI = "/null-pointer";
    private static final String TEST_ERROR_CODE_URI = "/test-error-code";
    private static final String TEST_ERROR_CODE_MISSING_MESSAGES_URI = "/test-error-missing-messages";
    private static final String UNABLE_TO_FIND_USER_URI = "/unable-to-find-user";
    private static final String RUNTIME_EXCEPTION_URI = "/runtime-exception";

    @Configuration
    static class Application {
        @RestController
        static class Controller {
            @GetMapping(path = NULL_POINTER_URI)
            public void nullPointer() {
                throw new NullPointerException();
            }

            @GetMapping(path = TEST_ERROR_CODE_URI)
            public void business() {
                throw new BusinessException(TestErrorCodes.TEST_ERROR_CODE);
            }

            @GetMapping(path = TEST_ERROR_CODE_MISSING_MESSAGES_URI)
            public void businessMissingMessages() {
                throw new BusinessException(TestErrorCodes.TEST_ERROR_MISSING_MESSAGES);
            }

            @GetMapping(path = UNABLE_TO_FIND_USER_URI)
            public void businessUnableToFindUser() {
                throw new BusinessException(NULL_POINTER_EXCEPTION);
            }

            @GetMapping(path = RUNTIME_EXCEPTION_URI)
            public void exception() {
                throw new RuntimeException("Exception");
            }
        }
    }

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ErrorConfigurationProperties errorConfigurationProperties;

    private MockMvc mockMvc;

    private static final String APPLICATION_NAME = "test-application";
    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    @BeforeEach
    void setupBeforeEach() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @BeforeAll
    static void setup() {
        System.setProperty("APP", APPLICATION_NAME);
        Locale.setDefault(DEFAULT_LOCALE);
    }

    @Test
    @DisplayName("Check NPE error handling")
    public void testNullPointer() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(NULL_POINTER_URI))
                .andDo(print());

        ErrorCodeProperties defaultErrorConfiguration = errorConfigurationProperties.getDefaultErrorConfiguration();

        checkError(resultActions, defaultErrorConfiguration.getHttpCode(), ErrorCodeProperties.DEFAULT_CLIENT_CODE,
                defaultErrorConfiguration.getMessage(DEFAULT_LOCALE.getLanguage(), errorConfigurationProperties.getDefaultLanguage()).get());
    }

    @Test
    @DisplayName("Check error handling with fully filled configuration")
    public void testErrorCode() throws Exception {
        String errorCode = TEST_ERROR_CODE.getCode();
        ErrorCodeProperties properties = errorConfigurationProperties.getConfiguration(errorCode);
        String message = message(properties, errorCode);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(TEST_ERROR_CODE_URI))
                .andDo(print());

        checkError(resultActions, properties.getHttpCode(), properties.getClientCode(), message);
    }

    @Test
    @DisplayName("Check error locale handling")
    public void testUnableToFindUserRuLocale() throws Exception {
        String locale = "ru";
        String errorCode = TEST_ERROR_CODE.getCode();
        ErrorCodeProperties properties = errorConfigurationProperties.getConfiguration(errorCode);
        String message = message(properties, errorCode, locale);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(TEST_ERROR_CODE_URI)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale))
                .andDo(print());

        checkError(resultActions, properties.getHttpCode(), properties.getClientCode(), message);
    }

    @Test
    @DisplayName("Check error locale handling - missing message, use default locale")
    public void testUnableToFindUserMissingMessage() throws Exception {
        String locale = "ch";
        String errorCode = TEST_ERROR_CODE.getCode();
        ErrorCodeProperties properties = errorConfigurationProperties.getConfiguration(errorCode);
        String message = message(properties, errorCode, locale);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(TEST_ERROR_CODE_URI)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale))
                .andDo(print());

        checkError(resultActions, properties.getHttpCode(), properties.getClientCode(), message);
    }

    @Test
    @DisplayName("Check error locale handling - missing messages")
    public void testMissingMessages() throws Exception {
        String locale = "ch";
        String errorCode = TEST_ERROR_MISSING_MESSAGES.getCode();
        ErrorCodeProperties properties = errorConfigurationProperties.getConfiguration(errorCode);
        String message = message(properties, errorCode, locale);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(TEST_ERROR_CODE_MISSING_MESSAGES_URI)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale))
                .andDo(print());

        checkError(resultActions, properties.getHttpCode(), properties.getClientCode(), message);
    }

    private ResultActions checkError(ResultActions resultActions, Integer httpCode, String errorCode, String message) throws Exception {
        return resultActions
                .andExpect(status().is(httpCode))
                .andExpect(jsonPath("$.errorId", Matchers.not(Matchers.isEmptyOrNullString())))
                .andExpect(jsonPath("$.errorCode", Matchers.equalTo(errorCode)))
                .andExpect(jsonPath("$.message", Matchers.equalTo(message)))
                .andExpect(jsonPath("$.applicationName", Matchers.equalTo(APPLICATION_NAME)));
    }

    private String message(ErrorCodeProperties properties, String errorCode, String locale) {
        return properties.getMessage(locale, errorConfigurationProperties.getDefaultLanguage())
                .orElse(errorCode);
    }

    private String message(ErrorCodeProperties properties, String errorCode) {
        return message(properties, errorCode, DEFAULT_LOCALE.getLanguage());
    }
}
