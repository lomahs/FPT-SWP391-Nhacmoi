package fpt.swp391.repository;

import fpt.swp391.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, String> {

    @Query(value = "select artist from Artist as artist where artist.artist_name = :name")
    Optional<Artist> findArtistByName(String name);
}