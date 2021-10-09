package fpt.swp391.service;

import fpt.swp391.model.Song;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ISongService {

    boolean saveSong(Song song);

    boolean deleteSong(int id);

    List<Song> getListSongs();
}