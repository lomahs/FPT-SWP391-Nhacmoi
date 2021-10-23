package fpt.swp391.service.impl;

import fpt.swp391.model.Song;
import fpt.swp391.repository.SongRepository;
import fpt.swp391.service.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SongService implements ISongService {

    @Autowired
    private SongRepository songRepository;

    @Override

    public Song getSongById(String id) {
        return songRepository.findById(id).orElse(null);
    }

    @Override
    public boolean saveSong(Song song) {
        if (song == null)
            return false;

        song.setDate_added(LocalDate.now());
        song.setStream_count(0);
        songRepository.save(song);
        return true;
    }

    @Override
    public boolean deleteSong(String id) {

        Song s = getSongById(id);
        if (s != null) {
            songRepository.deleteById(id);
            return true;
        }


        return false;
    }

    @Override
    public List<Song> getListSongs() {
        return songRepository.findAll();
    }

//    // Lọc bớt dữ liệu đẩy lên
//    private List<Map<String, Object>> filterObjectData(String entityName, Song song) {
//        List<Map<String, Object>> data = new ArrayList<>();
//        switch (entityName) {
//            case "category":
//                song.getCategories().forEach(category -> {
//                    Map<String, Object> c = new LinkedHashMap<>();
//                    c.put("cate_id", category.getCategory_id());
//                    c.put("cate_name", category.getCategory_name());
//                    data.add(c);
//                });
//                break;
//            case "user":
//                User user = song.getUser_added();
//                Map<String, Object> u = new LinkedHashMap<>();
//                u.put("user_id", user.getUser_id());
//                u.put("user_name", user.getUser_name());
//                u.put("account_name", user.getAccount().getAccount_name());
//                data.add(u);
//                break;
//            case "artist":
//                song.getArtist().forEach(artist -> {
//                    Map<String, Object> a = new LinkedHashMap<>();
//                    a.put("artist_id", artist.getArtist_id());
//                    a.put("artist_name", artist.getArtist_name());
//                    data.add(a);
//                });
//                break;
//            case "playlist":
//                song.getListPlaylists().forEach(playlist -> {
//                    Map<String, Object> p = new LinkedHashMap<>();
//                    p.put("playlist_id", playlist.getPlaylist_id());
//                    p.put("playlist_name", playlist.getPlaylist_name());
//                    p.put("playlist_image", playlist.getPlaylist_image());
//                    data.add(p);
//                });
//            default:
//                break;
//        }
//        return data;
//    }
//
//    @Override
//    // Map Entity to DTO
//    public Map<String, Object> toJson(Song song) {
//        Map<String, Object> data = new LinkedHashMap<>();
//        data.put("song_id", song.getSong_id());
//        data.put("song_name", song.getSong_name());
//        data.put("categories", filterObjectData("category", song));
//        data.put("path", song.getPath());
//        data.put("user_added", filterObjectData("user", song).get(0));
//        data.put("artists", filterObjectData("artist", song));
//        data.put("image", song.getSong_image());
//        data.put("duration", song.getSong_duration());
//        data.put("date_added", song.getDate_added());
//        data.put("stream_count", song.getStream_count());
//        data.put("playlists", filterObjectData("playlist", song));
//        return data;
//    }
//
//    @Override
//    // Map DTO to Entiy
//    public Song toSong(Map<String, Object> data) {
//        Song song = new Song();
//        song.setSong_name((String) data.get("song_name"));
//        song.setPath((String) data.get("path"));
//        User user_added = userRepository.getById((String) data.get("user_added"));
//        song.setUser_added(user_added);
//        song.setSong_image((String) data.get("image"));
//        song.setSong_duration((int) data.get("duration"));
//        // Lấy và thêm danh sách category | Category luôn có
//        List<String> listCategoryId = (List<String>) data.get("categories");
//        song.setCategories(new ArrayList<>());
//        listCategoryId.forEach(id -> {
//            Category category = (Category) categoryRepository.findById(id).orElse(null);
//            song.getCategories().add(category);
//        });
//        // Lấy và thêm danh sách artist | Artist luôn có
//        List<String> listArtistId = (List<String>) data.get("artists");
//        song.setArtist(new ArrayList<>());
//        listArtistId.forEach(id -> {
//            Artist artist = (Artist) artistRepository.getById(id);
//            song.getArtist().add(artist);
//        });
//        // Lấy và thêm danh sách playlist | Playlist có thể trống
//        List<String> listPlaylistId = (List<String>) data.get("playlists");
//        song.setListPlaylists(new ArrayList<>());
//        listPlaylistId.forEach(id -> {
//            Playlist playlist = (Playlist) playlistRepository.getById(id);
//            song.getListPlaylists().add(playlist);
//        });
//        return song;
//    }

}