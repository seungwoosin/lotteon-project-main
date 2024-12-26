package com.lotteon.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper; // 추가
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class RedisTokenRepositoryImpl implements PersistentTokenRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper; // 추가
    private final String REMEMBER_ME_KEY_PREFIX = "rememberMe:";

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        try {
            String tokenJson = objectMapper.writeValueAsString(token);
            redisTemplate.opsForValue().set(REMEMBER_ME_KEY_PREFIX + token.getSeries(), tokenJson);
        } catch (Exception e) {
            // 예외 처리
        }
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        PersistentRememberMeToken token = getTokenForSeries(series);
        if (token != null) {
            PersistentRememberMeToken newToken = new PersistentRememberMeToken(
                    token.getUsername(), series, tokenValue, lastUsed);
            try {
                String tokenJson = objectMapper.writeValueAsString(newToken);
                redisTemplate.opsForValue().set(REMEMBER_ME_KEY_PREFIX + series, tokenJson);
            } catch (Exception e) {
                // 예외 처리: 로그 남기기
                e.printStackTrace(); // 예를 들어, 로그 프레임워크를 사용
            }
        }
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        String tokenJson = (String) redisTemplate.opsForValue().get(REMEMBER_ME_KEY_PREFIX + seriesId);
        if (tokenJson != null) {
            try {
                return objectMapper.readValue(tokenJson, PersistentRememberMeToken.class);
            } catch (Exception e) {
                // 예외 처리
            }
        }
        return null;
    }

    @Override
    public void removeUserTokens(String username) {
        redisTemplate.delete(REMEMBER_ME_KEY_PREFIX + username);
    }
}