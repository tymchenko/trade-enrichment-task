package com.verygoodbank.tes.web.controller;

import com.verygoodbank.tes.web.model.TradeRequest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

//curl --data @src/test/resources/trade.csv --header 'Content-Type: text/csv' http://localhost:8080/api/v1/enrich
//curl --data @trade.csv --header 'Content-Type: text/csv' http://localhost:8080/api/v1/enrich
@RestController
@RequestMapping("api/v1")
public class TradeEnrichmentController {
    @PostMapping(value = "/enrich",
            consumes = "text/csv",
            produces = "text/csv")
    public ResponseEntity uploadCsvFile(@RequestBody byte[] fileData) {
        if (fileData.length == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        try {
            // Convert byte array to String
            String csvContent = new String(fileData, StandardCharsets.UTF_8);
            System.out.println("Raw CSV Content:");
            System.out.println(csvContent);


            // Print raw bytes to diagnose hidden characters or issues
            System.out.println("Raw bytes:");
            for (byte b : fileData) {
                System.out.printf("%02X ", b);
            }
            System.out.println();
            // Split the CSV content into lines
            List<String> lines = List.of(csvContent.split(";;;"));
            System.out.println("CSV Lines Count: " + lines.size());
            lines.forEach(line -> System.out.println("CSV Line: " + line));

            // Optional: Process each line further
            // Example: Convert lines to TradeRequest objects
            List<TradeRequest> tradeRequests = lines.stream()
                    .skip(1) // Skip header line
                    .map(this::mapToTradeRequest)
                    .collect(Collectors.toList());

            System.out.println("Processed TradeRequests:");
            tradeRequests.forEach(tradeRequest -> System.out.println(tradeRequest));

            if (tradeRequests.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No data found in the file");
            }

            //todo update file
            File enrichedFile = new File("src/main/resources/product.csv");
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=enrichedTrades.csv")
                    .contentLength(enrichedFile.length())
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(new FileSystemResource(enrichedFile));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process file");
        }
    }


    private TradeRequest mapToTradeRequest(String line) {
        String[] fields = line.split(";"); // Split the CSV line by comma

        if (fields.length != 4) {
            throw new IllegalArgumentException("Invalid CSV line: " + line);
        }

        String date = fields[0].trim();
        String productId = fields[1].trim();
        String currency = fields[2].trim();
        BigDecimal price = new BigDecimal(fields[3].trim());


        return new TradeRequest(productId, price, date, currency);
    }
}


