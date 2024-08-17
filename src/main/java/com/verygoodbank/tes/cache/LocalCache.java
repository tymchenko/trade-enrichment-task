package com.verygoodbank.tes.cache;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public interface LocalCache {

    static Optional getProductName(String productId) {
        return Optional.ofNullable(cache().get(productId));
    }

    private static Map<Integer, String> cache() {
        Map<Integer, String> productNameCache = new ConcurrentHashMap();

        productNameCache.put(1, "Treasury Bills Domestic");
        productNameCache.put(2, "Corporate Bonds Domestic");
        productNameCache.put(3, "REPO Domestic");
        productNameCache.put(4, "Interest rate swaps International");
        productNameCache.put(5, "OTC Index Option");
        productNameCache.put(6, "Currency Options");
        productNameCache.put(7, "Reverse Repos International");
        productNameCache.put(8, "REPO International");
        productNameCache.put(9, "766A_CORP BD");
        productNameCache.put(10, "766B_CORP BD");
        return productNameCache;
    }
}
