package ua.lviv.iot.spring.first.rest.controller;


import org.springframework.http.ResponseEntity;
import ua.lviv.iot.spring.first.rest.models.Album;
import ua.lviv.iot.spring.first.rest.models.Song;

import java.io.IOException;
import java.util.List;


public interface AlbumController extends TemplateController<Album, Integer> {

    ResponseEntity<List<Song>> getAlbumSongs(Integer id) throws IOException;

    ResponseEntity<Song> getAlbumSongById(Integer albumId, Integer songId);

}
