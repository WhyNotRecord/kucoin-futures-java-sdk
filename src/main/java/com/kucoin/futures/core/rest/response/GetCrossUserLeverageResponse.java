package com.kucoin.futures.core.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author isaac.tang
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetCrossUserLeverageResponse {
    /**
     * Symbol of the contract
     */
    private String symbol;

    /**
     * Leverage multiple
     */
    private String leverage;
}
