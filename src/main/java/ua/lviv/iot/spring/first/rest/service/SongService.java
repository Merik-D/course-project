package ua.lviv.iot.spring.first.rest.service;

import ua.lviv.iot.spring.first.rest.models.Song;

public interface SongService extends TemplateService<Song, Integer> {

    String getLyricsById(Integer id);
}
