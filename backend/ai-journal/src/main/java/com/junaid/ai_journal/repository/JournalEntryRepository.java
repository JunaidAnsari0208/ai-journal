package com.junaid.ai_journal.repository;

import com.junaid.ai_journal.model.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {

    public List<JournalEntry> findByUserIdAndCreatedAtBetween(Long userId,
                                                              LocalDateTime startTime,
                                                              LocalDateTime endTime);
    public List<JournalEntry> findByUserId(Long userId);

    @Query("SELECT DISTINCT j.userId FROM JournalEntry j")
    List<Long> findDistinctUserIds();
}
