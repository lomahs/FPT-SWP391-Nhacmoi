package fpt.swp391.controller;

import fpt.swp391.model.Account;
import fpt.swp391.model.LoginResponse;
import fpt.swp391.model.User;
import fpt.swp391.service.IAccountService;
import fpt.swp391.service.IPlaylistService;
import fpt.swp391.service.IUserService;
import fpt.swp391.service.JwtService;
import fpt.swp391.service.impl.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userServices;

    @Autowired
    private IUserService userService;

    @Autowired
    private IPlaylistService playlistService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {

        return new ResponseEntity<>(userService.registerNewUser(user), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<User> changePassword(@Valid @RequestBody Map<String, String> jsonData) {

        User user = userService.changePassword(jsonData);

        return user != null ? new ResponseEntity<>(user, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @PutMapping("/changeRole")
    public ResponseEntity<User> changeRole(@Valid @RequestBody Map<String, String> jsonData) {
        String userId = jsonData.get("user_id");
        String role = jsonData.get("role");

        User user = userService.changeRole(userService.getUserByID(userId), role);

        return user != null ? new ResponseEntity<>(user, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
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
    public ResponseEntity<LoginResponse> login(@RequestBody Account account) {
        String token = "";
        HttpStatus httpStatus = null;
        LoginResponse loginResponse;

        try {
            if (accountService.checkLogin(account)) {
                token = jwtService.generateTokenLogin(account.getAccount_name());
                User user = userService.getUserByAccountName(account.getAccount_name());

                loginResponse = new LoginResponse(user, token, "Login Successful");
                httpStatus = HttpStatus.OK;
            } else {
                loginResponse = new LoginResponse(null, null, "Wrong AccountName or Password");
                httpStatus = HttpStatus.BAD_REQUEST;
            }
        } catch (Exception ex) {
            loginResponse = new LoginResponse(null, null, "User email is not confirmed.");
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(loginResponse, httpStatus);
    }

    @GetMapping(path = "/register/confirm")
    public ResponseEntity<LoginResponse> confirm(@RequestParam("token") String token){
        return userServices.confirmToken(token);
    }
}