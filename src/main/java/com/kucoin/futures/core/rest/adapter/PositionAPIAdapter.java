/*
 * Copyright 2019 Mek Global Limited
 */
package com.kucoin.futures.core.rest.adapter;

import com.kucoin.futures.core.rest.impl.retrofit.AuthRetrofitAPIImpl;
import com.kucoin.futures.core.rest.interceptor.FuturesApiKey;
import com.kucoin.futures.core.rest.interfaces.PositionAPI;
import com.kucoin.futures.core.rest.interfaces.retrofit.PositionAPIRetrofit;
import com.kucoin.futures.core.rest.request.*;
import com.kucoin.futures.core.rest.response.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author chenshiwei
 * @since 2019/10/14
 */
public class PositionAPIAdapter extends AuthRetrofitAPIImpl<PositionAPIRetrofit> implements PositionAPI {

    public PositionAPIAdapter(String baseUrl, FuturesApiKey apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    @Override
    public PositionResponse getPosition(String symbol) throws IOException {
        return super.executeSync(getAPIImpl().getPosition(symbol));
    }

    @Override
    public List<PositionResponse> getPositions() throws IOException {
        return super.executeSync(getAPIImpl().getPositions());
    }

    @Override
    public MaxOpenSizeResponse getMaxOpenSize(MaxOpenSizeRequest request) throws IOException {
        return super.executeSync(getAPIImpl().getMaxOpenSize(request.getSymbol(), request.getPrice(), request.getLeverage()));
    }

    @Override
    public Pagination<HistoryPositionResponse> getHistoryPositions(HistoryPositionsRequest request) throws IOException {
        return super.executeSync(getAPIImpl().getHistoryPositions(request.getSymbol(),
                request.getFrom(),
                request.getTo(),
                request.getLimit(),
                request.getPageId())
        );
    }

    @Override
    public MarginModeResponse getMarginMode(String symbol) throws IOException {
        return super.executeSync(getAPIImpl().getMarginMode(symbol));
    }

    @Override
    public MarginModeResponse changeMarginMode(ChangeMarginRequest request) throws IOException {
        return super.executeSync(getAPIImpl().changeMarginMode(request));
    }

    @Override
    public GetCrossUserLeverageResponse getCrossUserLeverage(String symbol) throws IOException {
        return super.executeSync(getAPIImpl().getCrossUserLeverage(symbol));
    }

    @Override
    public boolean changeCrossUserLeverage(ChangeCrossUserLeverageRequest req) throws IOException {
        return super.executeSync(getAPIImpl().changeCrossUserLeverage(req));
    }

    @Override
    public void setAutoDepositMargin(String symbol, boolean status) throws IOException {
        UpdateAutoDepositMarginRequest request = UpdateAutoDepositMarginRequest.builder().status(status).symbol(symbol).build();
        super.executeSync(getAPIImpl().setAutoDepositMargin(request));
    }

    @Override
    public BigDecimal getMaxWithdrawMargin(String symbol) throws IOException {
        return super.executeSync(getAPIImpl().getMaxWithdrawMargin(symbol));
    }

    @Override
    public BigDecimal withdrawMargin(WithdrawMarginRequest request) throws IOException {
        return super.executeSync(getAPIImpl().withdrawMargin(request));
    }

    @Override
    public void addMarginManually(String symbol, BigDecimal margin, String bizNo) throws IOException {
        AddMarginManuallyRequest request = AddMarginManuallyRequest.builder().symbol(symbol).margin(margin).bizNo(bizNo).build();
        super.executeSync(getAPIImpl().addMarginManually(request));
    }
}
