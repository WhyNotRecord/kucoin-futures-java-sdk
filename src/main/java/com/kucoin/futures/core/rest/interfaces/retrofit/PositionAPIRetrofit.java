/*
 * Copyright 2019 Mek Global Limited
 */
package com.kucoin.futures.core.rest.interfaces.retrofit;

import com.kucoin.futures.core.rest.request.*;
import com.kucoin.futures.core.rest.response.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author chenshiwei
 * @since 2019/10/14
 */
public interface PositionAPIRetrofit {

    @GET("api/v1/position")
    Call<KucoinFuturesResponse<PositionResponse>> getPosition(@Query("symbol") String symbol);

    @GET("api/v1/positions")
    Call<KucoinFuturesResponse<List<PositionResponse>>> getPositions();

    @GET("api/v2/getMaxOpenSize")
    Call<KucoinFuturesResponse<MaxOpenSizeResponse>> getMaxOpenSize(@Query("symbol") String symbol,
                                                                    @Query("price") BigDecimal price,
                                                                    @Query("leverage") BigDecimal leverage);


    @GET("api/v1/history-positions")
    Call<KucoinFuturesResponse<Pagination<HistoryPositionResponse>>> getHistoryPositions(@Query("symbol") String symbol,
                                                                                         @Query("from") Long from,
                                                                                         @Query("to") Long to,
                                                                                         @Query("limit") Integer limit,
                                                                                         @Query("pageId") Integer pageId);

    @GET("api/v2/position/getMarginMode")
    Call<KucoinFuturesResponse<MarginModeResponse>> getMarginMode(@Query("symbol") String symbol);

    @POST("api/v2/position/changeMarginMode")
    Call<KucoinFuturesResponse<MarginModeResponse>> changeMarginMode(@Body ChangeMarginRequest request);

    @GET("api/v2/getCrossUserLeverage")
    Call<KucoinFuturesResponse<GetCrossUserLeverageResponse>> getCrossUserLeverage(@Query("symbol") String symbol);

    @POST("api/v2/changeCrossUserLeverage")
    Call<KucoinFuturesResponse<Boolean>> changeCrossUserLeverage(@Body ChangeCrossUserLeverageRequest request);


    @POST("api/v1/position/margin/auto-deposit-status")
    Call<KucoinFuturesResponse<Object>> setAutoDepositMargin(@Body UpdateAutoDepositMarginRequest request);

    @GET("api/v1/margin/maxWithdrawMargin")
    Call<KucoinFuturesResponse<BigDecimal>> getMaxWithdrawMargin(@Query("symbol") String symbol);

    @POST("api/v1/margin/withdrawMargin")
    Call<KucoinFuturesResponse<BigDecimal>> withdrawMargin(@Body WithdrawMarginRequest request);

    @POST("api/v1/position/margin/deposit-margin")
    Call<KucoinFuturesResponse<Object>> addMarginManually(@Body AddMarginManuallyRequest request);

}
