package fpt.swp391.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fpt.swp391.model.Account;
import fpt.swp391.model.User;
import fpt.swp391.repository.AccountRepository;
import fpt.swp391.repository.RoleRepository;
import fpt.swp391.service.IUserService;

@RestController
@RequestMapping("/api/user/")
public class UserController {
  @Autowired
  private IUserService iUserService;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private AccountRepository accountRepository;

  @PostMapping
  public ResponseEntity<User> create(@RequestBody User user) {
    try {
      // User u = iUserService.toUser(user);
      user.getAccount().setRole(roleRepository.getById("member"));
      iUserService.saveUser(user);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
    }
  }

  @PutMapping("{id}")
  public ResponseEntity<User> update(@PathVariable("id") String id, @RequestBody Map<String, Object> data) {
    User user = iUserService.findUserByID(id);
    if (user != null) {
      user.setUser_name((String)data.get("user_name"));
      // user.setBirthday(data.getBirthday());
      String sex = (String)data.get("sex");
      user.setSex(sex.charAt(0));
      user.setUser_email((String)data.get("user_email"));
      Account acc = user.getAccount();
      acc.setPassword((String) data.get("password"));
      acc.setRole(roleRepository.getById((String) data.get("role")));
      if(iUserService.saveUser(user));
        return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }


  @GetMapping
  public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
    try {
      List<Map<String, Object>> listUsers = new ArrayList<Map<String, Object>>();
      List<User> users = iUserService.getListUsers();
      for (User user : users) {
        listUsers.add(iUserService.toJson(user,0));
      }
      
      return new ResponseEntity<>(listUsers, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("{id}")
  public ResponseEntity<Map<String, Object>> getUser(@PathVariable("id") String id) {
    try {
      User user = iUserService.findUserByID(id);
      if (user != null) {
        Map<String, Object> detail = iUserService.toJson(user, 1);
        return new ResponseEntity<>(detail, HttpStatus.OK);
      }
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  @DeleteMapping("{id}")
  public ResponseEntity<HttpStatus> delete(@PathVariable("id") String id) {
    try {
      iUserService.deleteUser(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    }
  }  
}
