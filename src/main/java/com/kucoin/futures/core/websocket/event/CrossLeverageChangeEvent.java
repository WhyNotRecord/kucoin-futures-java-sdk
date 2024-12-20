/*
 * Copyright 2019 Mek Global Limited
 */

package com.kucoin.futures.core.websocket.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.HashMap;

/**
 * @author chenshiwei
 * @since 2019/10/18
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CrossLeverageChangeEvent extends HashMap<String, CrossLeverageChangeEvent.Leverage> {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Leverage {
        private String leverage;
    }
}
