package fpt.swp391.controller;

import fpt.swp391.model.Artist;
import fpt.swp391.model.Song;
import fpt.swp391.service.IArtistService;
import fpt.swp391.service.IPlaylistService;
import fpt.swp391.service.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/song")
public class SongController {

    @Autowired
    private ISongService songService;

    @Autowired
    private IPlaylistService playlistService;

    @Autowired
    private IArtistService artistService;

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
        List<Artist> listArtists = new ArrayList<>();

        song.getArtist().forEach(
                artist -> {
                    Optional<Artist> artistOptional = artistService.getArtistByName(artist.getArtist_name());

                    listArtists.add(artistOptional.orElseGet(() -> {
                        Artist a = new Artist(artist.getArtist_name());

                        return artistService.saveArtist(a);
                    }));
                }
        );

        song.setArtist(new HashSet<>(listArtists));

        return new ResponseEntity<>(songService.saveSong(song), HttpStatus.CREATED);
    }

    @PutMapping("/edit")
    public ResponseEntity<Song> updateSong(@RequestBody Song song) {

        Optional<Song> songOptional = songService.getSongById(song.getSong_id());

        return songOptional.map(s -> new ResponseEntity<>(songService.saveSong(song), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<String> deleteSong(@PathVariable("id") String id) {
        Optional<Song> songOptional = songService.getSongById(id);

        return songOptional.map(song -> {

            songService.deleteSongById(id);
            return new ResponseEntity<>("Delete Successful", HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}