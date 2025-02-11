package org.dmitry2025.taskservice.repos;

import org.dmitry2025.taskservice.entities.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface TaskRepository extends CrudRepository<Task, Long> {
    Set<Task> findAll();
    Set<Task> findAllByUserId(Long userId);

    Optional<Task> findById(Long id);
}
