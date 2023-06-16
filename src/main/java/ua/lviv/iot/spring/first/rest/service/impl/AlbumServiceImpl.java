package ua.lviv.iot.spring.first.rest.service.impl;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ua.lviv.iot.spring.first.rest.models.Album;
import ua.lviv.iot.spring.first.rest.models.Song;
import ua.lviv.iot.spring.first.rest.service.AlbumService;
import ua.lviv.iot.spring.first.rest.service.SongService;
import ua.lviv.iot.spring.first.rest.writer.AlbumWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AlbumServiceImpl implements AlbumService {
    private final Map<Integer, Album> albums;
    private int nextAvailableId = 1;
    private final AlbumWriter albumWriter;
    private final SongService songService;

    public AlbumServiceImpl(final AlbumWriter albumWriter, final SongService songService) throws IOException {
        this.albumWriter = albumWriter;
        this.albums = albumWriter.read();
        this.songService = songService;
        int maxId = albums.keySet().stream()
                .max(Integer::compareTo)
                .orElse(0);
        this.nextAvailableId = maxId + 1;
    }

    @Override
    public Album create(final Album album) throws IOException {
        album.setId(nextAvailableId++);
        albums.put(album.getId(), album);
        albumWriter.save(album);
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
        albumWriter.delete(id);
        return albums.remove(id);
    }

    @Override
    public Album update(final Integer id, final Album updatedAlbum) throws IOException {
        updatedAlbum.setId(id);
        albums.put(id, updatedAlbum);
        albumWriter.update(id, updatedAlbum);
        return updatedAlbum;
    }

    public List<Song> getAllSongsInAlbum(final Integer albumId) {
        Album album = albums.get(albumId);
        if (album != null) {
            List<Integer> songsId = album.getSongsId();
            List<Song> songsInAlbum = new ArrayList<>();

            for (Integer songId : songsId) {
                Song song = songService.getById(songId);
                if (song != null) {
                    songsInAlbum.add(song);
                }
            }

            return songsInAlbum;
        } else {
            return null;
        }
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

//    @Override
//    public Song deleteSongFromAlbum(Integer albumId, Integer songId) throws IOException {
//        Album album = albums.get(albumId);
//        if (album != null) {
//            List<Integer> songsId = album.getSongsId();
//            if (songsId.contains(songId)) {
//                songsId.remove(songId);
//                albumWriter.update(albumId, album);
//                return songService.delete(songId);
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public Song updateSongInAlbum(Integer albumId, Integer songId, Song updatedSong) throws IOException {
//        Album album = albums.get(albumId);
//        if (album != null) {
//            List<Integer> songsId = album.getSongsId();
//            if (songsId.contains(songId)) {
//                songService.update(songId, updatedSong);
//                int songIndex = songsId.indexOf(songId);
//                songsId.set(songIndex, updatedSong.getId());
//                albumWriter.update(albumId, album);
//                return updatedSong;
//            }
//        }
//        return null;
//    }

//    @Override
//    public void addSongToAlbum(Integer albumId, Integer songId, Song newSong) throws IOException {
//        Album album = albums.get(albumId);
//
//        if (album != null) {
//            List<Integer> songsId = album.getSongsId();
//            if (!songsId.contains(songId)) {
//                Song existingSong = songService.getById(songId);
//
//                if (existingSong != null) {
//                    songsId.add(songId);
//                } else {
//                    newSong.setId(songId);
//                    songService.create(newSong);
//                    songsId.add(songId);
//                }
//
//                albumWriter.update(albumId, album);
//            }
//        }
//    }


}
