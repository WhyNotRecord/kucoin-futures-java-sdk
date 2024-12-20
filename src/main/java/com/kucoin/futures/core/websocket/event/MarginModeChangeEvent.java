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
public class MarginModeChangeEvent extends HashMap<String, String> {


}
