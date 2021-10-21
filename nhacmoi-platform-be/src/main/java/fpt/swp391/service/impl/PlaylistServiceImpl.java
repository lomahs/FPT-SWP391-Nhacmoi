package fpt.swp391.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fpt.swp391.model.Playlist;
import fpt.swp391.model.Song;
import fpt.swp391.model.User;
import fpt.swp391.repository.PlaylistRepository;
import fpt.swp391.repository.SongRepository;
import fpt.swp391.repository.UserRepository;
import fpt.swp391.service.IPlaylistService;
import fpt.swp391.service.ISongService;

@Service
public class PlaylistServiceImpl implements IPlaylistService {

    @Autowired
    PlaylistRepository playlistRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SongRepository songRepository;

    @Autowired
    ISongService iSongService;

    @Override
    public Playlist getPlaylistById(String id) {
        return playlistRepository.findById(id).orElse(null);
    }

    @Override
    public boolean savePlaylist(Playlist playlist) {
        if (playlist == null)
            return false;
        playlistRepository.save(playlist);
        return true;
    }

    @Override
    public boolean deletePlaylist(String id) {
        Playlist playlist = getPlaylistById(id);
        if (playlist != null) {
            playlist.getListSongs().forEach(song -> song.getListPlaylists().remove(playlist));
            playlistRepository.save(playlist);
            playlistRepository.delete(playlist);
            return true;
        }
        return false;
    }

    @Override
    public List<Playlist> getListPlaylists() {
        return playlistRepository.findAll();
    }

    private List<Map<String, Object>> filterObjectData(String entityName, Playlist playlist) {
        List<Map<String, Object>> data = new ArrayList<>();
        switch (entityName) {
        case "user":
            User user = playlist.getUser_created_id();
            Map<String, Object> u = new LinkedHashMap<>();
            u.put("user_id", user.getUser_id());
            u.put("user_name", user.getUser_name());
            u.put("account_name", user.getAccount().getAccount_name());
            data.add(u);
            break;
        case "song":
            playlist.getListSongs().forEach(song -> {
                Map<String, Object> s = iSongService.toJson(song);
                s.remove("playlists");
                s.remove("user_added");
                data.add(s);
            });
            break;
        }
        return data;
    }

    public int calculateDuration(Playlist playlist) {
        int duration = 0;
        for (Song song : playlist.getListSongs()) {
            duration += song.getSong_duration();
        }
        return duration;
    }

    @Override
    public Map<String, Object> toJson(Playlist playlist) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("playlist_id", playlist.getPlaylist_id());
        data.put("user_created", filterObjectData("user", playlist).get(0));
        data.put("playlist_name", playlist.getPlaylist_name());
        data.put("playlist_duration", playlist.getPlaylist_duration());
        data.put("playlist_image", playlist.getPlaylist_image());
        data.put("songs", filterObjectData("song", playlist));
        return data;
    }

    @Override
    public Playlist toPlaylist(Map<String, Object> data) {
        Playlist playlist = new Playlist();
        playlist.setPlaylist_name((String) data.get("playlist_name"));
        playlist.setPlaylist_image((String) data.get("playlist_image"));
        User user_created = userRepository.getById((String) data.get("user_created"));
        playlist.setUser_created_id(user_created);

        // Lấy và thêm danh sách các bài hát
        List<String> listSongId = (List<String>) data.get("songs");
        playlist.setListSongs(new ArrayList<>());
        listSongId.forEach(id -> {
            Song song = (Song) songRepository.findById(id).orElse(null);
            playlist.getListSongs().add(song);
        });
        return playlist;
    }

}
