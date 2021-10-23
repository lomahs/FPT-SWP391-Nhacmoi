package fpt.swp391.controller;

import fpt.swp391.model.Song;
import fpt.swp391.service.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/song")
public class SongController {

    private final ISongService songService;

    @Autowired
    public SongController(ISongService songService) {
        this.songService = songService;
    }

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        List<Song> songs = songService.getListSongs();

        return songs.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(songs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable("id") String id) {
        Song song = songService.getSongById(id);

        return song != null ? new ResponseEntity<>(song, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create")
    public ResponseEntity<Song> createSong(@RequestBody Song song) {

        return songService.saveSong(song) ? new ResponseEntity<>(song, HttpStatus.CREATED)
                : new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
    }

    @PutMapping("/edit")
    public ResponseEntity<Song> updateSong(@RequestBody Song song) {

        if (songService.getSongById(song.getSong_id()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return songService.saveSong(song) ? new ResponseEntity<>(song, HttpStatus.OK)
                : new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSong(@PathVariable("id") String id) {
        Song song = songService.getSongById(id);

        if (song == null) {
            return new ResponseEntity<>("Can't find id", HttpStatus.NOT_FOUND);
        }

        return songService.deleteSong(id) ? new ResponseEntity<>("Delete Successful", HttpStatus.OK)
                : new ResponseEntity<>("Can't find id", HttpStatus.NOT_FOUND);

    }

}