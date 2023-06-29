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
import ua.lviv.iot.spring.first.rest.controller.AlbumController;
import ua.lviv.iot.spring.first.rest.models.Album;
import ua.lviv.iot.spring.first.rest.models.Song;
import ua.lviv.iot.spring.first.rest.service.AlbumService;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumControllerImpl implements AlbumController {
    private final AlbumService albumService;

    @Autowired
    public AlbumControllerImpl(final AlbumService albumService) {
        this.albumService = albumService;
    }

    @Override
    @GetMapping
    public ResponseEntity<Collection<Album>> getAll() {
        Collection<Album> albums = albumService.getAll();
        if (albums == null || albums.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(albums);
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Album> getById(@PathVariable final Integer id) {
        Album album = albumService.getById(id);
        if (album != null) {
            return ResponseEntity.ok(album);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @PostMapping
    public ResponseEntity<Album> create(@RequestBody final Album album) throws IOException {
        Album addedAlbum = albumService.create(album);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedAlbum);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Album> update(@PathVariable final Integer id, @RequestBody final Album album) throws IOException {
        if (albumService.getById(id) != null) {
            Album updatedAlbum = albumService.update(id, album);
            return ResponseEntity.ok(updatedAlbum);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable final Integer id) throws IOException {
        if (albumService.delete(id) != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @GetMapping("/{id}/songs")
    public ResponseEntity<List<Song>> getAlbumSongs(@PathVariable final Integer id) throws IOException {
        List<Song> songs = albumService.getAllSongsInAlbum(id);
        if (songs == null || songs.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(songs);
        }
    }

    @GetMapping("/{albumId}/songs/{songId}")
    public ResponseEntity<Song> getAlbumSongById(@PathVariable final Integer albumId, @PathVariable final Integer songId) {
        Song song = albumService.getSongInAlbumById(albumId, songId);
        if (song != null) {
            return ResponseEntity.ok(song);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
