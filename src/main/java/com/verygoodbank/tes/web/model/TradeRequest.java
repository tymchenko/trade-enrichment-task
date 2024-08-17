package com.verygoodbank.tes.web.model;

import java.math.BigInteger;

public record TradeRequest(String productId, BigInteger price, String date, String currency) {

}
