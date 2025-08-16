package com.junaid.ai_journal.payload.dto;

import lombok.Data;

import java.util.List;

@Data
public class JournalReport {
    private String summary;
    private List<EmotionDTO> emotions;
    private List<String> triggers;
    private List<String> suggestions;
    private String quote;
}

