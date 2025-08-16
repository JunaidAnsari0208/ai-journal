package com.junaid.ai_journal.service.impl;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.GenerationConfig;
import com.junaid.ai_journal.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {

    private final Client client;


    @Override
    public String getAnalysis(String journalEntriesText) {
        String prompt = """
You are an AI mental health assistant.
Analyze the following journal entry and return ONLY a valid JSON object.
Do not include any extra text, explanation, or commentary outside the JSON.
Do not use Markdown formatting, code blocks, or backticks. Output plain JSON only. THIS IS IMPORTANT


The JSON must strictly follow this structure:
{
  "summary": "3-5 sentences summarizing the day",
  "emotions": [
    {"emotion": "string", "intensity": number 0-10},
    {"emotion": "string", "intensity": number 0-10},
    {"emotion": "string", "intensity": number 0-10}
  ],
  "triggers": ["list", "possible", "triggers"],
  "suggestions": ["numbered wellness suggestions"],
  "quote": "one motivational line"
}

Journal entry:
""" + journalEntriesText;
        GenerateContentConfig generationConfig = GenerateContentConfig.builder()
                .responseMimeType("application/json")
                .build();

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.5-flash",
                        prompt,
                        generationConfig);

        return response.text();
    }
}
