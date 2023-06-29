package ua.lviv.iot.spring.first.rest.service;

import ua.lviv.iot.spring.first.rest.models.Album;
import ua.lviv.iot.spring.first.rest.models.Artist;

import java.util.List;

public interface ArtistService extends TemplateService<Artist, Integer> {

    List<Album> getAllAlbumsByArtist(Integer artistId);

    Album getAlbumInAristById(Integer artistId, Integer albumId);
}
