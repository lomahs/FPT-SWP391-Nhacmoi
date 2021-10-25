package fpt.swp391.service;

import fpt.swp391.model.Artist;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IArtistService {
    Artist getArtistById(String id);

    Artist saveArtist(Artist artist);

    boolean deleteArtist(String id);

    List<Artist> getListArtists();

    Optional<Artist> getArtistByName(String name);
}