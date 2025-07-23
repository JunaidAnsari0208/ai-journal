package com.junaid.ai_journal.service;

import com.junaid.ai_journal.model.JournalEntry;
import com.junaid.ai_journal.payload.dto.JournalEntryDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface JournalService {
    JournalEntry createJournalEntry(JournalEntryDTO journalEntryDTO);
    JournalEntry getJournalById(Long journalId);
    List<JournalEntry> getAllJournalEntries(Long userId);
    void deleteJournalEntry(Long journalId) throws Exception;
    List<JournalEntry> getWeeklyEntries(Long userId);
    void generateReport(Long userId);
}
