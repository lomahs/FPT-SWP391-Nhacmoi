package fpt.swp391.service.impl;

import fpt.swp391.model.Playlist;
import fpt.swp391.model.Song;
import fpt.swp391.model.User;
import fpt.swp391.repository.UserRepository;
import fpt.swp391.service.IPlaylistService;
import fpt.swp391.service.ISongService;
import fpt.swp391.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IPlaylistService playlistService;

    @Autowired
    private ISongService songService;

    @Override
    public User saveUser(User user) {

        return userRepository.save(user);
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
}