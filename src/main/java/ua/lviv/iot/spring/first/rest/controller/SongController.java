package ua.lviv.iot.spring.first.rest.controller;

import org.springframework.http.ResponseEntity;
import ua.lviv.iot.spring.first.rest.models.Song;

import java.util.Collection;

public interface SongController extends TemplateController<Song, Integer> {

    ResponseEntity<String> getSongLyrics(Integer id);

    ResponseEntity<Collection<String>> getAllSongLyrics();
}
