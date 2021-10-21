package fpt.swp391.service;

import fpt.swp391.model.Playlist;

import java.util.List;
import java.util.Map;

public interface IPlaylistService {

    Playlist getPlaylistById(String id);

    boolean savePlaylist(Playlist playlist);

    boolean deletePlaylist(String id);

    List<Playlist> getListPlaylists();

    Map<String, Object> toJson(Playlist playlist);

    Playlist toPlaylist(Map<String, Object> data);

    public int calculateDuration(Playlist playlist);
}