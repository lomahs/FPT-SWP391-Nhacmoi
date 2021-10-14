package fpt.swp391.service;

import fpt.swp391.model.Playlist;

import java.util.List;

public interface IPlaylistService {

    boolean savePlaylist(Playlist playlist);

    boolean deletePlaylist(int id);

    List<Playlist> getListPlaylists();
}