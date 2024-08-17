package com.verygoodbank.tes.web.model;

import java.math.BigDecimal;

public record TradeData(String productId, BigDecimal price, String date, String currency) {

}
