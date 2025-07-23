package com.junaid.ai_journal.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is an integration test to verify that we can successfully connect
 * to the Google Gemini API via our AnalysisService.
 */
@SpringBootTest // This annotation tells Spring Boot to load the full application context
class AnalysisServiceTest {

    // Spring will automatically inject the real AnalysisService bean
    @Autowired
    private AnalysisService analysisService;

    @Test
    void testGetAnalysis() {
        // 1. Define a simple text to analyze
        String testContent = "This is a test. I am feeling happy and productive today.";

        System.out.println("Sending test content to Gemini...");

        // 2. Call the service
        String result = analysisService.getAnalysis(testContent);

        // 3. Print the result from the AI
        System.out.println("Received response from Gemini:");
        System.out.println(result);

        // 4. Assert that the result is not null or empty
        assertNotNull(result, "The result from the AI should not be null.");
        assertFalse(result.isBlank(), "The result from the AI should not be blank.");
    }
}