//package fpt.swp391.service.impl;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import fpt.swp391.model.Artist;
//import fpt.swp391.model.Song;
//import fpt.swp391.repository.ArtistRepository;
//import fpt.swp391.service.IArtistService;
//import fpt.swp391.service.ISongService;
//
//@Service
//public class ArtistServiceImpl implements IArtistService {
//
//    @Autowired
//    ArtistRepository artistRepository;
//
//    @Autowired
//    ISongService iSongService;
//
//    @Override
//    public Artist getArtistById(String id) {
//        return artistRepository.findById(id).orElse(null);
//    }
//
//    @Override
//    public boolean saveArtist(Artist artist) {
//        if (artist == null)
//            return false;
//        artistRepository.save(artist);
//        return true;
//    }
//
//    @Override
//    public boolean deleteArtist(String id) {
//        Artist artist = artistRepository.findById(id).orElse(null);
//        if (artist != null) {
//            artist.getListSong().forEach(song -> song.getArtist().remove(artist));
//            artistRepository.delete(artist);
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public List<Artist> getListArtists() {
//        return artistRepository.findAll();
//    }
//
//    private List<Map<String, Object>> filterObjectData(String entityName, Artist artist) {
//        List<Map<String, Object>> data = new ArrayList<>();
//        switch (entityName) {
//        case "song":
//            artist.getListSong().forEach(song -> {
//                Map<String, Object> s = iSongService.toJson(song);
//                s.remove("artists");
//                data.add(s);
//            });
//            break;
//        default:
//            break;
//        }
//        return data;
//    }
//
//    @Override
//    public Map<String, Object> toJson(Artist artist) {
//        Map<String, Object> data = new LinkedHashMap<>();
//        data.put("artist_id", artist.getArtist_id());
//        data.put("artist_name", artist.getArtist_name());
//        data.put("artist_image", artist.getImage());
//        data.put("songs", filterObjectData("song", artist));
//        return data;
//    }
//
//    @Override
//    public Artist toArtist(Map<String, Object> data) {
//        Artist artist = new Artist();
//        artist.setArtist_name((String) data.get("artist_name"));
//        artist.setImage((String) data.get("artist_image"));
//        List<String> listSongId = (List<String>) data.get("songs");
//        artist.setListSong(new ArrayList<>());
//        listSongId.forEach(id -> {
//            Song song = iSongService.getSongById(id);
//            artist.getListSong().add(song);
//        });
//        return artist;
//    }
//
//}