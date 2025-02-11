package org.dmitry2025.taskservice.other;

public record TaskResponse (
        Long id,
        Long userId,
        String title
){
}
