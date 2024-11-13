package com.tpo.TPO.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tpo.TPO.entity.User;
import com.tpo.TPO.repository.UserRepository;
import com.tpo.TPO.service.UserService;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    private UserRepository userRepository;
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam String contains,
            @RequestParam int skip,
            @RequestParam int limit,
            @RequestParam(required = false) String orderby) {
        List<User> users = userService.getAllUsers(contains, skip, limit, orderby);
        return ResponseEntity.ok(users);
    }

    // Get User
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Post User
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // Update User
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Integer userId, @RequestBody User user) {
        User updatedUser = userService.updateUser(userId, user);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    // Delete User
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Get Comments
    @GetMapping("/{userId}/comments")
    public ResponseEntity<Integer> getUserComments(@PathVariable Integer userId) {
        int commentsCount = userService.getUserCommentsCount(userId);
        return ResponseEntity.ok(commentsCount);
    }

    // Endpoint para obtener los seguidores de un usuario
    @GetMapping("/{userId}/followers")
    public ResponseEntity<Set<User>> getFollowers(@PathVariable Integer userId) {
        Set<User> followers = userService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    // Endpoint para obtener los usuarios seguidos por un usuario
    @GetMapping("/{userId}/followed")
    public ResponseEntity<Set<User>> getFollowedUsers(@PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        Set<Integer> followedIds = user.getFollowedIds();
    
        // Consulta los usuarios por sus IDs
        Set<User> followedUsers = userService.getFollowedUsers(followedIds);
        return ResponseEntity.ok(followedUsers);
    }
    
    

    @PostMapping("/{userId}/follow/{followUserId}")
    public ResponseEntity<String> followUser(
            @PathVariable Integer userId,
            @PathVariable Integer followUserId) {
        try {
            User u = userService.getUserById(userId);
            System.out.println(u.getFollowersIds());
            userService.followUser(userId, followUserId);
            return ResponseEntity.status(HttpStatus.CREATED).body("User followed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User is already followed");
        } catch (RuntimeException e) {
            System.out.println(userId);
            System.out.println(followUserId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    // Get User by Email
    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/isEmailUsed")
    public ResponseEntity<Boolean> isEmailUsed(@RequestParam String email) {
        boolean exists = userService.isEmailUsed(email);
        return ResponseEntity.ok(exists);
    }

    // Get User by Username
    @GetMapping("/isUsernameUsed")
    public ResponseEntity<Boolean> isUsernameUsed(@RequestParam String username) {
        boolean exists = userService.isUsernameUsed(username);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/getId")
    public ResponseEntity<?> getId(@RequestHeader("Authorization") String authorizationHeader) {

        Integer userId = userService.getIdfromToken(authorizationHeader);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }

        return ResponseEntity.ok(userId);
    }

    // Get Random Users
    @GetMapping("/random")
    public ResponseEntity<List<User>> getRandomUsers(@RequestParam int limit) {
        Pageable pageable = PageRequest.of(0, limit); // Create pageable with 0 as page number and 'limit' as size
        List<User> randomUsers = userService.getRandomUsers(pageable); // Pass pageable to the service
        if (randomUsers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(randomUsers);
    }

}
