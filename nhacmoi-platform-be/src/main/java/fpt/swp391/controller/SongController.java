package fpt.swp391.controller;

import fpt.swp391.model.Song;
import fpt.swp391.service.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

        return new ResponseEntity<>(songService.getListSongs(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable("id") String id) {
        Optional<Song> songOptional = songService.getSongById(id);

        return songOptional.map(song -> new ResponseEntity<>(song, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public ResponseEntity<Song> createSong(@RequestBody Song song) {

        return new ResponseEntity<>(songService.saveSong(song), HttpStatus.CREATED);
    }

    @PutMapping("/edit")
    public ResponseEntity<Song> updateSong(@RequestBody Song song) {

        Optional<Song> songOptional = songService.getSongById(song.getSong_id());

        return songOptional.map(s -> new ResponseEntity<>(songService.saveSong(song), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSong(@PathVariable("id") String id) {
        Optional<Song> songOptional = songService.getSongById(id);

        return songOptional.map(song -> {
            songService.deleteSongById(id);
            return new ResponseEntity<>("Delete Successful", HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}