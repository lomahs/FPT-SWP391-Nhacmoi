package fpt.swp391.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fpt.swp391.model.Artist;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, String> {

}
