package fpt.swp391.repository;

import fpt.swp391.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, String> {

    @Query(value = "select p from Playlist as p where p.owner.account.account_name = :accountName")
    List<Playlist> getPlaylistByAccountName(String accountName);
}