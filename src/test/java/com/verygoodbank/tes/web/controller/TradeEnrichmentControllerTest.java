package com.verygoodbank.tes.web.controller;

import com.verygoodbank.tes.cache.ProductNameCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StreamUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This test verifies that http controller can deal with .csv file with 100000 trades
 * I mocked Redis cache
 * I used input and output .csv files
 */
@WebMvcTest(TradeEnrichmentController.class)
public class TradeEnrichmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductNameCache productNameCache;

    @BeforeEach
    void setUp() {
        // Mock the product name cache to return specific product names
        when(productNameCache.getProductDataById("1")).thenReturn("Treasury Bills Domestic");
        when(productNameCache.getProductDataById("2")).thenReturn("Corporate Bonds Domestic");
        when(productNameCache.getProductDataById("3")).thenReturn("REPO Domestic");
        when(productNameCache.getProductDataById("4")).thenReturn("Interest rate swaps International");
        when(productNameCache.getProductDataById("5")).thenReturn("OTC Index Option");
        when(productNameCache.getProductDataById("6")).thenReturn("Currency Option");
        when(productNameCache.getProductDataById("7")).thenReturn("Reverse Repos International");
        when(productNameCache.getProductDataById("8")).thenReturn("REPO International");
        when(productNameCache.getProductDataById("9")).thenReturn("766A_CORP BD");
        when(productNameCache.getProductDataById("10")).thenReturn("766B_CORP BD");
    }

    @Test
    void testUploadCsvFile() throws Exception {
        // Load input CSV from the resources directory
        InputStream inputCsvStream = getClass().getClassLoader().getResourceAsStream("bigTestInput.csv");
        String inputCsv = StreamUtils.copyToString(inputCsvStream, StandardCharsets.UTF_8);

        // Load expected CSV result from the resources directory
        InputStream expectedCsvStream = getClass().getClassLoader().getResourceAsStream("bigTestOutput.csv");
        String expectedCsv = StreamUtils.copyToString(expectedCsvStream, StandardCharsets.UTF_8);

        String actualResult = mockMvc.perform(post("/api/v1/enrich")
                        .contentType("text/csv")
                        .content(inputCsv.getBytes(StandardCharsets.UTF_8)))
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andDo(MvcResult::getAsyncResult)
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(actualResult, expectedCsv);
    }
}
