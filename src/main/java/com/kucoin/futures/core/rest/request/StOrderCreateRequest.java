/*
 * Copyright 2019 Mek Global Limited
 */
package com.kucoin.futures.core.rest.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class StOrderCreateRequest {

    // Required parameters
    private String clientOid; // Unique order id created by users
    private String side; // buy or sell
    private String symbol; // contract code e.g. XBTUSDM
    private BigDecimal leverage; // Used to calculate the margin to be frozen for the order

    // Optional parameters
    private String type; // limit or market, default is limit
    private String remark; // remark for the order
    private BigDecimal triggerStopUpPrice; // Take profit price
    private String stopPriceType; // TP, IP or MP
    private BigDecimal triggerStopDownPrice; // Stop loss price
    private boolean reduceOnly = false; // default to false
    private boolean closeOrder = false; // default to false
    private boolean forceHold = false; // default to false
    private String stp; // self trade prevention: CN, CO, CB
    private String marginMode; // ISOLATED , CROSS

    // Additional request parameters required by limit orders
    private BigDecimal price; // Limit price
    private Integer size; // Order size (positive number)
    private String timeInForce; // GTC, IOC (default GTC)
    private boolean postOnly = false; // default to false
    private boolean hidden = false; // default to false
    private boolean iceberg = false; // default to false
    private Integer visibleSize; // Max visible size for iceberg order

    // Additional request parameters required by market orders
    private Integer marketOrderSize; // contract amount to buy or sell
}