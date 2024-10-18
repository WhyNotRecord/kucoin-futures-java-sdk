package com.kucoin.futures.core.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

/**
 * @author blazetan
 */
@Data
@Builder
public class ChangeMarginRequest {

    /**
     * Symbol of the contract
     */
    private String symbol;

    /**
     * Margin mode: ISOLATED (isolated), CROSS (cross margin).
     */
    private String marginMode;
}
