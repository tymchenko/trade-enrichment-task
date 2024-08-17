package com.verygoodbank.tes.service.processor.product;

import com.verygoodbank.tes.service.processor.Processor;
import com.verygoodbank.tes.web.model.ProductData;
import com.verygoodbank.tes.web.model.TradeData;

import java.util.List;

import static com.verygoodbank.tes.cache.LocalCache.productNameCache;

//todo consider arrays
public class ProductProcessor implements Processor<List<ProductData>, List<TradeData>> {


    @Override
    public List<ProductData> process(List<TradeData> trades) {
        return trades.stream()
                .map(trade ->
                        new ProductData(
                                trade.date(),
                                productNameCache(trade.productId())
                                        .orElseThrow(() -> new RuntimeException("")),
                                trade.currency(),
                                trade.price()
                        ))
                .toList();

    }
}
