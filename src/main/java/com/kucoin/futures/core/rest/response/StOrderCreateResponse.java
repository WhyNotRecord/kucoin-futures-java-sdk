/*
 * Copyright 2019 Mek Global Limited
 */
package com.kucoin.futures.core.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StOrderCreateResponse {
    /**
     * Order ID
     */
    private String orderId;
    /**
     * Client order ID
     */
    private String clientOid;
}