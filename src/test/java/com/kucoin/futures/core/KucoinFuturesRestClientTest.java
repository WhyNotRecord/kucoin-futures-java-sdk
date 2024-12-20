/*
 * Copyright 2019 Mek Global Limited
 */
package com.kucoin.futures.core;

import com.kucoin.futures.core.exception.KucoinFuturesApiException;
import com.kucoin.futures.core.rest.request.*;
import com.kucoin.futures.core.rest.response.*;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.junit.MatcherAssert.assertThat;

/**
 * Created by chenshiwei on 2019/1/21.
 */
public class KucoinFuturesRestClientTest extends BaseTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();


    @Test
    public void accountAPI() throws Exception {
        AccountOverviewResponse accountOverviewResponse = futuresRestClient.accountAPI().accountOverview("USDT");
        assertThat(accountOverviewResponse, notNullValue());

        AccountOverviewAllResponse accountOverviewAllResponse = futuresRestClient.accountAPI().accountOverviewAll("USDT");
        assertThat(accountOverviewAllResponse, notNullValue());

        HasMoreResponse<TransactionHistory> transactionHistoryHasMoreResponse = futuresRestClient.accountAPI()
                .transactionHistory(null, null, null);
        assertThat(transactionHistoryHasMoreResponse, notNullValue());
        assertThat(transactionHistoryHasMoreResponse.isHasMore(), Is.is(false));

        SubApiKeyResponse subApiKeyCreateResponse = futuresRestClient.accountAPI().createSubApiKey("TestSubUser001", "123456789", "remark test", null, null, null);
        MatcherAssert.assertThat(subApiKeyCreateResponse, notNullValue());

        List<SubApiKeyResponse> subApiKeyResponses = futuresRestClient.accountAPI().getSubApiKey("TestSubUser001", null);
        MatcherAssert.assertThat(subApiKeyResponses, notNullValue());

        SubApiKeyResponse subApiKeyUpdateResponse = futuresRestClient.accountAPI().updateSubApiKey("TestSubUser001", "apiKey", "123456789", null, "127.0.0.1", "360");
        MatcherAssert.assertThat(subApiKeyUpdateResponse, notNullValue());

        SubApiKeyResponse subApiKeyDeleteResponse = futuresRestClient.accountAPI().deleteSubApiKey("TestSubUser001", "apiKey", "123456789");
        MatcherAssert.assertThat(subApiKeyDeleteResponse, notNullValue());
    }

    @Test
    @Ignore
    public void depositAPI() throws Exception {

        exception.expect(KucoinFuturesApiException.class);
        exception.expectMessage("Sandbox environment cannot get deposit address");
        futuresRestClient.depositAPI().getDepositAddress("XBT");

        exception.expect(KucoinFuturesApiException.class);
        exception.expectMessage("Sandbox environment cannot get deposit address");
        futuresRestClient.depositAPI().getDepositList("SUCCESS", null, pageRequest);
    }

    @Test
    @Ignore
    public void withdrawalAPI() throws Exception {
        Pagination<WithdrawResponse> withdrawList = futuresRestClient.withdrawalAPI()
                .getWithdrawList("FAILURE", null, null);
        assertThat(withdrawList, notNullValue());

        WithdrawQuotaResponse kcs = futuresRestClient.withdrawalAPI().getWithdrawQuotas("XBT");
        assertThat(kcs, notNullValue());

        exception.expect(KucoinFuturesApiException.class);
        exception.expectMessage("Sandbox environment cannot be withdrawn");
        WithdrawApplyRequest withdrawApplyRequest = WithdrawApplyRequest.builder().address("123467")
                .amount(BigDecimal.valueOf(0.00000001)).currency("XBT").build();
        futuresRestClient.withdrawalAPI().withdrawFunds(withdrawApplyRequest);

        futuresRestClient.withdrawalAPI().cancelWithdraw("1234567");
    }

    @Test
    public void transferAPI() throws Exception {
        DuringPageRequest pageRequest = DuringPageRequest.builder()
                .starAt(LocalDateTime.now().minusHours(23).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .endAt(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .currentPage(1)
                .pageSize(10).build();

        Pagination<TransferHistory> records = futuresRestClient.transferAPI().getTransferOutRecords("SUCCESS", null, pageRequest);
        assertThat(records.getItems().size(), is(0));

        TransferResponse transferResponse = futuresRestClient.transferAPI().toKucoinMainAccount("123456", BigDecimal.valueOf(0.0000001), "USDT");
        assertThat(transferResponse.getApplyId(), notNullValue());

        futuresRestClient.transferAPI().cancelTransferOutRequest(transferResponse.getApplyId());

        TransferResponse transferOutRes = futuresRestClient.transferAPI().transferOut("TRADE", BigDecimal.valueOf(0.0000001), "USDT");
        assertThat(transferOutRes, notNullValue());

        futuresRestClient.transferAPI().transferIn("TRADE", BigDecimal.valueOf(0.0000001), "USDT");
    }

    @Test
    public void riskLimitAPI() throws Exception {
        List<RiskLimitResponse> riskLimits = futuresRestClient.riskLimitAPI().getRiskLimit(SYMBOL);
        assertThat(riskLimits, notNullValue());

        Boolean result = futuresRestClient.riskLimitAPI().changeRiskLimit(SYMBOL, 2);
        assertThat(result, is(true));
    }

    @Test
    public void orderAPI() throws Exception {
        OrderCreateResponse order = placeCannotDealLimitOrder();
        assertThat(order, notNullValue());

        OrderResponse orderDetail = futuresRestClient.orderAPI().getOrderDetail(order.getOrderId());
        assertThat(orderDetail, notNullValue());

        OrderResponse orderDetail2 = futuresRestClient.orderAPI().getOrderDetailByClientOid(order.getClientOid());
        assertThat(orderDetail2, notNullValue());

        OrderCancelResponse orderCancelResponse = futuresRestClient.orderAPI().cancelOrder(order.getOrderId());
        assertThat(orderCancelResponse.getCancelledOrderIds().size(), is(1));

        order = placeCannotDealLimitOrder();
        assertThat(order, notNullValue());

        orderDetail = futuresRestClient.orderAPI().getOrderDetail(order.getOrderId());
        assertThat(orderDetail, notNullValue());

        OrderCancelByClientOidResponse orderCancelByClientOidResponse = futuresRestClient.orderAPI().cancelOrderByClientOid(orderDetail.getClientOid(), SYMBOL);
        assertThat(orderCancelByClientOidResponse.getClientOid(), notNullValue());

        placeCannotDealLimitOrder();
        OrderCancelResponse cancelAllLimitOrders = futuresRestClient.orderAPI().cancelAllLimitOrders(SYMBOL);
        assertThat(cancelAllLimitOrders.getCancelledOrderIds().size(), greaterThan(0));

        placeCannotTriggerStopOrder();
        Pagination<OrderResponse> stopOrderList = futuresRestClient.orderAPI().getUntriggeredStopOrderList(SYMBOL,
                "buy", "limit", pageRequest);
        assertThat(stopOrderList.getItems().size(), greaterThan(0));

        OrderCancelResponse cancelAllStopOrders = futuresRestClient.orderAPI().cancelAllStopOrders(SYMBOL);
        assertThat(cancelAllStopOrders.getCancelledOrderIds().size(), greaterThan(0));

        Pagination<OrderResponse> orderList = futuresRestClient.orderAPI().getOrderList(SYMBOL, "buy",
                "limit", "done", null);
        assertThat(orderList.getItems().size(), greaterThan(0));

        List<OrderResponse> recentDoneOrders = futuresRestClient.orderAPI().getRecentDoneOrders();
        assertThat(recentDoneOrders.size(), greaterThan(0));

        TradeFeeResponse tradeFee = futuresRestClient.orderAPI().getTradeFee(SYMBOL);
        assertThat(tradeFee, notNullValue());

    }

    @Test
    public void fillAPI() throws Exception {
        Pagination<FillResponse> fills = futuresRestClient.fillAPI().getFills(null, null, null,
                null, null);
        assertThat(fills, notNullValue());

        List<FillResponse> fillResponses = futuresRestClient.fillAPI().recentFills();
        assertThat(fillResponses, notNullValue());

        ActiveOrderResponse xbtusdtm = futuresRestClient.fillAPI().calActiveOrderValue(SYMBOL);
        assertThat(xbtusdtm, notNullValue());
    }

    @Test
    public void positionAPI() throws Exception {
        PositionResponse position = futuresRestClient.positionAPI().getPosition(SYMBOL);
        assertThat(position, notNullValue());
        MaxOpenSizeResponse maxOpenSize = futuresRestClient.positionAPI().getMaxOpenSize(MaxOpenSizeRequest.builder().
                symbol("PEPEUSDTM").price(BigDecimal.valueOf(0.0000000001)).leverage(BigDecimal.valueOf(10)).build());
        assertThat(maxOpenSize, notNullValue());

        List<PositionResponse> positions = futuresRestClient.positionAPI().getPositions();
        assertThat(positions, notNullValue());

        if (!positions.isEmpty()) {
            futuresRestClient.positionAPI().setAutoDepositMargin(SYMBOL, true);
            futuresRestClient.positionAPI().addMarginManually(SYMBOL, BigDecimal.valueOf(0.000001), "123456");
        }

        BigDecimal maxWithdrawMargin = futuresRestClient.positionAPI().getMaxWithdrawMargin(SYMBOL);
        assertThat(maxWithdrawMargin, notNullValue());

        BigDecimal withdrewMargin = futuresRestClient.positionAPI().withdrawMargin(WithdrawMarginRequest.builder().symbol(SYMBOL).withdrawAmount(new BigDecimal("0.0000001")).build());
        assertThat(withdrewMargin, notNullValue());

        Pagination<HistoryPositionResponse> historyPositions = futuresRestClient.positionAPI().getHistoryPositions(HistoryPositionsRequest.builder().symbol("BOMEUSDTM").from(startAt).to(endAt).limit(10).pageId(1).build());
        assertThat(historyPositions, notNullValue());

    }

    @Test
    public void positionAPI1() throws Exception {
        MarginModeResponse marginMode = futuresRestClient.positionAPI().getMarginMode(SYMBOL);
        assertThat(marginMode, notNullValue());

        marginMode = futuresRestClient.positionAPI().changeMarginMode(ChangeMarginRequest.builder().marginMode("CROSS").symbol(SYMBOL).build());
        assertThat(marginMode, notNullValue());


        boolean success = futuresRestClient.positionAPI().changeCrossUserLeverage(ChangeCrossUserLeverageRequest.builder().symbol(SYMBOL).leverage("10").build());
        assertThat(success, is(true));

        GetCrossUserLeverageResponse response = futuresRestClient.positionAPI().getCrossUserLeverage(SYMBOL);
        assertThat(response, notNullValue());


    }

    @Test
    public void fundingFeeAPI() throws Exception {
        HasMoreResponse<FundingHistoryResponse> fundingHistory = futuresRestClient.fundingFeeAPI()
                .getFundingHistory(SYMBOL, null, null, hasMoreRequest);
        assertThat(fundingHistory, notNullValue());

        List<PublicFundingReteResponse> publicFundingReteResponses = futuresRestClient.fundingFeeAPI().getPublicFundingRates("IDUSDTM", 1700310700000L, 1702310700000L);
        assertThat(publicFundingReteResponses, notNullValue());

    }

    @Test
    public void symbolAPI() throws Exception {
        ContractResponse contract = futuresRestClient.symbolAPI().getContract(SYMBOL);
        assertThat(contract, notNullValue());

        List<ContractResponse> openContractList = futuresRestClient.symbolAPI().getOpenContractList();
        assertThat(openContractList.size(), greaterThan(0));
    }

    @Test
    public void tickerAPI() throws Exception {
        TickerResponse ticker = futuresRestClient.tickerAPI().getTicker(SYMBOL);
        assertThat(ticker, notNullValue());

        List<TickerResponse> tickers = futuresRestClient.tickerAPI().getAllTickers();
        tickers.forEach(t -> {
            assertThat(t, notNullValue());
        });
        assertThat(tickers.size(), greaterThan(0));
    }

    @Test
    public void orderBookAPI() throws Exception {
        OrderBookResponse fullOrderBookAggregated = futuresRestClient.orderBookAPI().getFullLevel2OrderBook(SYMBOL);
        assertThat(fullOrderBookAggregated, notNullValue());

        OrderBookResponse orderBook20 = futuresRestClient.orderBookAPI().getDepth20Level2OrderBook(SYMBOL);
        assertThat(orderBook20, notNullValue());

        OrderBookResponse orderBook100 = futuresRestClient.orderBookAPI().getDepth100Level2OrderBook(SYMBOL);
        assertThat(orderBook100, notNullValue());

        OrderBookResponse fullOrderBookAtomic = futuresRestClient.orderBookAPI().getFullLevel3OrderBook(SYMBOL);
        assertThat(fullOrderBookAtomic, notNullValue());
    }

    @Test
    public void historyAPI() throws Exception {
        List<TransactionHistoryResponse> transactionHistories = futuresRestClient.historyAPI().getTransactionHistories(SYMBOL);
        assertThat(transactionHistories.size(), greaterThan(0));
    }

    @Test
    public void indexAPI() throws Exception {
        HasMoreResponse<InterestRateResponse> interestRateList = futuresRestClient.indexAPI()
                .getInterestRateList(SYMBOL, null, null, hasMoreRequest);
        assertThat(interestRateList, notNullValue());

        FundingRateResponse currentFundingRate = futuresRestClient.indexAPI().getCurrentFundingRate(SYMBOL);
        assertThat(currentFundingRate, notNullValue());

        TradeStatisticsResponse tradeStatistics = futuresRestClient.indexAPI().getTradeStatistics();
        assertThat(tradeStatistics, notNullValue());

    }

    @Test
    @Ignore
    public void serviceStatusAPI() throws Exception {
        ServiceStatusResponse serviceStatus = futuresRestClient.serviceStatusAPI().getServiceStatus();
        assertThat(serviceStatus, notNullValue());
    }

    @Test
    public void kChartAPI() throws Exception {
        List<List<String>> kChart = futuresRestClient.kChartAPI().getKChart(SYMBOL, 60, null, null);
        assertThat(kChart, notNullValue());
    }

    @Test
    public void timeAPI() throws Exception {
        Long serverTimeStamp = futuresRestClient.timeAPI().getServerTimeStamp();
        assertThat(System.currentTimeMillis() - serverTimeStamp, lessThanOrEqualTo(5000L));
    }

    @Test
    public void placeOrderTest() throws IOException {
        OrderCreateApiRequest pageRequest = OrderCreateApiRequest.builder()
                .price(BigDecimal.valueOf(5)).size(BigDecimal.ONE).side("buy").leverage("5")
                .symbol(SYMBOL).type("limit").clientOid(UUID.randomUUID().toString()).build();
        OrderCreateResponse orderTest = futuresRestClient.orderAPI().createOrderTest(pageRequest);
        assertThat(orderTest, notNullValue());
    }

    @Test
    public void stOrderTest() throws IOException {
        StOrderCreateRequest request = StOrderCreateRequest.builder().
                clientOid(UUID.randomUUID().toString()).
                side("buy").symbol("XBTUSDTM").
                leverage(BigDecimal.valueOf(2)).
                type("limit").
                price(BigDecimal.valueOf(800)).
                size(1).stopPriceType("TP").marginMode("CROSS").
                triggerStopUpPrice(BigDecimal.valueOf(9000)).
                triggerStopDownPrice(BigDecimal.valueOf(100)).
                timeInForce("GTC").
                build();
        StOrderCreateResponse orderTest = futuresRestClient.orderAPI().createStOrders(request);
        assertThat(orderTest, notNullValue());
    }

    @Test
    public void placeOrderMultiTest() throws IOException {
        OrderCreateApiRequest pageRequest = OrderCreateApiRequest.builder()
                .price(BigDecimal.valueOf(5)).size(BigDecimal.ONE).side("buy").leverage("5")
                .symbol(SYMBOL).type("limit").clientOid(UUID.randomUUID().toString()).build();
        List<OrderCreateMultiResponse> orderCreateMultiResponseList = futuresRestClient.orderAPI().createOrderMulti(Arrays.asList(pageRequest));
        assertThat(orderCreateMultiResponseList, notNullValue());
    }

    private OrderCreateResponse placeCannotDealLimitOrder() throws IOException {
        OrderCreateApiRequest pageRequest = OrderCreateApiRequest.builder()
                .price(BigDecimal.valueOf(5)).size(BigDecimal.ONE).side("buy").leverage("5")
                .marginMode("CROSS")
                .symbol(SYMBOL).type("limit").clientOid(UUID.randomUUID().toString()).build();
        return futuresRestClient.orderAPI().createOrder(pageRequest);
    }

    private OrderCreateResponse placeCannotTriggerStopOrder() throws IOException {
        OrderCreateApiRequest pageRequest = OrderCreateApiRequest.builder()
                .stop("down").stopPriceType("MP").stopPrice(BigDecimal.valueOf(1000))
                .price(BigDecimal.valueOf(1000)).size(BigDecimal.ONE).side("buy").leverage("5")
                .symbol(SYMBOL).type("limit").clientOid(UUID.randomUUID().toString()).build();
        return futuresRestClient.orderAPI().createOrder(pageRequest);
    }

}