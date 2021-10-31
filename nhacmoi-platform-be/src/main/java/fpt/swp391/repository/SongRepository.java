package fpt.swp391.repository;

import fpt.swp391.model.Song;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, String> {
    @Query(value = "SELECT * FROM song WHERE song_name LIKE %?1%", nativeQuery = true)
    List<Song> findBySongName(String name);

    @Query(value = "SELECT * FROM song WHERE song_name LIKE %?1% AND song_id IN (SELECT song_id FROM playlist_song WHERE playlist_id = ?2)", nativeQuery = true)
    List<Song> findBySongNameAndPlaylistId(String name, String playlistId);
}