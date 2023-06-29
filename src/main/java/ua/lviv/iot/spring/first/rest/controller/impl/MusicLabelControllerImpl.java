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
import ua.lviv.iot.spring.first.rest.controller.MusicLabelController;
import ua.lviv.iot.spring.first.rest.models.Artist;
import ua.lviv.iot.spring.first.rest.models.MusicLabel;
import ua.lviv.iot.spring.first.rest.service.MusicLabelService;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/musicLabels")
public class MusicLabelControllerImpl implements MusicLabelController {

    private final MusicLabelService musicLabelService;

    @Autowired
    public MusicLabelControllerImpl(final MusicLabelService musicLabelService) {
        this.musicLabelService = musicLabelService;
    }

    @Override
    @GetMapping
    public ResponseEntity<Collection<MusicLabel>> getAll() {
        Collection<MusicLabel> albums = musicLabelService.getAll();
        if (albums == null || albums.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(albums);
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<MusicLabel> getById(@PathVariable final Integer id) {
        MusicLabel album = musicLabelService.getById(id);
        if (album != null) {
            return ResponseEntity.ok(album);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @PostMapping
    public ResponseEntity<MusicLabel> create(@RequestBody final MusicLabel musicLabel) throws IOException {
        MusicLabel addedAlbum = musicLabelService.create(musicLabel);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedAlbum);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<MusicLabel> update(@PathVariable final Integer id, @RequestBody final MusicLabel musicLabel) throws IOException {
        if (musicLabelService.getById(id) != null) {
            MusicLabel updatedAlbum = musicLabelService.update(id, musicLabel);
            return ResponseEntity.ok(updatedAlbum);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable final Integer id) throws IOException {
        if (musicLabelService.delete(id) != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @GetMapping("/{id}/artists")
    public ResponseEntity<List<Artist>> getAllArtistsInMusicLabel(@PathVariable final Integer id) throws IOException {
        List<Artist> artists = musicLabelService.getAllArtistsInMusicLabel(id);
        if (artists == null || artists.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(artists);
        }
    }


    @GetMapping("/{musicLabelId}/artists/{artistId}")
    public ResponseEntity<Artist> getArtistInMusicLabelById(@PathVariable final Integer musicLabelId, @PathVariable final Integer artistId) {
        Artist artist = musicLabelService.getArtistInMusicLabelById(musicLabelId, artistId);
        if (artist != null) {
            return ResponseEntity.ok(artist);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
