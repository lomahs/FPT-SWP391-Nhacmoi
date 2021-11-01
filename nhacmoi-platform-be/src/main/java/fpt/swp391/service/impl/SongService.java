package fpt.swp391.service.impl;

import fpt.swp391.model.Playlist;
import fpt.swp391.model.Song;
import fpt.swp391.repository.SongRepository;
import fpt.swp391.service.IPlaylistService;
import fpt.swp391.service.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SongService implements ISongService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private IPlaylistService playlistService;

    public Optional<Song> getSongById(String id) {
        return songRepository.findById(id);
    }

    @Override
    public Song saveSong(Song song) {

        return songRepository.save(song);
    }

    @Override
    public void deleteSongById(String id) {

        Optional<Song> songOptional = getSongById(id);
        Set<Playlist> listPlaylists = new HashSet<>();
        songOptional.ifPresent(song -> {
            listPlaylists.addAll(song.getListPlaylists());
            listPlaylists.forEach(playlist -> {
                playlist.removeSong(song);

                playlistService.savePlaylist(playlist);
            });
            songRepository.deleteById(id);
        });

    }

    @Override
    public List<Song> getListSongs() {
        return songRepository.findAll();
    }

    @Override
    public List<Song> searchSongByName(String name) {

        return (List<Song>) songRepository.findBySongName(name);
    }

    @Override
    public List<Song> searchSongByNameAndPlaylistId(String name, String id) {
        return (List<Song>) songRepository.findBySongNameAndPlaylistId(name, id);
    }
}