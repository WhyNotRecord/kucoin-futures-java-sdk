/*
 * Copyright 2019 Mek Global Limited
 */
package com.kucoin.futures.core.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderResponse {

    private String id;

    private String symbol;

    private String type;

    private String side;

    private BigDecimal price;

    private BigDecimal size;

    private BigDecimal value;

    private BigDecimal dealValue;

    private BigDecimal dealSize;

    private String stp;

    private String stop;

    private String stopPriceType;

    private Boolean stopTriggered;

    private BigDecimal stopPrice;

    private String timeInForce;

    private boolean postOnly;

    private boolean hidden;

    private boolean iceberg;

    private BigDecimal visibleSize;

    private String leverage;

    private boolean forceHold;

    private boolean closeOrder;

    private String clientOid;

    private String remark;

    private String tags;

    @JsonProperty("isActive")
    private boolean isActive;

    private boolean cancelExist;

    private Date createdAt;

    private Date updatedAt;

    private Date endAt;

    private Long orderTime;

    private String settleCurrency;

    private String status;

    private BigDecimal filledSize;

    private BigDecimal filledValue;

    private boolean reduceOnly;

    /**
     * Margin mode: ISOLATED, CROSS
     */
    private String marginMode;

    public String getStop() {
        return this.stop == null ? null : this.stop.toLowerCase();
    }

    public String getType() {
        return this.type == null ? null : this.type.toLowerCase();
    }

    public String getSide() {
        return this.side == null ? null : this.side.toLowerCase();
    }

}
