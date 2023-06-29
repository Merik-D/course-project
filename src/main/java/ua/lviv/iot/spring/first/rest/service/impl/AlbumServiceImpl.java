package ua.lviv.iot.spring.first.rest.service.impl;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import ua.lviv.iot.spring.first.rest.managers.FileManager;
import ua.lviv.iot.spring.first.rest.models.Album;
import ua.lviv.iot.spring.first.rest.models.Song;
import ua.lviv.iot.spring.first.rest.service.AlbumService;
import ua.lviv.iot.spring.first.rest.service.SongService;
import ua.lviv.iot.spring.first.rest.writer.AlbumWriter;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Configuration
public class AlbumServiceImpl implements AlbumService {
    private final Album entityInstance = new Album();

    private final AlbumWriter albumWriter;
    private final FileManager manager = new FileManager();
    private final Map<Integer, Album> albums;
    private Integer nextAvailableId;
    private final SongService songService;
    private final Map<Integer, List<Song>> albumSongs = new HashMap<>();

    public AlbumServiceImpl(final AlbumWriter albumWriter, final SongService songService) throws IOException {
        this.albumWriter = albumWriter;
        this.albums = albumWriter.read(new File(manager.getMonthDirectoryPath(entityInstance)));
        this.songService = songService;
        this.nextAvailableId = albums.keySet().stream()
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }

    @Override
    public Album create(final Album album) throws IOException {
        String fileName = manager.getFilePath(album);
        album.setId(nextAvailableId++);
        albums.put(album.getId(), album);
        albumWriter.save(album, fileName);
        return album;
    }

    @Override
    public Album getById(final Integer id) {
        return albums.get(id);
    }

    @Override
    public Collection<Album> getAll() {
        return albums.values();
    }

    @Override
    public Album delete(final Integer id) throws IOException {
        File[] files = manager.getFileFromCurrentMonth(entityInstance);
        if (files != null) {
            albumWriter.delete(id, files);
        }
        return albums.remove(id);
    }

    @Override
    public Album update(final Integer id, final Album updatedAlbum) throws IOException {
        File[] files = manager.getFileFromCurrentMonth(entityInstance);
        if (files != null) {
            updatedAlbum.setId(id);
            albums.put(id, updatedAlbum);
            albumWriter.update(id, updatedAlbum, files);
        }
        return updatedAlbum;
    }

    public List<Song> getAllSongsInAlbum(final Integer albumId) {
        Album album = albums.get(albumId);
        if (album != null) {
            List<Song> songsInAlbum = albumSongs.get(albumId);
            if (songsInAlbum == null) {
                List<Integer> songsId = album.getSongsId();
                songsInAlbum = songService.getSongsByIds(songsId);
                albumSongs.put(albumId, songsInAlbum);
            }
            return songsInAlbum;
        }
        return null;
    }

    @Override
    public Song getSongInAlbumById(final Integer albumId, final Integer songId) {
        Album album = albums.get(albumId);
        if (album != null) {
            List<Integer> songsId = album.getSongsId();
            if (songsId.contains(songId)) {
                return songService.getById(songId);
            }
        }
        return null;
    }

}
