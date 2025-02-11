package org.dmitry2025.taskservice.controllers;

import org.dmitry2025.taskservice.entities.Task;
import org.dmitry2025.taskservice.other.TaskResponse;
import org.dmitry2025.taskservice.other.UserClient;
import org.dmitry2025.taskservice.repos.TaskRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task-api")
public class MainController {

    public static final String TASKS_DELETED = "Tasks deleted";
    public static final String TASK_CREATED = "Task created";
    public static final String THERE_IS_NO_TASK_WITH_THIS_ID = "There is no task with this id";
    public static final String USER_DOESN_T_EXIST = "User doesn't exist";
    public static final String TASK_UPDATED = "Task updated";
    public static final String USER_IS_NOT_THE_OWNER_OF_THIS_TASK = "User is not the owner of this task";

    private final TaskRepository taskRepository;
    private final UserClient userClient;

    public MainController(TaskRepository taskRepository, UserClient userClient) {
        this.taskRepository = taskRepository;
        this.userClient = userClient;
    }

    @PostMapping("/create")
    public String createTask(
            @RequestParam("userId") Long userId,
            @RequestParam("title") String title
    ){
        var task = new Task(title, userId);
        taskRepository.save(task);
        return TASK_CREATED;
    }

    @PutMapping("/update-title")
    public String changeTaskTitle(
            @RequestParam("userEmail") String email,
            @RequestParam("taskId") Long taskId,
            @RequestParam("title") String title
    ){
        var task = taskRepository.findById(taskId);
        if(task.isEmpty()){
            return THERE_IS_NO_TASK_WITH_THIS_ID;
        }

        //gettin userId from user-service by email
        var response = userClient.verifyUser(email);

        if(response.isEmpty()) {
            return USER_DOESN_T_EXIST;
        }

        if(response.get() == task.get().getUserId()) {
            task.get().setTitle(title);
            taskRepository.save(task.get());
        }else{
            return USER_IS_NOT_THE_OWNER_OF_THIS_TASK;
        }

        return TASK_UPDATED;
    }

    @DeleteMapping("/delete-user-tasks")
    public String deleteTask(
            @RequestParam("userId") Long userId
    ){
        var tasks = taskRepository.findAllByUserId(userId);
        for(var task : tasks) {
            taskRepository.delete(task);
        }
        return TASKS_DELETED;
    }

    @GetMapping("/get-all")
    public List<TaskResponse> getAllTasks(){
        var tasks = taskRepository.findAll();
        return tasks.stream()
                .map(u ->
                        new TaskResponse(u.getId(), u.getUserId(), u.getTitle()))
                .toList();
    }
}
