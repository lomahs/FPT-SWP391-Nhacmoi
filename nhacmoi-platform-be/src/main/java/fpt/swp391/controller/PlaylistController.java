package fpt.swp391.controller;

import fpt.swp391.model.Playlist;
import fpt.swp391.model.Song;
import fpt.swp391.service.IPlaylistService;
import fpt.swp391.service.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/playlist")
public class PlaylistController {

    @Autowired
    private IPlaylistService playlistService;

    @Autowired
    private ISongService songService;

    @GetMapping
    public ResponseEntity<List<Playlist>> getAllPlaylists() {

        return new ResponseEntity<>(playlistService.getListPlaylists(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable("id") String id) {
        Optional<Playlist> playlistOptional = playlistService.getPlaylistById(id);

        return playlistOptional.map(playlist -> new ResponseEntity<>(playlist, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public ResponseEntity<Playlist> createPlaylist(@RequestBody Playlist playlist) {

        return new ResponseEntity<>(playlistService.savePlaylist(playlist), HttpStatus.CREATED);
    }

    @PutMapping("/edit")
    public ResponseEntity<Playlist> updatePlaylist(@RequestBody Playlist playlist) {

        Optional<Playlist> playlistOptional = playlistService.getPlaylistById(playlist.getPlaylist_id());

        return playlistOptional.map(s -> new ResponseEntity<>(playlistService.savePlaylist(playlist), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePlaylist(@PathVariable("id") String id) {
        Optional<Playlist> playlistOptional = playlistService.getPlaylistById(id);

        return playlistOptional.map(song -> {
            playlistService.deletePlaylistById(id);
            return new ResponseEntity<>("Delete Successful", HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/remove/{playlistId}/{songId}")
    public ResponseEntity<Playlist> removeSong(@PathVariable("playlistId") String playlistId,
                                               @PathVariable("songId") String songId) {
        Optional<Playlist> playlistOptional = playlistService.getPlaylistById(playlistId);
        Optional<Song> songOptional = songService.getSongById(songId);

        return playlistOptional.map(playlist -> {
            songOptional.ifPresent(playlist::removeSong);

            playlistService.savePlaylist(playlist);

            return new ResponseEntity<>(playlist, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @GetMapping("/add/{playlistId}/{songId}")
    public ResponseEntity<Playlist> addSong(@PathVariable("playlistId") String playlistId,
                                            @PathVariable("songId") String songId) {

        Optional<Playlist> playlistOptional = playlistService.getPlaylistById(playlistId);
        Optional<Song> songOptional = songService.getSongById(songId);

        return playlistOptional.map(playlist -> {
            songOptional.ifPresent(playlist::addSong);

            playlistService.savePlaylist(playlist);

            return new ResponseEntity<>(playlist, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
}