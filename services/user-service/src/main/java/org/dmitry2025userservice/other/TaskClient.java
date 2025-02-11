package org.dmitry2025userservice.other;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(
        name = "task-service",
        url = "http://l2-task-cont:8222/task-api"
)
public interface TaskClient {

    @DeleteMapping("/delete-user-tasks")
    public String deleteTask(@RequestParam("userId") Long userId);

    @PostMapping("/create")
    public String createTask(
            @RequestParam("userId") Long userId,
            @RequestParam("title") String title
    );
}

