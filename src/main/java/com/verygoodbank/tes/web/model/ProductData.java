package com.verygoodbank.tes.web.model;

import java.math.BigDecimal;

public record ProductData(String date, String productName, String currency, BigDecimal price) {
}
