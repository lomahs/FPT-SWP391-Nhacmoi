package fpt.swp391.service;

import fpt.swp391.model.User;

import java.util.List;

public interface IUserService {

    boolean saveUser(User user);

    boolean deleteUser(int id);

    List<User> getListUsers();
}