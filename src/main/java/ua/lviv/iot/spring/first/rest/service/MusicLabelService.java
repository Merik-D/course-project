package ua.lviv.iot.spring.first.rest.service;


import ua.lviv.iot.spring.first.rest.models.Artist;
import ua.lviv.iot.spring.first.rest.models.MusicLabel;

import java.util.List;

public interface MusicLabelService extends TemplateService<MusicLabel, Integer> {

    List<Artist> getAllArtistsInMusicLabel(Integer musicLabelId);
    Artist getArtistInMusicLabelById(Integer musicLabelId, Integer artistId);
}
