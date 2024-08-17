package com.verygoodbank.tes.web.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

//curl --data @src/test/resources/trade.csv --header 'Content-Type: text/csv' http://localhost:8080/api/v1/enrich
//curl --data @trade.csv --header 'Content-Type: text/csv' http://localhost:8080/api/v1/enrich
@RestController
@RequestMapping("api/v1")
public class TradeEnrichmentController {

    @PostMapping(
            value = "/enrich",
            consumes = "text/csv"
    )
    public ResponseEntity<String> uploadCsvFile(@RequestBody byte[] fileData) {
        if (fileData.length == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        try {
            // Process the CSV file
            List<String[]> csvData;
            try (var br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(fileData), StandardCharsets.UTF_8))) {
                csvData = br.lines()
                        .map(line -> line.split(","))
                        .toList();
            }

            // Example: Log or process the data
            csvData.forEach(row -> {
                // Process each row, here we're just printing it out
                System.out.println("Processing row: " + String.join(", ", row));
            });

            // Return a success response
            return ResponseEntity.ok("File uploaded and processed successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process file");
        }
    }

}


