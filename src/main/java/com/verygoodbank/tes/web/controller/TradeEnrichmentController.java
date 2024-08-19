package com.verygoodbank.tes.web.controller;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.verygoodbank.tes.cache.ProductNameCache;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import static com.verygoodbank.tes.service.validation.DateValidator.notValidDate;

//curl --data @src/test/resources/trade.csv --header 'Content-Type: text/csv' http://localhost:8080/api/v1/enrich
//curl --data @trade.csv --header 'Content-Type: text/csv' http://localhost:8080/api/v1/enrich
@RestController
@RequestMapping("api/v1")
public class TradeEnrichmentController {

    private static final int DATE_INDEX = 0;
    private static final int PRODUCT_ID_INDEX = 1;
    private static final int CURRENCY_INDEX = 2;
    private static final int PRICE_INDEX = 3;

    private final ProductNameCache productNameCache;


    public TradeEnrichmentController(ProductNameCache productNameCache) {
        this.productNameCache = productNameCache;
    }

    @PostMapping(value = "/enrich", consumes = "text/csv", produces = "text/csv")
    public StreamingResponseBody uploadCsvFile(InputStream inputStream) {

        return outputStream -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                 CSVReader csvReader = new CSVReader(reader);
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                 CSVWriter csvWriter = new CSVWriter(writer)) {

                csvReader.skip(1);

                String[] header = {"date" , "product_name", "currency", "price"};
                csvWriter.writeNext(header);

                Iterator<String[]> iterator = csvReader.iterator();
                while (iterator.hasNext()) {
                    String[] values = iterator.next();
                    if (values.length < 4 || notValidDate(values[DATE_INDEX])) {
                        continue;
                    }

                    String productName = productNameCache.getProductDataById(values[PRODUCT_ID_INDEX]);
                    if(productName == null) {
                        continue;
                    }

                    String[] line = {values[0], productName, values[CURRENCY_INDEX], values[PRICE_INDEX]};
                    csvWriter.writeNext(line);
                }
            }
            outputStream.flush();
        };
    }
}


