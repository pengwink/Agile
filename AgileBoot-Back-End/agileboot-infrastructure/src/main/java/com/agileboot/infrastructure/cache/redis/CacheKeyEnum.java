package com.agileboot.infrastructure.cache.redis;

import java.util.concurrent.TimeUnit;

/**
 * @author valarchie
 */
public enum CacheKeyEnum {

    /**
     * Redis各类缓存集合
     */
    CAPTCHAT("captcha_codes:", 2, TimeUnit.MINUTES),
    LOGIN_USER_KEY("login_tokens:", 6, TimeUnit.HOURS),
    RATE_LIMIT_KEY("rate_limit:", 60, TimeUnit.SECONDS),
    USER_ENTITY_KEY("user_entity:", 6, TimeUnit.HOURS),
    ROLE_ENTITY_KEY("role_entity:", 6, TimeUnit.HOURS),
    POST_ENTITY_KEY("post_entity:", 6, TimeUnit.HOURS),
    ROLE_MODEL_INFO_KEY("role_model_info:", 6, TimeUnit.HOURS),

    ;


    CacheKeyEnum(String key, int expiration, TimeUnit timeUnit) {
        this.key = key;
        this.expiration = expiration;
        this.timeUnit = timeUnit;
    }

    private final String key;
    private final int expiration;
    private final TimeUnit timeUnit;

    public String key() {
        return key;
    }

    public int expiration() {
        return expiration;
    }

    public TimeUnit timeUnit() {
        return timeUnit;
    }

}
