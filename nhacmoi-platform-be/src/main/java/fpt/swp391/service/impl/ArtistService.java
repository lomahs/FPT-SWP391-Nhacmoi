package fpt.swp391.service.impl;

import fpt.swp391.model.Artist;
import fpt.swp391.repository.ArtistRepository;
import fpt.swp391.service.IArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistService implements IArtistService {

    @Autowired
    ArtistRepository artistRepository;

    @Override
    public Artist getArtistById(String id) {
        return artistRepository.findById(id).orElse(null);
    }

    @Override
    public Artist saveArtist(Artist artist) {

        return artistRepository.save(artist);
    }

    @Override
    public boolean deleteArtist(String id) {
        Artist artist = artistRepository.findById(id).orElse(null);
        if (artist != null) {
            artist.getListSong().forEach(song -> song.getArtist().remove(artist));
            artistRepository.delete(artist);
            return true;
        }
        return false;
    }

    @Override
    public List<Artist> getListArtists() {
        return artistRepository.findAll();
    }

    @Override
    public Optional<Artist> getArtistByName(String name) {

        return artistRepository.findArtistByName(name);
    }

}