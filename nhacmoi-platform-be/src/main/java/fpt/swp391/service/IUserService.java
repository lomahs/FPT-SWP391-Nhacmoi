package fpt.swp391.service;

import fpt.swp391.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IUserService {
    User saveUser(User user);

    void deleteUserById(String id);

    List<User> getListUsers();

    Optional<User> getUserByID(String id);

    User registerNewUser(User user);

    User changePassword(Map<String, String> jsonData);

    User changeRole(Optional<User> userOptional, String role);
}