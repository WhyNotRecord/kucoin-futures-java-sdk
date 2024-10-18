package com.kucoin.futures.core.rest.request;

import lombok.Builder;
import lombok.Data;

/**
 * @author isaac.tang
 */
@Data
@Builder
public class ChangeCrossUserLeverageRequest {

    /**
     * Symbol of the contract
     */
    private String symbol;

    /**
     * Leverage multiple
     */
    private String leverage;
}
