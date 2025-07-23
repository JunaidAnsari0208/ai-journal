package com.junaid.ai_journal.payload.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
public class JournalEntryDTO {

    @NotNull(message = "User ID cannot be null!")
    private Long userId;
    @NotBlank(message = "Content cannot be empty")
    private String content;
}
