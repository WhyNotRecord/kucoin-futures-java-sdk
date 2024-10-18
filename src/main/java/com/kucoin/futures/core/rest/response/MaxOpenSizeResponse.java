/*
 * Copyright 2019 Mek Global Limited
 */

package com.kucoin.futures.core.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MaxOpenSizeResponse {

    /**
     * Contract symbol
     */
    private String symbol;

    /**
     * Maximum buy size
     */
    private BigDecimal maxBuyOpenSize;

    /**
     * Maximum sell size
     */
    private BigDecimal maxSellOpenSize;
}
