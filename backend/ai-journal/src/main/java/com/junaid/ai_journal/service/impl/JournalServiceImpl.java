package com.junaid.ai_journal.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junaid.ai_journal.client.UserServiceClient;
import com.junaid.ai_journal.model.JournalEntry;
import com.junaid.ai_journal.payload.dto.JournalEntryDTO;
import com.junaid.ai_journal.payload.dto.JournalReport;
import com.junaid.ai_journal.payload.dto.UserDTO;
import com.junaid.ai_journal.repository.JournalEntryRepository;
import com.junaid.ai_journal.service.AnalysisService;
import com.junaid.ai_journal.service.EmailService;
import com.junaid.ai_journal.service.JournalService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JournalServiceImpl implements JournalService {

    private final JournalEntryRepository journalEntryRepository;
    private final AnalysisService analysisService;
    private final EmailService emailService;
    private final UserServiceClient userServiceClient;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(JournalServiceImpl.class);


    @Override
    public JournalEntry createJournalEntry(JournalEntryDTO journalEntryDTO) {
        JournalEntry journal = new JournalEntry();
        journal.setContent(journalEntryDTO.getContent());
        journal.setUserId(journalEntryDTO.getUserId());

        return journalEntryRepository.save(journal);
    }

    @Override
    public JournalEntry getJournalById(Long journalId) {
        return journalEntryRepository.findById(journalId).orElseThrow((
        ) -> new RuntimeException("Journal not found with id: " + journalId));
    }

    @Override
    public List<JournalEntry> getAllJournalEntries(Long userId) {
        return journalEntryRepository.findByUserId(userId);
    }

    @Override
    public void deleteJournalEntry(Long journalId) throws Exception {
        if(journalEntryRepository.findById(journalId).isPresent()) {
            journalEntryRepository.deleteById(journalId);
        }
        else{
            throw new Exception("The Journal with that ID does not exist!");
        }
    }

    @Override
    public List<JournalEntry> getWeeklyEntries(Long userId) {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startTime = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
                                    .with(LocalTime.MIN);

        LocalDateTime endTime = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))
                                    .with(LocalTime.MAX);

        return journalEntryRepository.findByUserIdAndCreatedAtBetween(userId, startTime, endTime);
    }

    @Override
    public void generateReport(Long userId) throws JsonProcessingException {

        UserDTO userDTO = userServiceClient.getUserById(userId);

        String userName = userDTO.getUserName();
        String recipientEmail = userDTO.getEmail();

        List<JournalEntry> entries = getWeeklyEntries(userId);
        StringBuilder combinedText = new StringBuilder();
        for(JournalEntry entry: entries){
            String content = entry.getContent();
            combinedText.append(content);
            combinedText.append("\n\n--\n\n");
        }

        String combinedJournalText = combinedText.toString();


        // Get the raw report first in string format
        String rawStringReport = analysisService.getAnalysis(combinedJournalText);

        // Now parse it
        JournalReport report = objectMapper.readValue(rawStringReport, JournalReport.class);

        logger.info("Successfully parsed JSON report for user: {}", userDTO.getUserName());

        // Send it
        emailService.sendEmail(recipientEmail, userName, report);
    }
}
