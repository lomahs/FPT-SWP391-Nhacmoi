package fpt.swp391.service;

import fpt.swp391.model.User;


import java.util.List;
import java.util.Map;

public interface IUserService {
    boolean saveUser(User user);

    boolean deleteUser(String id);

    List<User> getListUsers();

    User findUserByID(String id);

    Map<String, Object> toJson(User user, int detail);
}
