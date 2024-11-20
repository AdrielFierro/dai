package com.tpo.TPO.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tpo.TPO.controller.dto.UserDTO;
import com.tpo.TPO.entity.User;
import com.tpo.TPO.service.ImageService;
import com.tpo.TPO.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;

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
    
    @GetMapping("/search/{username}")
    public ResponseEntity<List<User>> searchUsersByUsername(@PathVariable String username) {
        List<User> users = userService.searchUsersByUsername(username);
        return users.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).build() : ResponseEntity.ok(users);
    }
    

    // Post User
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // Update User
    @PutMapping(value = "/{userId}", consumes = "multipart/form-data")
    public ResponseEntity<User> updateUser(
        @PathVariable Integer userId,
        @ModelAttribute UserDTO userDto
    ) throws IOException {
        ArrayList<String> urlImage = imageService.fileToURL(userDto.getUrlImage());
        ArrayList<String> backImage = imageService.fileToURL(userDto.getBackImage());
        

        User user = userService.getUserById(userId);
        safeSet(userDto.getName(), user::setName);
        safeSet(userDto.getLastName(), user::setLastName);
        safeSet(userDto.getDescripcion(), user::setDescription);
        safeSet(userDto.getUsername(), user::setUsername);

        user.setBackgroundImage(backImage.get(0));
        user.setUrlImage(urlImage.get(0));
        User updatedUser = userService.updateUser(userId, user);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }
    
    private void safeSet(String newValue, Consumer<String> setter) {
        if (newValue != null && !newValue.isEmpty()) {
            setter.accept(newValue);
        }
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

    // Endpoint para dejar de seguir a un usuario
    @DeleteMapping("/{userId}/unfollow/{unfollowUserId}")
    public ResponseEntity<String> unfollowUser(
            @PathVariable Integer userId,
            @PathVariable Integer unfollowUserId) {
        try {
            // Obtén el usuario y verifica si está siguiendo al usuario
            User u = userService.getUserById(userId);
            if (u == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            
            // Llama al servicio para dejar de seguir al usuario
            userService.unfollowUser(userId, unfollowUserId);
            return ResponseEntity.status(HttpStatus.OK).body("User unfollowed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User is not following the specified user");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User to unfollow not found");
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
    @GetMapping("/random/{userId}")
    public ResponseEntity<List<User>> getRandomUsers(@PathVariable Integer userId) {
        List<User> randomUsers = userService.getRandomUsers(userId); // Pasamos el userId
        if (randomUsers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        System.out.println(randomUsers);
        return ResponseEntity.ok(randomUsers);
    }
    

}
