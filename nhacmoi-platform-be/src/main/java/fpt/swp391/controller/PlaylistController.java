package fpt.swp391.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fpt.swp391.model.Playlist;
import fpt.swp391.service.IPlaylistService;

@RestController
@RequestMapping("/api/playlist/")
public class PlaylistController {
    @Autowired
    private IPlaylistService iPlaylistService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllPlaylists() {
        try {
            List<Playlist> listPlaylists = iPlaylistService.getListPlaylists();
            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
            listPlaylists.forEach(playlist -> data.add(iPlaylistService.toJson(playlist)));
            return data.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                    : new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Map<String, Object>> getPlaylistById(@PathVariable("id") String id) {
        try {
            Playlist playlist = iPlaylistService.getPlaylistById(id);
            if (playlist != null) {
                Map<String, Object> data = iPlaylistService.toJson(playlist);
                return new ResponseEntity<>(data, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Playlist> createPlaylist(@RequestBody Map<String, Object> data) {
        try {
            Playlist playlist = iPlaylistService.toPlaylist(data);
            playlist.getListSongs().forEach(song -> song.getListPlaylists().add(playlist));
            playlist.setPlaylist_duration(iPlaylistService.calculateDuration(playlist));
            return iPlaylistService.savePlaylist(playlist) ? new ResponseEntity<>(HttpStatus.CREATED)
                    : new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Playlist> updatePlaylist(@PathVariable("id") String id,
            @RequestBody Map<String, Object> data) {
        try {
            Playlist playlist = iPlaylistService.getPlaylistById(id);
            if (playlist == null)
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            Playlist item = iPlaylistService.toPlaylist(data);
            playlist.setPlaylist_name(item.getPlaylist_name());
            playlist.setPlaylist_image(item.getPlaylist_image());
            playlist.setUser_created_id(item.getUser_created_id());
            // update list song
            playlist.getListSongs().forEach(song -> song.getListPlaylists().remove(playlist));
            item.getListSongs().forEach(song -> song.getListPlaylists().add(playlist));
            playlist.setListSongs(item.getListSongs());
            // update duration
            playlist.setPlaylist_duration(iPlaylistService.calculateDuration(playlist));

            return iPlaylistService.savePlaylist(playlist) ? new ResponseEntity<>(HttpStatus.OK)
                    : new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deletePlaylist(@PathVariable("id") String id) {
        try {
            return iPlaylistService.deletePlaylist(id) ? new ResponseEntity<>(HttpStatus.OK)
                    : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}