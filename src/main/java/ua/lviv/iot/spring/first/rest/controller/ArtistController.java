package ua.lviv.iot.spring.first.rest.controller;

import org.springframework.http.ResponseEntity;
import ua.lviv.iot.spring.first.rest.models.Album;
import ua.lviv.iot.spring.first.rest.models.Artist;

import java.io.IOException;
import java.util.List;

public interface ArtistController extends TemplateController<Artist, Integer> {

    ResponseEntity<List<Album>> getArtistAlbum(Integer id) throws IOException;

    ResponseEntity<Album> getArtistAlbumById(Integer artistId, Integer albumId);

}
