package com.verygoodbank.tes;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {

    static String[][] testInputData = {
            {"20160101", "1", "EUR", "10.0"},
            {"2016010112", "1", "EUR", "10.0"},
            {"20160101", "2", "EUR", "20.1"},
            {"20160101", "3", "EUR", "30.34"},
            {"20160101", "11", "EUR", "35.34"}
    };

    static String[][] testOutputData = {
            {"20160101", "Treasury Bills Domestic", "EUR", "10.0"},
            {"2016010112", "Treasury Bills Domestic", "EUR", "10.0"},
            {"20160101", "Corporate Bonds Domestic", "EUR", "20.1"},
            {"20160101", "REPO Domestic", "EUR", "30.34"}
    };

    static Random random = new Random();

    public static void main(String[] args) {
        String inputFilePath = "src/main/resources/bigTestInput.csv";
        String outputFilePath = "src/main/resources/bigTestOutput.csv";
        try (CSVWriter writerInput = new CSVWriter(new FileWriter(inputFilePath),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);
             CSVWriter writerOutput = new CSVWriter(new FileWriter(outputFilePath),
                     CSVWriter.DEFAULT_SEPARATOR,
                     CSVWriter.NO_QUOTE_CHARACTER,
                     CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                     CSVWriter.DEFAULT_LINE_END)) {
            // Write the header to the CSV file
            String[] headerInput = {"date", "product_id", "currency", "price"};
            String[] headerOutput = {"date", "product_name", "currency", "price"};
            writerInput.writeNext(headerInput);
            writerOutput.writeNext(headerOutput);

            // Write all data rows to the CSV file
            for (int i = 0; i < 100_000; i++) {
                int productIndex = random.nextInt(testInputData.length);
                String[] inputData = testInputData[productIndex];


                String[] lineInput = {inputData[0], inputData[1], inputData[2], inputData[3]};
                writerInput.writeNext(lineInput);
                if (productIndex != 1 && productIndex != 4) {
                    String[] outputData = testOutputData[productIndex];
                    String[] lineOutput = {outputData[0], outputData[1], outputData[2], outputData[3]};

                    writerOutput.writeNext(lineOutput);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
