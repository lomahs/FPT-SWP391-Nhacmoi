package fpt.swp391.service;

import fpt.swp391.model.Playlist;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPlaylistService {

    boolean savePlaylist(Playlist playlist);

    boolean deletePlaylist(int id);

    List<Playlist> getListPlaylists();
}