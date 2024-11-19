package com.movesmart.busdatamanager.core;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movessmart.busdatamanager.core.infrastructure.api.ErrorResponseHandler;
import java.nio.charset.StandardCharsets;
import lombok.NoArgsConstructor;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;

@NoArgsConstructor
public class Fixtures {

    public static MockMvc setupMockMvc(Object... controllers) {
        return standaloneSetup(controllers)
                .setControllerAdvice(new ErrorResponseHandler())
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build();
    }

    public static ObjectMapper setupObjectMapper() {
        return new Jackson2ObjectMapperBuilder().build();
    }
}
