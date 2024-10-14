package com.kucoin.futures.core.rest.interceptor;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

/**
 * Created by Luka on 2021/3/16.
 */
@Builder
@Data
public class FuturesApiKey {

    private String key;

    private String secret;

    private String passPhrase;

    private String keyVersion;

    @Override
    public int hashCode() {
        return Objects.hash(key, secret, passPhrase, keyVersion);
    }
}
