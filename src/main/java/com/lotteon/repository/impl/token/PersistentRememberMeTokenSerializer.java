package com.lotteon.repository.impl.token;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import java.io.IOException;

public class PersistentRememberMeTokenSerializer extends JsonSerializer<PersistentRememberMeToken> {
    @Override
    public void serialize(PersistentRememberMeToken token, JsonGenerator jsonGenerator,
                          com.fasterxml.jackson.databind.SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("username", token.getUsername());
        jsonGenerator.writeStringField("series", token.getSeries());
        jsonGenerator.writeStringField("tokenValue", token.getTokenValue());
        jsonGenerator.writeNumberField("date", token.getDate().getTime());
        jsonGenerator.writeEndObject();
    }
}