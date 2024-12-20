/*
 * Copyright 2019 Mek Global Limited
 */

package com.kucoin.futures.core.websocket.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenshiwei
 * @since 2020/6/28
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StopOrderLifecycleEvent {

    private String orderId;

    private String symbol;

    private String type;

    private String orderType;

    private String side;

    private Integer size;

    private BigDecimal orderPrice;

    private String stop;

    private BigDecimal stopPrice;

    private String stopPriceType;

    private Boolean triggerSuccess;

    private String error;

    private Long createdAt;

    private Long ts;

    private String marginMode;

}
