package com.hospital.management.hospitalmanagementsystem.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer stringTrimmer() {
        return builder -> builder.deserializerByType(
                String.class,
                new JsonDeserializer<String>() {
                    @Override
                    public String deserialize(
                            JsonParser parser,
                            DeserializationContext context
                    ) throws IOException {

                        String value = parser.getValueAsString();

                        if (value == null) {
                            return null;
                        }

                        value = value.trim();

                        return value.isEmpty() ? null : value;
                    }
                }
        );
    }
}