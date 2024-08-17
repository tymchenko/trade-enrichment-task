package com.verygoodbank.tes.web.controller;

import com.verygoodbank.tes.service.processor.csv.CSVProcessor;
import com.verygoodbank.tes.service.processor.Processor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

//curl --data @src/test/resources/trade.csv --header 'Content-Type: text/csv' http://localhost:8080/api/v1/enrich
//curl --data @trade.csv --header 'Content-Type: text/csv' http://localhost:8080/api/v1/enrich
@RestController
@RequestMapping("api/v1")
public class TradeEnrichmentController {

    private Processor<File, byte[]> processor = new CSVProcessor();

    @PostMapping(value = "/enrich",
            consumes = "text/csv",
            produces = "text/csv")
    public ResponseEntity uploadCsvFile(@RequestBody byte[] trades) {
        if (isEmpty(trades)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        File products = processor.process(trades);

        if (validationFails(products)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process file");
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=enrichedTrades.csv")
                .contentLength(products.length())
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new FileSystemResource(products));
    }

    private boolean validationFails(File file) {
        return isNull(file) || !file.exists() || file.length() == 0;
    }
}


