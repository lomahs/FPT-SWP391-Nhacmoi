package fpt.swp391.service.impl;

import fpt.swp391.model.Playlist;
import fpt.swp391.model.Song;
import fpt.swp391.repository.PlaylistRepository;
import fpt.swp391.service.IPlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService implements IPlaylistService {

    @Autowired
    PlaylistRepository playlistRepository;

    @Override
    public Optional<Playlist> getPlaylistById(String id) {
        return playlistRepository.findById(id);
    }

    @Override
    public Playlist savePlaylist(Playlist playlist) {

        return playlistRepository.save(playlist);
    }

    @Override
    public void deletePlaylistById(String id) {

        playlistRepository.deleteById(id);
    }

    @Override
    public List<Playlist> getListPlaylists() {

        return playlistRepository.findAll();
    }


    public int calculateDuration(Playlist playlist) {
        int duration = 0;

        for (Song song : playlist.getListSongs()) {
            duration += song.getSong_duration();
        }
        return duration;
    }

}