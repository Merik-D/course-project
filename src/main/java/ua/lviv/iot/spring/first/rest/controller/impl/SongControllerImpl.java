package ua.lviv.iot.spring.first.rest.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ua.lviv.iot.spring.first.rest.controller.SongController;
import ua.lviv.iot.spring.first.rest.models.Song;
import ua.lviv.iot.spring.first.rest.service.SongService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/songs")
public class SongControllerImpl implements SongController {

    private final SongService songService;

    @Autowired
    public SongControllerImpl(final SongService songService) {
        this.songService = songService;
    }

    @Override
    @GetMapping
    public ResponseEntity<Collection<Song>> getAll() {
        Collection<Song> songs = songService.getAll();
        if (songs.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(songs);
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Song> getById(@PathVariable final Integer id) {
        Song song = songService.getById(id);
        if (song != null) {
            return ResponseEntity.ok(song);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @PostMapping
    public ResponseEntity<Song> create(@RequestBody final Song song) throws IOException {
        Song addedSong = songService.create(song);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedSong);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Song> update(@PathVariable final Integer id, @RequestBody final Song song) throws IOException {
        if (songService.getById(id) != null) {
            Song updatedSong = songService.update(id, song);
            return ResponseEntity.ok(updatedSong);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable final Integer id) throws IOException {
        if (songService.delete(id) != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/lyrics")
    public ResponseEntity<String> getSongLyrics(@PathVariable final Integer id) {
        String lyrics = songService.getLyricsById(id);
        if (lyrics != null) {
            return ResponseEntity.ok(lyrics);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("lyrics")
    public ResponseEntity<Collection<String>> getAllSongLyrics() {
        Collection<Song> songs = songService.getAll();
        Collection<String> allLyrics = new ArrayList<>();
        for (Song song : songs) {
            String lyrics = song.getLyrics();
            allLyrics.add(lyrics);
        }
        if (!allLyrics.isEmpty()) {
            return ResponseEntity.ok(allLyrics);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
