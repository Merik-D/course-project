package ua.lviv.iot.spring.first.rest.service.impl;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ua.lviv.iot.spring.first.rest.models.Song;
import ua.lviv.iot.spring.first.rest.service.SongService;
import ua.lviv.iot.spring.first.rest.writer.SongWriter;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@Service
@Scope("singleton")
public class SongServiceImpl implements SongService {
    private final Map<Integer, Song> songs;
    private int nextAvailableId = 1;
    private final SongWriter songWriter;

    public SongServiceImpl(final SongWriter songWriter) throws IOException {
        this.songWriter = songWriter;
        this.songs = songWriter.read();
        int maxId = songs.keySet().stream()
                .max(Integer::compareTo)
                .orElse(0);
        this.nextAvailableId = maxId + 1;
    }

    @Override
    public Song create(final Song song) throws IOException {
        song.setId(nextAvailableId++);
        songs.put(song.getId(), song);
        songWriter.save(song);
        return song;
    }

    @Override
    public Song getById(final Integer id) {
        return songs.get(id);
    }

    @Override
    public Collection<Song> getAll() {
        return songs.values();
    }

    @Override
    public Song delete(final Integer id) throws IOException {
        songWriter.delete(id);
        return songs.remove(id);
    }

    @Override
    public Song update(final Integer id, final Song updatedSong) throws IOException {
        updatedSong.setId(id);
        songs.put(id, updatedSong);
        songWriter.update(id, updatedSong);
        return updatedSong;
    }

    @Override
    public String getLyricsById(final Integer id) {
        Song song = songs.get(id);
        if (song != null) {
            return song.getLyrics();
        } else {
            return null;
        }
    }
}
