/*
 * Copyright 2019 Mek Global Limited
 */
package com.kucoin.futures.core.rest.interfaces;

import com.kucoin.futures.core.rest.response.TickerResponse;

import java.io.IOException;
import java.util.List;

/**
 * @author chenshiwei
 * @since 2019/10/14
 */
public interface TickerAPI {

    /**
     * The real-time ticker includes the last traded price, the last traded size, transaction ID,
     * the side of liquidity taker, the best bid price and size, the best ask price and size as well as the transaction time of the orders.
     * These messages can also be obtained through Websocket.
     * The Sequence Number is used to judge whether the messages pushed by Websocket is continuous.
     *
     * @param symbol Symbol of the contract
     * @return
     * @throws IOException
     */
    TickerResponse getTicker(String symbol) throws IOException;


    /**
     * Get Latest Ticker for All Contracts
     * @return
     * @throws IOException
     */
    List<TickerResponse> getAllTickers() throws IOException;

}
