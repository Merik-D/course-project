package ua.lviv.iot.spring.first.rest.service.impl;

import org.springframework.stereotype.Service;
import ua.lviv.iot.spring.first.rest.managers.FileManager;
import ua.lviv.iot.spring.first.rest.models.Song;
import ua.lviv.iot.spring.first.rest.service.SongService;
import ua.lviv.iot.spring.first.rest.writer.SongWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class SongServiceImpl implements SongService {
    private final Song entityInstance = new Song();
    private final SongWriter songWriter;
    private final FileManager manager = new FileManager();
    private final Map<Integer, Song> songs;
    private Integer nextAvailableId;

    public SongServiceImpl(final SongWriter songWriter) throws IOException {
        this.songWriter = songWriter;
        this.songs = songWriter.read(new File(manager.getMonthDirectoryPath(entityInstance)));
        this.nextAvailableId = songs.keySet().stream()
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }

    @Override
    public Song create(final Song song) throws IOException {
        String fileName = manager.getFilePath(song);
        song.setId(nextAvailableId++);
        songs.put(song.getId(), song);
        songWriter.save(song, fileName);
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
        File[] files = manager.getFileFromCurrentMonth(entityInstance);
        if (files != null) {
            songWriter.delete(id, files);
        }
        return songs.remove(id);
    }

    @Override
    public Song update(final Integer id, final Song updatedSong) throws IOException {
        File[] files = manager.getFileFromCurrentMonth(entityInstance);
        if (files != null) {
            updatedSong.setId(id);
            songs.put(id, updatedSong);
            songWriter.update(id, updatedSong, files);
        }

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

    public List<Song> getSongsByIds(List<Integer> songIds) {
        List<Song> songs = new ArrayList<>();
        for (Integer songId : songIds) {
            Song song = getById(songId);
            if (song != null) {
                songs.add(song);
            }
        }
        return songs;
    }
}
