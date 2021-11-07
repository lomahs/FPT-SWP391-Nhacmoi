package fpt.swp391.controller;

import fpt.swp391.model.Account;
import fpt.swp391.model.Playlist;
import fpt.swp391.model.Song;
import fpt.swp391.service.IAccountService;
import fpt.swp391.service.IPlaylistService;
import fpt.swp391.service.ISongService;
import fpt.swp391.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/playlist")
public class PlaylistController {

    @Autowired
    private IPlaylistService playlistService;

    @Autowired
    private ISongService songService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private IAccountService accountService;

    @GetMapping
    public ResponseEntity<List<Playlist>> getAllPlaylists(@RequestHeader(value = "Authorization", required = false) String token) {

        if (token == null) {
            return new ResponseEntity<>(playlistService.getListPlaylistsOfUser("ADMIN"), HttpStatus.OK);
        }
        Account account = accountService.loadUserByAccountName(jwtService.getAccountNameFromToken(token));

        return new ResponseEntity<>(playlistService.getListPlaylistsOfUser(account.getAccount_name()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable("id") String id) {
        Optional<Playlist> playlistOptional = playlistService.getPlaylistById(id);

        return playlistOptional.map(playlist -> new ResponseEntity<>(playlist, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Playlist> createPlaylist(@Valid @RequestBody Playlist playlist) {

        return new ResponseEntity<>(playlistService.savePlaylist(playlist), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Playlist> updatePlaylist(@RequestBody Playlist playlist) {

        Optional<Playlist> playlistOptional = playlistService.getPlaylistById(playlist.getPlaylist_id());

        return playlistOptional.map(s -> {
                    s.setPlaylist_name(playlist.getPlaylist_name());
                    return new ResponseEntity<>(playlistService.savePlaylist(s), HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlaylist(@PathVariable("id") String id) {
        Optional<Playlist> playlistOptional = playlistService.getPlaylistById(id);

        return playlistOptional.map(song -> {
            playlistService.deletePlaylistById(id);
            return new ResponseEntity<>("Delete Successful", HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{playlistId}/{songId}")
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

    @PostMapping("/{playlistId}/{songId}")
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