package org.dmitry2025.taskservice.other;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(
        name = "user-service",
        url = "http://l2-user-cont:8888/user-api"
)
public interface UserClient {

    @GetMapping("/verify")
    public Optional<Long> verifyUser(@RequestParam("email") String email);
}
