package com.kucoin.futures.core.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author blazetan
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarginModeResponse {

    /**
     * Symbol of the contract
     */
    private String symbol;

    /**
     * Margin mode: ISOLATED (isolated), CROSS (cross margin).
     */
    private String marginMode;
}
