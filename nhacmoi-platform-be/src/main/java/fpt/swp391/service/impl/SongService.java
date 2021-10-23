package fpt.swp391.service.impl;

import fpt.swp391.model.Song;
import fpt.swp391.repository.SongRepository;
import fpt.swp391.service.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SongService implements ISongService {

    @Autowired
    private SongRepository songRepository;

    @Override

    public Optional<Song> getSongById(String id) {
        return songRepository.findById(id);
    }

    @Override
    public Song saveSong(Song song) {

        return songRepository.save(song);
    }

    @Override
    public void deleteSongById(String id) {

        songRepository.deleteById(id);
    }

    @Override
    public List<Song> getListSongs() {
        return songRepository.findAll();
    }
}