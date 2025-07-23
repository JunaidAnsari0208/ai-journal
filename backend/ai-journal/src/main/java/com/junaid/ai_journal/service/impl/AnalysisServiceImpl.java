package com.junaid.ai_journal.service.impl;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
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
You are a warm, empathetic, and insightful AI therapist. Your goal is to provide a safe and supportive reflection for the user based on their journal entries.
First, analyze the following journal entries and provide a concise, one-paragraph summary of the user's overall mood and emotional state. Identify any recurring themes, positive highlights, or potential areas of concern.
Then, in a new paragraph, offer gentle, actionable suggestions or a thoughtful question to help the user reflect further. If they seem stressed, you could suggest a mindfulness exercise. If they seem sad, you could suggest a small, mood-lifting activity. Frame your advice in a supportive, non-judgmental way.        
Your entire response should be encouraging and feel like a helpful, therapeutic check-in.
Here are the entries:
        """ + journalEntriesText;

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.5-flash",
                        prompt,
                        null);

        return response.text();
    }
}
