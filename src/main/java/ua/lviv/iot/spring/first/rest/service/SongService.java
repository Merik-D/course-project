package ua.lviv.iot.spring.first.rest.service;

import ua.lviv.iot.spring.first.rest.models.Song;

import java.util.List;

public interface SongService extends TemplateService<Song, Integer> {

    String getLyricsById(Integer id);

    List<Song> getSongsByIds(List<Integer> songsId);
}
