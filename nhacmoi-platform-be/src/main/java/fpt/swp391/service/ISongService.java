package fpt.swp391.service;

import fpt.swp391.model.Song;

import java.util.List;
import java.util.Optional;

public interface ISongService {

    Optional<Song> getSongById(String id);

    Song saveSong(Song song);

    void deleteSongById(String id);

    List<Song> getListSongs();

//    Map<String, Object> toJson(Song song);
//
//    Song toSong(Map<String, Object> data);

}