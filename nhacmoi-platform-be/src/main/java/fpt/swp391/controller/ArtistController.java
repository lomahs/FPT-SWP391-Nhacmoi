//package fpt.swp391.controller;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import fpt.swp391.model.Artist;
//import fpt.swp391.service.IArtistService;
//
//@RestController
//@RequestMapping("/api/artist/")
//public class ArtistController {
//    @Autowired
//    IArtistService iArtistService;
//
//    @GetMapping
//    public ResponseEntity<List<Map<String, Object>>> getAllArtists() {
//        try {
//            List<Artist> artists = iArtistService.getListArtists();
//            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
//            artists.forEach(Artist -> data.add(iArtistService.toJson(Artist)));
//            return data.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
//                    : new ResponseEntity<>(data, HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @GetMapping("{id}")
//    public ResponseEntity<Map<String, Object>> getArtistById(@PathVariable("id") String id) {
//        try {
//            Artist artist = iArtistService.getArtistById(id);
//            if (artist != null) {
//                Map<String, Object> data = iArtistService.toJson(artist);
//                return new ResponseEntity<>(data, HttpStatus.OK);
//            }
//            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @PostMapping
//    public ResponseEntity<Artist> createArtist(@RequestBody Map<String, Object> data) {
//        try {
//            Artist artist = iArtistService.toArtist(data);
//            artist.getListSong().forEach(song -> song.getArtist().add(artist));
//            return iArtistService.saveArtist(artist) ? new ResponseEntity<>(HttpStatus.CREATED)
//                    : new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @PutMapping("{id}")
//    public ResponseEntity<Artist> updateArtist(@PathVariable("id") String id, @RequestBody Map<String, Object> data) {
//        try {
//            Artist artist = iArtistService.getArtistById(id);
//            if (artist == null)
//                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//            Artist item = iArtistService.toArtist(data);
//            artist.setArtist_name(item.getArtist_name());
//            artist.setImage(item.getImage());
//            // update list song
//            artist.getListSong().forEach(song -> song.getArtist().remove(artist));
//            item.getListSong().forEach(song -> song.getArtist().add(artist));
//            artist.setListSong(item.getListSong());
//
//            return iArtistService.saveArtist(artist) ? new ResponseEntity<>(HttpStatus.OK)
//                    : new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @DeleteMapping("{id}")
//    public ResponseEntity<HttpStatus> deletePlaylist(@PathVariable("id") String id) {
//        try {
//            return iArtistService.deleteArtist(id) ? new ResponseEntity<>(HttpStatus.OK)
//                    : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//}