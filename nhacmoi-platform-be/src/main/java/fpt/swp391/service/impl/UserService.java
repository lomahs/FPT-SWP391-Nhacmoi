package fpt.swp391.service.impl;

import fpt.swp391.model.Playlist;
import fpt.swp391.model.Role;
import fpt.swp391.model.Song;
import fpt.swp391.model.User;
import fpt.swp391.repository.UserRepository;
import fpt.swp391.service.IPlaylistService;
import fpt.swp391.service.ISongService;
import fpt.swp391.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IPlaylistService playlistService;

    @Autowired
    private ISongService songService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {

        return userRepository.save(user);
    }

    @Override
    public User getUserByAccountName(String name) {
        return userRepository.getUserByAccountName(name);
    }

    @Override
    public void deleteUserById(String id) {
        Optional<User> userOptional = getUserByID(id);

        userOptional.ifPresent(
                user -> {
                    Set<Playlist> listPlaylists = new HashSet<>(user.getListPlaylist());
                    Set<Song> listSongs = new HashSet<>(user.getListSong());

                    listPlaylists.forEach(
                            playlist -> {
                                playlist.setOwner(null);

                                playlistService.savePlaylist(playlist);
                            }
                    );

                    listSongs.forEach(
                            song -> {
                                song.setAdder(null);

                                songService.saveSong(song);
                            }
                    );
                }
        );

        userRepository.deleteById(id);
    }

    @Override
    public List<User> getListUsers() {

        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserByID(String id) {

        return userRepository.findById(id);
    }

    @Override
    public User registerNewUser(User user) {
        user.getAccount().setPassword(passwordEncoder.encode(user.getAccount().getPassword()));
        user.getAccount().getRoles().add(new Role("ROLE_USER"));

        User newUser = saveUser(user);

        Playlist playlist = playlistService.savePlaylist(new Playlist("Liked Song", newUser));

        newUser.setListPlaylist(new HashSet<>());
        newUser.getListPlaylist().add(playlist);

        return newUser;
    }

    @Override
    public User changeRole(Optional<User> userOptional, String role) {

        return userOptional.map(user -> {
            user.getAccount().getRoles().add(new Role(role));
            return userRepository.save(user);
        }).orElse(null);
    }

    @Override
    public User changePassword(Map<String, String> jsonData) {
        String userId = jsonData.get("user_id");
        String oldPassword = jsonData.get("old_password");
        String newPassword = jsonData.get("new_password");

        String passRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–{}:;',?/*~$^+=<>]).{8,}$";

        Optional<User> userOptional = getUserByID(userId);

        return userOptional.map(user -> {
            if (newPassword.matches(passRegex) && passwordEncoder.matches(oldPassword, user.getAccount().getPassword())) {
                user.getAccount().setPassword(passwordEncoder.encode(newPassword));
                return saveUser(user);
            } else {
                return null;
            }
        }).orElse(null);
    }
}