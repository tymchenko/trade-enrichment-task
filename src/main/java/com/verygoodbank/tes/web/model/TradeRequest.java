package com.verygoodbank.tes.web.model;

import java.math.BigDecimal;

public record TradeRequest(String productId, BigDecimal price, String date, String currency) {

}
