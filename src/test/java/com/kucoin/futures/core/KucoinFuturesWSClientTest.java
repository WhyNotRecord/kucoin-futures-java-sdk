/*
 * Copyright 2019 Mek Global Limited
 */

package com.kucoin.futures.core;

import com.kucoin.futures.core.enums.KLineTypeEnum;
import com.kucoin.futures.core.model.enums.PrivateChannelEnum;
import com.kucoin.futures.core.model.enums.PublicChannelEnum;
import com.kucoin.futures.core.rest.request.ChangeCrossUserLeverageRequest;
import com.kucoin.futures.core.rest.request.ChangeMarginRequest;
import com.kucoin.futures.core.rest.request.OrderCreateApiRequest;
import com.kucoin.futures.core.rest.response.MarkPriceResponse;
import com.kucoin.futures.core.rest.response.OrderCreateResponse;
import com.kucoin.futures.core.rest.response.TickerResponse;
import com.kucoin.futures.core.websocket.event.*;
import org.hamcrest.core.Is;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

/**
 * Run with -Dorg.slf4j.simpleLogger.defaultLogLevel=debug for debug logging
 *
 * @author chenshiwei
 * @since 2019/10/21
 */
public class KucoinFuturesWSClientTest extends BaseTest {


    @Test
    public void onStopOrderLifecycle() throws Exception {
        AtomicReference<StopOrderLifecycleEvent> event = new AtomicReference<>();
        CountDownLatch gotEvent = new CountDownLatch(1);

        kucoinFuturesPrivateWSClient.onStopOrderLifecycle(response -> {
            event.set(response.getData());
            kucoinFuturesPrivateWSClient.unsubscribe(PrivateChannelEnum.STOP_ORDER, SYMBOL);
            gotEvent.countDown();
        });

        MarkPriceResponse currentMarkPrice = futuresRestClient.indexAPI().getCurrentMarkPrice(SYMBOL);
        BigDecimal marketPrice = currentMarkPrice.getValue();

        placeStopOrder(marketPrice.add(new BigDecimal("0.05")), "up");
        placeStopOrder(marketPrice.add(new BigDecimal("0.5")), "up");
        placeStopOrder(marketPrice.add(BigDecimal.ONE), "up");
        placeStopOrder(marketPrice.subtract(BigDecimal.ONE), "down");
        placeStopOrder(marketPrice.subtract(new BigDecimal("0.05")), "down");
        placeStopOrder(marketPrice.subtract(new BigDecimal("0.5")), "down");

        boolean await = gotEvent.await(20, TimeUnit.SECONDS);
        futuresRestClient.orderAPI().cancelAllLimitOrders(SYMBOL);
        futuresRestClient.orderAPI().cancelAllStopOrders(SYMBOL);

        assertTrue(await);
        assertThat(event.get(), notNullValue());
    }

    @Test
    public void onAccountBalance() throws Exception {
        AtomicReference<AccountChangeEvent> event = new AtomicReference<>();
        CountDownLatch gotEvent = new CountDownLatch(1);

        kucoinFuturesPrivateWSClient.onAccountBalance(response -> {
            event.set(response.getData());
            kucoinFuturesPrivateWSClient.unsubscribe(PrivateChannelEnum.ACCOUNT);
            gotEvent.countDown();
        });

        buyAndSell();

        assertTrue(gotEvent.await(20, TimeUnit.SECONDS));
        assertThat(event.get(), notNullValue());
    }

    @Test
    public void onPositionChange() throws Exception {
        AtomicReference<PositionChangeEvent> event = new AtomicReference<>();
        CountDownLatch gotEvent = new CountDownLatch(1);

        kucoinFuturesPrivateWSClient.onPositionChange(response -> {
            event.set(response.getData());
            kucoinFuturesPrivateWSClient.unsubscribe(PrivateChannelEnum.POSITION, SYMBOL);
            gotEvent.countDown();
        }, SYMBOL);

        buyAndSell();

        assertTrue(gotEvent.await(20, TimeUnit.SECONDS));
        assertThat(event.get(), notNullValue());
    }

    @Test
    public void onPositionAllChange() throws Exception {
        AtomicReference<PositionChangeEvent> event = new AtomicReference<>();
        CountDownLatch gotEvent = new CountDownLatch(1);

        kucoinFuturesPrivateWSClient.onPositionAllChange(response -> {
            event.set(response.getData());
            kucoinFuturesPrivateWSClient.unsubscribe(PrivateChannelEnum.POSITION_ALL);
            gotEvent.countDown();
        });

        assertTrue(gotEvent.await(20000, TimeUnit.SECONDS));
        assertThat(event.get(), notNullValue());
    }

    @Test
    public void onMarginModeAndCrossLeverageChange() throws Exception {
        CountDownLatch gotEvent = new CountDownLatch(2);

        kucoinFuturesPrivateWSClient.onMarginModeChange(response -> {

            try {
                response.getData().forEach((k, v) -> {
                    assertThat(k, equalTo(SYMBOL));
                    assertThat(v, equalTo("CROSS"));
                });
                kucoinFuturesPrivateWSClient.unsubscribe(PrivateChannelEnum.MARGIN_MODE_CHANGE);
            } catch (Exception e) {
                assertThat(e, nullValue());
            } finally {
                gotEvent.countDown();
            }

        });

        String subId = kucoinFuturesPrivateWSClient.onCrossLeverageChange(response -> {
            try {
                response.getData().forEach((k, v) -> {
                    assertThat(k, equalTo(SYMBOL));
                    assertThat(v.getLeverage(), equalTo("10"));
                });
                kucoinFuturesPrivateWSClient.unsubscribe(PrivateChannelEnum.CROSS_LEVERAGE_CHANGE);
            } catch (Exception e) {
                assertThat(e, nullValue());
            } finally {
                gotEvent.countDown();
            }
        });


        Thread.sleep(1000);
        futuresRestClient.positionAPI().changeMarginMode(ChangeMarginRequest.builder().marginMode("CROSS").symbol(SYMBOL).build());
        Thread.sleep(1000);
        futuresRestClient.positionAPI().changeCrossUserLeverage(ChangeCrossUserLeverageRequest.builder().
                symbol(SYMBOL).leverage("10").build());
        Thread.sleep(1000);
        assertTrue(gotEvent.await(20000, TimeUnit.SECONDS));
    }

    @Test
    public void onSymbolOrderChange() throws Exception {
        AtomicReference<OrderChangeEvent> event = new AtomicReference<>();
        CountDownLatch gotEvent = new CountDownLatch(1);

        kucoinFuturesPrivateWSClient.onOrderChange(response -> {
            event.set(response.getData());
            kucoinFuturesPrivateWSClient.unsubscribe(PrivateChannelEnum.SYMBOL_ORDER_CHANGE, "BTCUSDT");
            gotEvent.countDown();
        }, "BTCUSDT");

        assertTrue(gotEvent.await(200, TimeUnit.SECONDS));
        assertThat(event.get(), notNullValue());
    }

    @Test
    public void onOrderChange() throws Exception {
        AtomicReference<OrderChangeEvent> event = new AtomicReference<>();
        CountDownLatch gotEvent = new CountDownLatch(1);

        kucoinFuturesPrivateWSClient.onOrderChange(response -> {
            event.set(response.getData());
            kucoinFuturesPrivateWSClient.unsubscribe(PrivateChannelEnum.ORDER_CHANGE);
            gotEvent.countDown();
        });

        assertTrue(gotEvent.await(20, TimeUnit.SECONDS));
        assertThat(event.get(), notNullValue());
    }

    @Test
    public void ping() throws Exception {
        String requestId = "1234567890";
        String ping = kucoinFuturesPrivateWSClient.ping(requestId);
        assertThat(ping, Is.is(requestId));
    }

    @Test
    public void onTicker() throws Exception {
        AtomicReference<TickerResponse> event = new AtomicReference<>();
        CountDownLatch gotEvent = new CountDownLatch(1);

        kucoinFuturesPublicWSClient.onTicker(response -> {
            event.set(response.getData());
            kucoinFuturesPublicWSClient.unsubscribe(PublicChannelEnum.TICKER, SYMBOL);
            gotEvent.countDown();
        }, SYMBOL);

        // Make some actual executions
        buyAndSell();

        assertTrue(gotEvent.await(20, TimeUnit.SECONDS));
        assertThat(event.get(), notNullValue());
    }

    @Test
    public void onTickerV2() throws Exception {
        AtomicReference<TickerV2ChangeEvent> event = new AtomicReference<>();
        CountDownLatch gotEvent = new CountDownLatch(1);

        kucoinFuturesPublicWSClient.onTickerV2(response -> {
            event.set(response.getData());
            kucoinFuturesPublicWSClient.unsubscribe(PublicChannelEnum.TICKERV2, SYMBOL);
            gotEvent.countDown();
        }, SYMBOL);

        // Make some actual executions
        buyAndSell();

        assertTrue(gotEvent.await(20, TimeUnit.SECONDS));
        assertThat(event.get(), notNullValue());
    }

    @Test
    public void onLevel2Data() throws Exception {
        AtomicReference<Level2ChangeEvent> event = new AtomicReference<>();
        AtomicReference<Level2OrderBookEvent> depth5Event = new AtomicReference<>();
        AtomicReference<Level2OrderBookEvent> depth50Event = new AtomicReference<>();
        CountDownLatch gotEvent = new CountDownLatch(3);

        kucoinFuturesPublicWSClient.onLevel2Data(response -> {
            event.set(response.getData());
            kucoinFuturesPublicWSClient.unsubscribe(PublicChannelEnum.LEVEL2, SYMBOL);
            gotEvent.countDown();
        }, SYMBOL);

        kucoinFuturesPublicWSClient.onLevel2Depth5Data(response -> {
            depth5Event.set(response.getData());
            kucoinFuturesPublicWSClient.unsubscribe(PublicChannelEnum.LEVEL2_DEPTH_5, SYMBOL);
            gotEvent.countDown();
        }, SYMBOL);

        kucoinFuturesPublicWSClient.onLevel2Depth50Data(response -> {
            depth50Event.set(response.getData());
            kucoinFuturesPublicWSClient.unsubscribe(PublicChannelEnum.LEVEL2_DEPTH_50, SYMBOL);
            gotEvent.countDown();
        }, SYMBOL);

        assertTrue(gotEvent.await(20, TimeUnit.SECONDS));
        assertThat(event.get(), notNullValue());
        assertThat(depth5Event.get(), notNullValue());
        assertThat(depth50Event.get(), notNullValue());
    }

    @Test
    public void onKline() throws Exception {
        AtomicReference<KLineEvent> klineEvent = new AtomicReference<>();
        CountDownLatch gotEvent = new CountDownLatch(1);

        String subParam = KLineTypeEnum.ONE_MIN.kLineParam(SYMBOL);

        kucoinFuturesPublicWSClient.onKline(response -> {
            klineEvent.set(response.getData());
            kucoinFuturesPublicWSClient.unsubscribe(PublicChannelEnum.K_LINE_TOPIC, subParam);
            gotEvent.countDown();
        }, subParam);

        assertTrue(gotEvent.await(20, TimeUnit.SECONDS));
        assertThat(klineEvent.get(), notNullValue());

    }

    @Test
    public void onMatchExecutionData() throws Exception {
        AtomicReference<ExecutionChangeEvent> event = new AtomicReference<>();
        CountDownLatch gotEvent = new CountDownLatch(1);

        kucoinFuturesPrivateWSClient.onExecutionData(response -> {
            event.set(response.getData());
            kucoinFuturesPrivateWSClient.unsubscribe(PublicChannelEnum.MATCH, SYMBOL);
            gotEvent.countDown();
        }, SYMBOL);

        // Make some actual executions
        buyAndSell();

        assertTrue(gotEvent.await(20, TimeUnit.SECONDS));
        assertThat(event.get(), notNullValue());
    }

    @Test
    public void onLevel3Data() throws Exception {
        AtomicReference<Level3ChangeEventV2> v2Event = new AtomicReference<>();
        CountDownLatch gotEvent = new CountDownLatch(1);

        kucoinFuturesPrivateWSClient.onLevel3DataV2(response -> {
            v2Event.set(response.getData());
            kucoinFuturesPrivateWSClient.unsubscribe(PublicChannelEnum.LEVEL3_V2, SYMBOL);
            gotEvent.countDown();
        }, SYMBOL);

        // Trigger a market change
        placeOrderAndCancelOrder();

        assertTrue(gotEvent.await(10, TimeUnit.SECONDS));
        assertThat(v2Event.get(), notNullValue());
    }

    @Test
    public void onContractMarketData() throws Exception {
        AtomicReference<ContractMarketEvent> event = new AtomicReference<>();
        CountDownLatch gotEvent = new CountDownLatch(1);

        kucoinFuturesPublicWSClient.onContractMarketData(response -> {
            event.set(response.getData());
            kucoinFuturesPublicWSClient.unsubscribe(PublicChannelEnum.CONTRACT_MARKET, SYMBOL);
            gotEvent.countDown();
        }, SYMBOL);

        assertTrue(gotEvent.await(20, TimeUnit.SECONDS));
        assertThat(event.get(), notNullValue());
    }

    @Test
    @Ignore
    public void onTransactionStatistic() throws Exception {
        AtomicReference<TransactionStatisticEvent> event = new AtomicReference<>();
        CountDownLatch gotEvent = new CountDownLatch(1);

        kucoinFuturesPublicWSClient.onTransactionStatistic(response -> {
            event.set(response.getData());
            kucoinFuturesPublicWSClient.unsubscribe(PublicChannelEnum.TRANSACTION_STATISTIC, SYMBOL);
            gotEvent.countDown();
        }, SYMBOL);

        assertTrue(gotEvent.await(20, TimeUnit.SECONDS));
        assertThat(event.get(), notNullValue());
    }

    private void placeOrderAndCancelOrder() throws InterruptedException, IOException {
        Thread.sleep(1000);
        OrderCreateApiRequest request = OrderCreateApiRequest.builder()
                .price(BigDecimal.valueOf(1000)).size(BigDecimal.ONE).side("buy").leverage("5")
                .symbol(SYMBOL).type("limit").clientOid(UUID.randomUUID().toString()).build();
        OrderCreateResponse order = futuresRestClient.orderAPI().createOrder(request);
        futuresRestClient.orderAPI().cancelOrder(order.getOrderId());
    }

    private void buyAndSell() throws InterruptedException, IOException {
        Thread.sleep(1000);
        OrderCreateApiRequest request1 = OrderCreateApiRequest.builder()
                .size(BigDecimal.ONE)
                .side("buy")
                .symbol(SYMBOL)
                .marginMode("CROSS")
                .type("market")
                .leverage("5")
                .clientOid(UUID.randomUUID().toString())
                .build();
        futuresRestClient.orderAPI().createOrder(request1);
        OrderCreateApiRequest request2 = OrderCreateApiRequest.builder()
                .size(BigDecimal.ONE)
                .side("sell")
                .marginMode("CROSS")
                .symbol(SYMBOL)
                .type("market")
                .leverage("5")
                .clientOid(UUID.randomUUID().toString())
                .build();
        futuresRestClient.orderAPI().createOrder(request2);
    }

    private OrderCreateResponse placeStopOrder(BigDecimal stopPrice, String stop) throws IOException {
        OrderCreateApiRequest pageRequest = OrderCreateApiRequest.builder()
                .stop(stop).stopPriceType("MP").stopPrice(stopPrice)
                .price(BigDecimal.valueOf(1000)).size(BigDecimal.ONE).side("buy").leverage("5")
                .symbol(SYMBOL).type("limit").clientOid(UUID.randomUUID().toString()).build();
        return futuresRestClient.orderAPI().createOrder(pageRequest);
    }
}
