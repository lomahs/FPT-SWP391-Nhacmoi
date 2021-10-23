package fpt.swp391.service;

import fpt.swp391.model.Song;

import java.util.List;

public interface ISongService {

    Song getSongById(String id);

    boolean saveSong(Song song);

    boolean deleteSong(String id);

    List<Song> getListSongs();

//    Map<String, Object> toJson(Song song);
//
//    Song toSong(Map<String, Object> data);

}