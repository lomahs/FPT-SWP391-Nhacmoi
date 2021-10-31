package fpt.swp391.controller;

import fpt.swp391.model.Artist;
import fpt.swp391.model.Song;
import fpt.swp391.service.IArtistService;
import fpt.swp391.service.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/song")
public class SongController {

    @Autowired
    private ISongService songService;

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
    public ResponseEntity<Song> createSong(@Valid @RequestBody Song song) {
        List<Artist> listArtists = new ArrayList<>();

        song.getArtist().forEach(artist -> {
            Optional<Artist> artistOptional = artistService.getArtistByName(artist.getArtist_name());

            listArtists.add(artistOptional.orElseGet(() -> {
                Artist a = new Artist(artist.getArtist_name());

                return artistService.saveArtist(a);
            }));
        });

        song.setArtist(new HashSet<>(listArtists));

        return new ResponseEntity<>(songService.saveSong(song), HttpStatus.CREATED);
    }

    @PutMapping("/edit")
    public ResponseEntity<Song> updateSong(@Valid @RequestBody Song song) {

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

    @GetMapping("stream/{id}")
    public ResponseEntity<Byte[]> getStreamSong(HttpServletResponse response, @PathVariable("id") String id) {
        // đường dẫn đến folder chứa file nhạc trong máy,
        // path của song ở database sẽ lưu tên bài hát.mp3, ví dụ Hello.mp3
        String url = "C:\\Users\\tuanm\\Desktop\\music\\";
        Optional<Song> songOptional = songService.getSongById(id);
        songOptional.map(song -> {
            try {

                Path path = Paths.get(url + song.getPath());
                response.setContentType("audio/mpeg");
                Files.copy(path, response.getOutputStream());
                response.flushBuffer();

            } catch (Exception ignored) {

            }

            return null;
        });

        return null;

    }

    @GetMapping("download/{id}")
    public ResponseEntity<Byte[]> getDownLoadSong(HttpServletResponse response, @PathVariable("id") String id) {
        // đường dẫn đến folder chứa file nhạc trong máy,
        // path của song ở database sẽ lưu tên bài hát.mp3, ví dụ Hello.mp3
        String url = "C:\\Users\\tuanm\\Desktop\\music\\";
        Optional<Song> songOptional = songService.getSongById(id);
        songOptional.map(song -> {
            try {

                Path path = Paths.get(url + song.getPath());
                response.setContentType("audio/mpeg");
                response.setHeader("Content-Disposition", "attachment;filename=" + song.getPath());
                Files.copy(path, response.getOutputStream());
                response.flushBuffer();

            } catch (Exception ignored) {

            }

            return null;
        });

        return null;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}