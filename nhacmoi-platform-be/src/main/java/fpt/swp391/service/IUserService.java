package fpt.swp391.service;

import fpt.swp391.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IUserService {

    boolean saveUser(User user);

    boolean deleteUser(int id);

    List<User> getListUsers();
}