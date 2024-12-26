package com.lotteon.repository.impl.token;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import java.io.IOException;
import java.util.Date;

public class PersistentRememberMeTokenDeserializer extends JsonDeserializer<PersistentRememberMeToken> {
    @Override
    public PersistentRememberMeToken deserialize(JsonParser jsonParser,
                                                 DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String username = node.get("username").asText();
        String series = node.get("series").asText();
        String tokenValue = node.get("tokenValue").asText();
        Date date = new Date(node.get("date").asLong());
        return new PersistentRememberMeToken(username, series, tokenValue, date);
    }
}