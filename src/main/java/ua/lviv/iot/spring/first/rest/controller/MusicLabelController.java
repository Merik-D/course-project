package ua.lviv.iot.spring.first.rest.controller;

import org.springframework.http.ResponseEntity;
import ua.lviv.iot.spring.first.rest.models.Artist;
import ua.lviv.iot.spring.first.rest.models.MusicLabel;

import java.io.IOException;
import java.util.List;

public interface MusicLabelController extends TemplateController<MusicLabel, Integer> {

    ResponseEntity<List<Artist>> getAllArtistsInMusicLabel(Integer id) throws IOException;

    ResponseEntity<Artist> getArtistInMusicLabelById(Integer musicLabelId, Integer artistId);
}
