package com.junaid.ai_journal.client;

import com.junaid.ai_journal.payload.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {

    @GetMapping("/api/users/{userId}")
    UserDTO getUserById(@PathVariable Long userId);
}
