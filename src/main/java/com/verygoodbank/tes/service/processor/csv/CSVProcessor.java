package com.verygoodbank.tes.service.processor.csv;

import com.verygoodbank.tes.service.processor.Processor;
import com.verygoodbank.tes.service.processor.product.ProductProcessor;
import com.verygoodbank.tes.web.model.ProductData;
import com.verygoodbank.tes.web.model.TradeData;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

//todo refactor this class
public class CSVProcessor implements Processor<File, byte[]> {

    private Processor<List<ProductData>, List<TradeData>> processor = new ProductProcessor();

    @Override
    public File process(byte[] data) {
        try {
            // Convert byte array to String
            String csvContent = new String(data, StandardCharsets.UTF_8);
            System.out.println("Raw CSV Content:");
            System.out.println(csvContent);

            // Print raw bytes to diagnose hidden characters or issues
            System.out.println("Raw bytes:");
            for (byte b : data) {
                System.out.printf("%02X ", b);
            }
            System.out.println();
            // Split the CSV content into lines
            List<String> lines = List.of(csvContent.split(";;;"));
            System.out.println("CSV Lines Count: " + lines.size());
            lines.forEach(line -> System.out.println("CSV Line: " + line));

            // Optional: Process each line further
            // Example: Convert lines to TradeData objects
            List<TradeData> tradeData = lines.stream()
                    .skip(1) // Skip header line
                    .map(this::mapToTradeRequest)
                    .collect(Collectors.toList());

            List<ProductData> products = processor.process(tradeData);


            System.out.println("Processed TradeRequests:");
            tradeData.forEach(tradeRequest -> System.out.println(tradeRequest));

            //todo refactor here
//            if (tradeData.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No data found in the file");
//            }

            //todo update file
            return writeProductsData(products);
        } catch (Exception e) {
            e.printStackTrace();
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to process file");
        }
        //todo not good here
        return null;
    }

    //todo add file writing
    private File writeProductsData(List<ProductData> products) {
        return null;
    }

    private TradeData mapToTradeRequest(String line) {
        String[] fields = line.split(";"); // Split the CSV line by comma

        if (fields.length != 4) {
            throw new IllegalArgumentException("Invalid CSV line: " + line);
        }

        String date = fields[0].trim();
        String productId = fields[1].trim();
        String currency = fields[2].trim();
        BigDecimal price = new BigDecimal(fields[3].trim());


        return new TradeData(productId, price, date, currency);
    }
}
