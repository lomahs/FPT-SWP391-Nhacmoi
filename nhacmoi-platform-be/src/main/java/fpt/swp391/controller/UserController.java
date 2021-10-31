package fpt.swp391.controller;

import fpt.swp391.model.Account;
import fpt.swp391.model.Playlist;
import fpt.swp391.model.Role;
import fpt.swp391.model.User;
import fpt.swp391.service.IAccountService;
import fpt.swp391.service.IPlaylistService;
import fpt.swp391.service.IUserService;
import fpt.swp391.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IPlaylistService playlistService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private JwtService jwtService;

    @PostMapping
    @Transactional
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {

        Account account = user.getAccount();

        Set<Role> roles = new HashSet<>();
        roles.add(new Role("ROLE_USER"));
        account.setRoles(roles);

        User newUser = userService.saveUser(user);

        Playlist playlist = playlistService.savePlaylist(new Playlist("Liked Song", newUser));

        newUser.setListPlaylist(new HashSet<>());
        newUser.getListPlaylist().add(playlist);

        return new ResponseEntity<>(userService.saveUser(newUser), HttpStatus.CREATED);
    }

    @PutMapping
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

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") String id) {
        Optional<User> userOptional = userService.getUserByID(id);

        return userOptional.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Account account) {
        String result = "";
        HttpStatus httpStatus = null;

        try {
            if (accountService.checkLogin(account)) {
                result = jwtService.generateTokenLogin(account.getAccount_name());
                httpStatus = HttpStatus.OK;
            } else {
                result = "Wrong AccountName or password";
                httpStatus = HttpStatus.BAD_REQUEST;
            }
        } catch (Exception ex) {
            result = "Server Error";
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(result, httpStatus);
    }
}