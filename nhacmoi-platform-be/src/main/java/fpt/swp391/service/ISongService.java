package fpt.swp391.service;

import fpt.swp391.model.Song;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ISongService {

    Optional<Song> getSongById(String id);

    Song saveSong(Song song);

    void deleteSongById(String id);

    List<Song> getListSongs();


}