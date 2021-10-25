package fpt.swp391.service;

import fpt.swp391.model.Playlist;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IPlaylistService {

    Optional<Playlist> getPlaylistById(String id);

    Playlist savePlaylist(Playlist playlist);

    void deletePlaylistById(String id);

    List<Playlist> getListPlaylists();

    public int calculateDuration(Playlist playlist);

}