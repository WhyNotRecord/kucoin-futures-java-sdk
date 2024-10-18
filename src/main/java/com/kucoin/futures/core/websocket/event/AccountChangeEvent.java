/*
 * Copyright 2019 Mek Global Limited
 */
package com.kucoin.futures.core.websocket.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by chenshiwei on 2019/1/23.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountChangeEvent {

    private String currency; // Currency Symbol
    private String walletBalance; // Wallet Balance
    private String availableBalance; // Available Balance
    private String holdBalance; // Frozen Balance
    private String isolatedOrderMargin; // Margin of the isolated margin order
    private String isolatedPosMargin; // Margin of the isolated margin position
    private String isolatedUnPnl; // Unrealized P&L in isolated margin mode
    private String isolatedFundingFeeMargin; // Isolated margin funding fee
    private String crossOrderMargin; // Margin of the cross margin order
    private String crossPosMargin; // Margin of the cross margin position
    private String crossUnPnl; // Unrealized P&L in cross margin mode
    private String equity; // Equity
    private String totalCrossMargin; // Total margin under cross margin mode
    private String version; // Version
    private String timestamp; // Last modified time
}
