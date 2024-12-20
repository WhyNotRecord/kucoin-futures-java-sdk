/*
 * Copyright 2019 Mek Global Limited
 */
package com.kucoin.futures.core.rest.adapter;

import com.kucoin.futures.core.rest.interceptor.FuturesApiKey;
import com.kucoin.futures.core.rest.interfaces.OrderAPI;
import com.kucoin.futures.core.rest.interfaces.retrofit.OrderAPIRetrofit;
import com.kucoin.futures.core.rest.request.OrderCreateApiRequest;
import com.kucoin.futures.core.rest.impl.retrofit.AuthRetrofitAPIImpl;
import com.kucoin.futures.core.rest.request.DuringPageRequest;
import com.kucoin.futures.core.rest.request.StOrderCreateRequest;
import com.kucoin.futures.core.rest.response.*;
import retrofit2.http.Query;

import java.io.IOException;
import java.util.List;

/**
 * Created by chenshiwei on 2019/1/18.
 */
public class OrderAPIAdapter extends AuthRetrofitAPIImpl<OrderAPIRetrofit> implements OrderAPI {

    public OrderAPIAdapter(String baseUrl, FuturesApiKey apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    @Override
    public OrderCreateResponse createOrder(OrderCreateApiRequest opsRequest) throws IOException {
        return executeSync(getAPIImpl().createOrder(opsRequest));
    }

    @Override
    public List<OrderCreateMultiResponse> createOrderMulti(List<OrderCreateApiRequest> requests) throws IOException {
        return executeSync(getAPIImpl().createOrderMulti(requests));
    }

    @Override
    public OrderCreateResponse createOrderTest(OrderCreateApiRequest opsRequest) throws IOException {
        return executeSync(getAPIImpl().createOrderTest(opsRequest));
    }

    @Override
    public StOrderCreateResponse createStOrders(StOrderCreateRequest request) throws IOException {
        return executeSync(getAPIImpl().createStOrders(request));
    }

    @Override
    public OrderCancelResponse cancelOrder(String orderId) throws IOException {
        return executeSync(getAPIImpl().cancelOrder(orderId));
    }

    @Override
    public OrderCancelByClientOidResponse cancelOrderByClientOid(String clientOid, String symbol) throws IOException {
        return executeSync(getAPIImpl().cancelOrderByClientOid(clientOid, symbol));
    }

    @Override
    public OrderCancelResponse cancelAllLimitOrders(String symbol) throws IOException {
        return executeSync(getAPIImpl().cancelLimitOrders(symbol));
    }

    @Override
    public OrderCancelResponse cancelAllStopOrders(String symbol) throws IOException {
        return executeSync(getAPIImpl().cancelStopOrders(symbol));
    }

    @Override
    public OrderResponse getOrderDetail(String orderId) throws IOException {
        return executeSync(getAPIImpl().getOrder(orderId));
    }

    @Override
    public OrderResponse getOrderDetailByClientOid(String clientOid) throws IOException {
        return executeSync(getAPIImpl().getOrderByClientOid(clientOid));
    }

    @Override
    public Pagination<OrderResponse> getOrderList(String symbol, String side, String type,
                                                  String status, DuringPageRequest request) throws IOException {
        if (request == null) request = DuringPageRequest.builder().build();
        return executeSync(getAPIImpl().queryOrders(symbol, side, type, status,
                request.getStarAt(), request.getEndAt(), request.getPageSize(), request.getCurrentPage()));
    }

    @Override
    public Pagination<OrderResponse> getUntriggeredStopOrderList(String symbol, String side, String type,
                                                                 DuringPageRequest request) throws IOException {
        if (request == null) request = DuringPageRequest.builder().build();
        return executeSync(getAPIImpl().queryStopOrders(symbol, side, type,
                request.getStarAt(), request.getEndAt(), request.getPageSize(), request.getCurrentPage()));
    }

    @Override
    public List<OrderResponse> getRecentDoneOrders() throws IOException {
        return executeSync(getAPIImpl().queryRecentDoneOrders());
    }

    @Override
    public TradeFeeResponse getTradeFee(String symbol) throws IOException {
        return executeSync(getAPIImpl().getTradeFee(symbol));
    }

}
