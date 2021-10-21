package fpt.swp391.service;

import java.util.List;
import java.util.Map;

import fpt.swp391.model.Artist;

public interface IArtistService {
    Artist getArtistById(String id);

    boolean saveArtist(Artist artist);

    boolean deleteArtist(String id);

    List<Artist> getListArtists();

    Map<String, Object> toJson(Artist artist);

    Artist toArtist(Map<String, Object> data);
}
