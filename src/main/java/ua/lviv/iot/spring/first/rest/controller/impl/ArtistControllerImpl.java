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
import ua.lviv.iot.spring.first.rest.controller.ArtistController;
import ua.lviv.iot.spring.first.rest.models.Album;
import ua.lviv.iot.spring.first.rest.models.Artist;
import ua.lviv.iot.spring.first.rest.service.ArtistService;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistControllerImpl implements ArtistController {
    private final ArtistService artistService;

    @Autowired
    public ArtistControllerImpl(final ArtistService artistService) {
        this.artistService = artistService;
    }

    @Override
    @GetMapping
    public ResponseEntity<Collection<Artist>> getAll() {
        Collection<Artist> artists = artistService.getAll();
        if (artists.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(artists);
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Artist> getById(@PathVariable final Integer id) {
        Artist artist = artistService.getById(id);
        if (artist != null) {
            return ResponseEntity.ok(artist);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @PostMapping
    public ResponseEntity<Artist> create(@RequestBody final Artist artist) throws IOException {
        Artist addedArtist = artistService.create(artist);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedArtist);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Artist> update(@PathVariable final Integer id, @RequestBody final Artist artist) throws IOException {
        if (artistService.getById(id) != null) {
            Artist updatedArtist = artistService.update(id, artist);
            return ResponseEntity.ok(updatedArtist);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable final Integer id) throws IOException {
        if (artistService.delete(id) != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @GetMapping("/{id}/albums")
    public ResponseEntity<List<Album>> getArtistAlbum(@PathVariable final Integer id) {
        List<Album> albums = artistService.getAllAlbumsByArtist(id);
        if (albums == null || albums.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(albums);
        }
    }

    @Override
    @GetMapping("/{artistId}/albums/{albumId}")
    public ResponseEntity<Album> getArtistAlbumById(@PathVariable final Integer artistId, @PathVariable final Integer albumId) {
        Album album = artistService.getAlbumInAristById(artistId, albumId);
        if (album != null) {
            return ResponseEntity.ok(album);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
