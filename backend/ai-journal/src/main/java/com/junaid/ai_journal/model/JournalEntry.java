package com.junaid.ai_journal.model;

import com.junaid.ai_journal.config.AesEncryptor;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long journalId;

    @Column(nullable = false)
    private Long userId;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    @Convert(converter = AesEncryptor.class)
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

}
