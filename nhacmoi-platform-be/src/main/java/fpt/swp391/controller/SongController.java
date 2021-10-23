package fpt.swp391.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.util.ArrayList;

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

import fpt.swp391.model.Song;
import fpt.swp391.model.Playlist;
import fpt.swp391.service.ISongService;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/song/")
public class SongController {
    @Autowired
    private ISongService iSongService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllSongs() {
        try {
            List<Song> songs = iSongService.getListSongs();
            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
            songs.forEach(song -> data.add(iSongService.toJson(song)));
            return data.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                    : new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Map<String, Object>> getSongById(@PathVariable("id") String id) {
        try {
            Song song = iSongService.getSongById(id);
            if (song != null) {
                Map<String, Object> data = iSongService.toJson(song);
                return new ResponseEntity<>(data, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("stream/{id}")
    public ResponseEntity<Byte[]> getStreamSong(HttpServletResponse response, @PathVariable("id") String id){
        // đường dẫn đến folder chứa file nhạc trong máy,
        // path của song ở database sẽ lưu tên bài hát.mp3, ví dụ Hello.mp3
        String url = "C:\\Users\\tuanm\\Desktop\\music\\";
        try{
            Song song = iSongService.getSongById(id);
            Path path = Paths.get(url+song.getPath());
            response.setContentType("audio/mpeg");
            Files.copy(path,response.getOutputStream());
            response.flushBuffer();

        }catch (Exception ignored){

        }

        return null;
    }

    @GetMapping("download/{id}")
    public ResponseEntity<Byte[]> getDownLoadSong(HttpServletResponse response, @PathVariable("id") String id){
        // đường dẫn đến folder chứa file nhạc trong máy
        String url = "C:\\Users\\tuanm\\Desktop\\music\\";
        try{
            Song song = iSongService.getSongById(id);
            Path path = Paths.get(url+song.getPath());
            response.setContentType("audio/mpeg");
            response.setHeader( "Content-Disposition", "attachment;filename=" + song.getPath());
            Files.copy(path,response.getOutputStream());
            response.flushBuffer();
        }catch (Exception ignored){

        }

        return null;
    }



    @PostMapping
    public ResponseEntity<Song> createSong(@RequestBody Map<String, Object> data) {
        try {
            Song song = iSongService.toSong(data);
            song.setDate_added(LocalDate.now());
            song.setStream_count(0);
            song.getCategories().forEach(cate -> cate.getListSong().add(song));
            song.getArtist().forEach(artist -> artist.getListSong().add(song));
            List<Playlist> playlists = song.getListPlaylists();
            playlists.forEach(playlist -> playlist.getListSongs().add(song));

            return iSongService.saveSong(song) ? new ResponseEntity<>(HttpStatus.CREATED)
                    : new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Song> updateSong(@PathVariable("id") String id, @RequestBody Map<String, Object> data) {
        try {
            Song song = iSongService.getSongById(id);
            if (song == null)
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            Song item = iSongService.toSong(data);
            song.setArtist(item.getArtist());
            song.setDate_added(item.getDate_added());
            song.setPath(item.getPath());
            song.setSong_duration(item.getSong_duration());
            song.setSong_image(item.getSong_image());
            song.setSong_name(item.getSong_name());
            song.setUser_added(item.getUser_added());
            song.setStream_count((int) data.get("stream_count"));
            // update category
            song.getCategories().forEach(cate -> cate.getListSong().remove(song));
            item.getCategories().forEach(cate -> cate.getListSong().add(song));
            song.setCategories(item.getCategories());
            // update artist
            song.getArtist().forEach(artist -> artist.getListSong().remove(song));
            item.getArtist().forEach(artist -> artist.getListSong().add(song));
            song.setArtist(item.getArtist());
            // update playlist
            song.getListPlaylists().forEach(playlist -> playlist.getListSongs().remove(song));
            item.getListPlaylists().forEach(playlist -> playlist.getListSongs().add(song));
            song.setListPlaylists(item.getListPlaylists());

            return iSongService.saveSong(song) ? new ResponseEntity<>(HttpStatus.OK)
                    : new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteSong(@PathVariable("id") String id) {
        try {
            return iSongService.deleteSong(id) ? new ResponseEntity<>(HttpStatus.OK)
                    : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}