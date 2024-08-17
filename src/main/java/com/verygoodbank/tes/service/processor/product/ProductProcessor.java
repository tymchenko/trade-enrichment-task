package com.verygoodbank.tes.service.processor.product;

import com.verygoodbank.tes.service.processor.Processor;
import com.verygoodbank.tes.service.processor.validation.ValidationProcessor;
import com.verygoodbank.tes.web.model.ProductData;
import com.verygoodbank.tes.web.model.TradeData;

import java.util.List;

import static com.verygoodbank.tes.cache.LocalCache.productNameCache;
import static java.lang.String.format;

//todo consider arrays
public class ProductProcessor implements Processor<List<ProductData>, List<TradeData>> {

    Processor<Boolean, TradeData> processor = new ValidationProcessor();

    @Override
    public List<ProductData> process(List<TradeData> trades) {
        return trades.stream()
                .peek(trade -> processor.process(trade))
                .map(trade ->
                        new ProductData(
                                trade.productId(),
                                productNameCache(trade.productId())
                                        .orElseThrow(
                                                () -> new RuntimeException(format("No product found with id: %s", trade.productId())))
                        ))
                .toList();

    }
}
