package com.junaid.ai_journal.controller;

import com.junaid.ai_journal.model.JournalEntry;
import com.junaid.ai_journal.payload.dto.CreateJournalRequest;
import com.junaid.ai_journal.payload.dto.JournalEntryDTO;
import com.junaid.ai_journal.security.JwtService;
import com.junaid.ai_journal.security.UserPrincipal;
import com.junaid.ai_journal.service.JournalService;
import com.junaid.ai_journal.service.impl.JournalServiceImpl;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/journals")
@RequiredArgsConstructor
public class JournalController {

    private final JournalService journalService;
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(JournalServiceImpl.class);

    @PostMapping
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody CreateJournalRequest createJournalRequest,
                                                           @RequestHeader("Authorization") String auth){

        String token = auth.replace("Bearer ", "");
        Long userId = jwtService.extractUserId(token);

        JournalEntryDTO dto = new JournalEntryDTO();
        dto.setContent(createJournalRequest.getContent());
        dto.setUserId(userId);

        JournalEntry createdEntry = journalService.createJournalEntry(dto);
        return new ResponseEntity<>(createdEntry, HttpStatus.CREATED);
    }

    @GetMapping("/my-journals")
    public ResponseEntity<List<JournalEntry>> getMyJournals(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            // This is a safeguard, but shouldn't be hit if the filter is working.
            return ResponseEntity.status(401).build();
        }

        // 1. Get the principal from the Authentication object.
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // 2. Safely get the userId.
        Long userId = userPrincipal.getUserId();

        // 3. Call the service.
        List<JournalEntry> entries = journalService.getAllJournalEntries(userId);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/user/{userId}/weekly")
    public ResponseEntity<List<JournalEntry>> getWeeklyEntriesForUser(@PathVariable Long userId) {
        List<JournalEntry> entries = journalService.getWeeklyEntries(userId);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/{journalId}")
    public ResponseEntity<JournalEntry> getJournalById(@PathVariable Long journalId){
        JournalEntry entry = journalService.getJournalById(journalId);
        return ResponseEntity.ok(entry);

    }
    @DeleteMapping("/{journalId}")
    public ResponseEntity<Void> deleteJournalEntry(@PathVariable Long journalId) throws Exception {
            journalService.deleteJournalEntry(journalId);
            return ResponseEntity.noContent().build();
    }
// --- NEW TEST ENDPOINT ---
    /**
     * A test endpoint to manually trigger the AI weekly report generation for a user.
     * In a real production app, this would likely only be run by the scheduler.
     * Handles POST requests to "/api/journals/user/{userId}/generate-report".
     *
     * @param userId The ID of the user to generate the report for.
     * @return A confirmation message.
     */
    @PostMapping("/user/generate-report")
    @RateLimiter(name = "reportLimiter", fallbackMethod = "reportLimiter")
    public ResponseEntity<String> triggerWeeklyReport(@RequestHeader("Authorization") String auth) {

        try {
            String token = auth.replace("Bearer ", "");
            Long userId = jwtService.extractUserId(token);
            // Call the existing service method that contains all the logic.
            journalService.generateReport(userId);

            return ResponseEntity.ok("Report generated successfully!");

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong!");
        }
    }

    public ResponseEntity<String> reportLimiter(String auth, Throwable throwable){
        logger.warn("Rate limit hit for auth token [{}]. Reason: {}", auth, throwable.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Too many requests. Please try again later.");
    }
}
