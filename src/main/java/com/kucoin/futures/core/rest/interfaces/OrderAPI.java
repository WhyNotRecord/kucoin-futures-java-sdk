/*
 * Copyright 2019 Mek Global Limited
 */
package com.kucoin.futures.core.rest.interfaces;

import com.kucoin.futures.core.rest.request.DuringPageRequest;
import com.kucoin.futures.core.rest.request.OrderCreateApiRequest;
import com.kucoin.futures.core.rest.request.StOrderCreateRequest;
import com.kucoin.futures.core.rest.response.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by chenshiwei on 2019/1/9.
 */
public interface OrderAPI {

    /**
     * Place a new order
     * <p>
     * You can place two types of orders: limit and market. Orders can only be placed if your account has sufficient funds.
     * Once an order is placed, your account funds will be put on hold for the duration of the order.
     * How much and which funds are put on hold depends on the order type and parameters specified.
     * <p>
     * Please be noted that the system would hold the fees from the orders entered the orderbook in advance. Read Get Fills to learn more.
     *
     * Do NOT include extra spaces in JSON strings.
     *
     * Place Order Limit:
     * The maximum limit orders for a single contract is 100 per account, and the maximum stop orders for a single contract is 50 per account.
     * @param opsRequest
     * @return A response containing the order id.
     */
    OrderCreateResponse createOrder(OrderCreateApiRequest opsRequest) throws IOException;

    /**
     * Place Multiple Orders
     * You can place up to 20 orders at one time, including limit orders, market orders, and stop orders
     *
     * @param requests
     * @return
     * @throws IOException
     */
    List<OrderCreateMultiResponse> createOrderMulti(List<OrderCreateApiRequest> requests) throws IOException;

    /**
     * Order test endpoint, the request parameters and return parameters of this endpoint are exactly the same as the order endpoint, and can be used to verify whether the signature is correct and other operations.
     * After placing an order, the order will not enter the matching system, and the order cannot be queried.
     *
     * @param opsRequest
     * @return
     * @throws IOException
     */
    OrderCreateResponse createOrderTest(OrderCreateApiRequest opsRequest) throws IOException;


    /**
     * This interface supports both take-profit and stop-loss functions, and other functions are exactly the same as the place order interface.
     * You can place two types of orders: limit and market. Orders can only be placed if your account has sufficient funds. Once an order is placed,
     *  your funds will be put on hold for the duration of the order. The amount of funds on hold depends on the order type and parameters specified.
     * Please be noted that the system would hold the fees from the orders entered the orderbook in advance. Read Get Fills to learn more.
     * @param request
     * @return
     * @throws IOException
     */
    StOrderCreateResponse createStOrders(StOrderCreateRequest request) throws IOException;

    /**
     * Cancel an order
     * <p>
     * Cancel a previously placed order.
     *
     * @param orderId
     * @return A response containing the id of the cancelled order.
     */
    OrderCancelResponse cancelOrder(String orderId) throws IOException;

    /**
     * Cancel Order by clientOid
     *
     * @param clientOid
     * @param symbol
     * @return OrderCancelByClientOidResponse
     * @throws IOException
     */
    OrderCancelByClientOidResponse cancelOrderByClientOid(String clientOid, String symbol) throws IOException;

    /**
     * Cancel all open orders (excluding stop orders). The response is a list of orderIDs of the canceled orders.
     *
     * @param symbol [optional] Cancel all limit orders for a specific contract only
     * @return A response containing the ids of all open orders.
     */
    OrderCancelResponse cancelAllLimitOrders(String symbol) throws IOException;

    /**
     * Cancel all untriggered stop orders. The response is a list of orderIDs of the canceled stop orders.
     * To cancel triggered stop orders, please use 'Limit Order Mass Cancelation'.
     *
     * @param symbol [optional] Cancel all limit orders for a specific contract only
     * @return A response containing the ids of all open orders.
     */
    OrderCancelResponse cancelAllStopOrders(String symbol) throws IOException;

    /**
     * List your current orders.
     *
     * @param symbol      [optional] Symbol of the contract
     * @param side        [optional] buy or sell
     * @param type        [optional] limit, market, limit_stop or market_stop
     * @param status      [optional] active or done, done as default. Only list orders for a specific status
     * @param request     [optional] include startAt endAt currentPage and pageSize parameters
     * @return A page of orders.
     */
    Pagination<OrderResponse> getOrderList(String symbol, String side, String type, String status,
                                           DuringPageRequest request) throws IOException;

    /**
     * Get the un-triggered stop orders list.
     *
     * @param symbol      [optional] Symbol of the contract
     * @param side        [optional] buy or sell
     * @param type        [optional] limit, market
     * @param request     [optional] include startAt endAt currentPage and pageSize parameters
     * @return A page of orders.
     * @throws IOException
     */
    Pagination<OrderResponse> getUntriggeredStopOrderList(String symbol, String side, String type, DuringPageRequest request) throws IOException;

    /**
     * Get a list of recent 1000 orders in the last 24 hours.
     *
     * If you need to get your recent traded order history with low latency, you may query this endpoint.
     *
     * @return
     * @throws IOException
     */
    List<OrderResponse> getRecentDoneOrders() throws IOException;

    /**
     * Get a single order by order id (including a stop order).
     *
     * @param orderId
     * @return The requested order.
     */
    OrderResponse getOrderDetail(String orderId) throws IOException;


    /**
     * Get a single order by clientOid
     *
     * @param clientOid
     * @return The requested order.
     */
    OrderResponse getOrderDetailByClientOid(String clientOid) throws IOException;
    
    /**
     * This interface is for the actual fee rate of the trading pair.
     * The fee rate of your sub-account is the same as that of the master account.
     *
     * @param symbol Symbol of the contract
     * @return
     * @throws IOException
     */
    TradeFeeResponse getTradeFee(String symbol) throws IOException;
}
