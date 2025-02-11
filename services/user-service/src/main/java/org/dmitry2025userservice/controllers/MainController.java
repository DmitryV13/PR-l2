package org.dmitry2025userservice.controllers;

import org.dmitry2025userservice.entities.User;
import org.dmitry2025userservice.other.TaskClient;
import org.dmitry2025userservice.other.UserReqResp;
import org.dmitry2025userservice.repos.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user-api/")
public class MainController {

    public static final String SUCH_USER_ALREADY_EXISTS = "Such user already exists.";
    public static final String USER_CREATED = "User created.";
    public static final String NAME_WAS_CHANGED = "Name was changed.";
    public static final String USER_NOT_FOUND = "User not found.";
    public static final String USER_DELETED = "User deleted.";

    private final UserRepository userRepository;
    private final TaskClient taskClient;

    public MainController(UserRepository userRepository, TaskClient taskClient) {
        this.userRepository = userRepository;
        this.taskClient = taskClient;
    }

    @PostMapping("/create")
    public String createUser(
            @RequestBody UserReqResp user
    ) {
        var checkUser = userRepository.findByEmail(user.email());
        if (checkUser.isPresent()) {
            return SUCH_USER_ALREADY_EXISTS;
        }

        var newUser = new User(user.name(), user.email());
        userRepository.save(newUser);

        //invocation of deleteTask method from task-service
        var taskCreationResp = taskClient.createTask(newUser.getId(), "First task");

        return USER_CREATED + taskCreationResp;
    }

    @PutMapping("/update-name")
    public String changeName(
            @RequestParam("email") String email,
            @RequestParam("name") String name
    ) {
        var user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return USER_NOT_FOUND;
        }
        user.get().setName(name);
        userRepository.save(user.get());
        return NAME_WAS_CHANGED;
    }

    @GetMapping("/get-all")
    public List<UserReqResp> getAllUsers() {
        var users = userRepository.findAll();
        return users.stream().map(u -> new UserReqResp(u.getName(), u.getEmail())).toList();
    }

    @DeleteMapping("/delete")
    public String deleteUser(
            @RequestParam("email") String email
    ){
        var user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return USER_NOT_FOUND;
        }

        //invocation of deleteTask method from task-service
        var taskDelResp = taskClient.deleteTask(user.get().getId());

        userRepository.delete(user.get());
        return USER_DELETED + taskDelResp;
    }

    @GetMapping("/verify")
    public Optional<Long> verifyUser(
            @RequestParam("email") String email
    ){
        var user = userRepository.findByEmail(email);
        return user.isEmpty() ? Optional.empty() : Optional.of(user.get().getId());
    }
}
