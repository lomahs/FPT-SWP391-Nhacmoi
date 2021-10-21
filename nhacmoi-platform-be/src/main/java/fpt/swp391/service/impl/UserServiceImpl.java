package fpt.swp391.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fpt.swp391.model.Account;
import fpt.swp391.model.Role;
import fpt.swp391.model.User;
import fpt.swp391.repository.AccountRepository;
import fpt.swp391.repository.RoleRepository;
import fpt.swp391.repository.UserRepository;
import fpt.swp391.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Override
  public boolean saveUser(User user) {
    if (user != null) {
      userRepository.save(user);
      return true;
    }
    return false;
  }

  @Override
  public boolean deleteUser(String id) {
    if (id != null) {
      User user = userRepository.getById(id);
      user.getListSong().forEach(song -> song.setUser_added(null));
      if (user != null) {
        userRepository.delete(user);
        return true;
      }
    }
    return false;
  }

  @Override
  public List<User> getListUsers() {
    return userRepository.findAll();
  }

  @Override
  public User findUserByID(String id) {
    if (id != null) {
      User user = userRepository.getById(id);
      return user;
    }
    return null;
  }

  private List<Map<String, Object>> filterObject(String entity, User user) {
    List<Map<String, Object>> list = new ArrayList();
    switch (entity) {
      case "songs":
        user.getListSong().parallelStream().forEach(song -> {
          Map<String, Object> s = new LinkedHashMap<>();
          s.put("song_id", song.getSong_id());
          s.put("song_name", song.getSong_name());
          s.put("date_added", song.getDate_added());
          s.put("song_image", song.getSong_image());
          s.put("artist", song.getArtist().get(0).getArtist_name());
          list.add(s);
        });
        break;
      case "playlists":
        user.getListPlaylist().parallelStream().forEach(playlist -> {
          Map<String, Object> p = new LinkedHashMap<>();
          p.put("playlist_id", playlist.getPlaylist_id());
          p.put("playlist_name", playlist.getPlaylist_name());
          p.put("playlist_image", playlist.getPlaylist_image());
          list.add(p);
        });
        break;
      default:
        break;
    }
    return list;
    
  }

  @Override
  public Map<String, Object> toJson(User user, int detail) {
    Map<String, Object> data = new LinkedHashMap<>();
    data.put("user_id", user.getUser_id());
    data.put("user_name", user.getUser_name());
    data.put("user_email", user.getUser_email());
    data.put("sex", user.getSex());
    data.put("birthday", user.getBirthday());
    data.put("account", user.getAccount().getAccount_name());
    data.put("role", user.getAccount().getRole().getRole_name());
    if (detail != 0) {
      data.put("password", user.getAccount().getPassword());
      data.put("songs_list", filterObject("songs", user));
      data.put("playlist_list", filterObject("playlists", user));
    }
    return data;
  }

  // @Override
  // public User toUser(Map<String, Object> data) {
  //   User user = new User();
  //   user.setUser_name((String) data.get("user_name"));
  //   user.setUser_email((String) data.get("user_email"));
  //   user.setSex((char) data.get("sex"));
  //   // user.setBirthday(data.get("birthday")));
  //   Role role = roleRepository.getById((String) data.get("role"));

  //   user.setAccount(account);
  //   // Account acc = new Account((String) data.get("account_name"), (String) data.get("password"), role, user);
  //   // accountRepository.save(acc);
  //   // user.setAccount(acc);
  //   return user;
  // }

}
