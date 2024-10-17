/*
 * Copyright 2019 Mek Global Limited
 */

package com.kucoin.futures.core.rest.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MaxOpenSizeRequest {

    /**
     * Contract symbol
     */
    private String symbol;

    /**
     * Order price
     */
    private BigDecimal price;

    /**
     * Leverage
     */
    private BigDecimal leverage;
}
