package fpt.swp391.controller;

import fpt.swp391.model.Account;
import fpt.swp391.model.Playlist;
import fpt.swp391.model.Role;
import fpt.swp391.model.User;
import fpt.swp391.service.IAccountService;
import fpt.swp391.service.IPlaylistService;
import fpt.swp391.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IPlaylistService playlistService;

    @Autowired
    private IAccountService accountService;

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {

        Account account = user.getAccount();

        if (account.getRole() == null) {
            account.setRole(new Role("user"));
        }

        User newUser = userService.saveUser(user);

        Playlist playlist = playlistService.savePlaylist(new Playlist("Liked Song", newUser));

        newUser.setListPlaylist(new HashSet<>());
        newUser.getListPlaylist().add(playlist);

        return new ResponseEntity<>(userService.saveUser(newUser), HttpStatus.CREATED);
    }

    @PutMapping("/edit")
    public ResponseEntity<User> changePassword(@Valid @RequestBody Map<String, String> jsonData) {

        String userId = jsonData.get("user_id");
        String password = jsonData.get("password");

        Optional<User> userOptional = userService.getUserByID(userId);

        return userOptional.map(user -> {
            user.getAccount().setPassword(password);
            accountService.saveAccount(user.getAccount());
            return new ResponseEntity<>(user, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {

        return new ResponseEntity<>(userService.getListUsers(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") String id) {
        Optional<User> userOptional = userService.getUserByID(id);

        return userOptional.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
        Optional<User> userOptional = userService.getUserByID(id);

        return userOptional.map(user -> {

            userService.deleteUserById(id);
            return new ResponseEntity<>("Delete Successful", HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        System.out.println(errors);
        return errors;
    }
}