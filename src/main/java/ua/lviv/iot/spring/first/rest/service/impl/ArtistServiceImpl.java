package ua.lviv.iot.spring.first.rest.service.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ua.lviv.iot.spring.first.rest.managers.FileManager;
import ua.lviv.iot.spring.first.rest.models.Album;
import ua.lviv.iot.spring.first.rest.models.Artist;
import ua.lviv.iot.spring.first.rest.service.AlbumService;
import ua.lviv.iot.spring.first.rest.service.ArtistService;
import ua.lviv.iot.spring.first.rest.writer.ArtistWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Scope("singleton")
public class ArtistServiceImpl implements ArtistService {

    private final Artist entityInstance = new Artist();
    private final Map<Integer, Artist> artists;
    private final ArtistWriter artistWriter;
    private final FileManager manager = new FileManager();
    private Integer nextAvailableId;
    private final AlbumService albumService;

    public ArtistServiceImpl(final ArtistWriter artistWriter, final AlbumService albumService) throws IOException {
        this.artistWriter = artistWriter;
        this.artists = artistWriter.read(new File(manager.getMonthDirectoryPath(entityInstance)));
        this.albumService = albumService;
        this.nextAvailableId = artists.keySet().stream()
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }

    @Override
    public Artist create(final Artist artist) throws IOException {
        String fileName = manager.getFilePath(artist);
        artist.setId(nextAvailableId++);
        artists.put(artist.getId(), artist);
        artistWriter.save(artist, fileName);
        return artist;
    }

    @Override
    public Artist getById(final Integer id) {
        return artists.get(id);
    }

    @Override
    public Collection<Artist> getAll() {
        return artists.values();
    }

    @Override
    public Artist delete(final Integer id) throws IOException {
        File[] files = manager.getFileFromCurrentMonth(entityInstance);
        if (files != null) {
            artistWriter.delete(id, files);
        }
        return artists.remove(id);
    }

    @Override
    public Artist update(final Integer id, final Artist updatedArtist) throws IOException {
        File[] files = manager.getFileFromCurrentMonth(entityInstance);
        if (files != null) {
            updatedArtist.setId(id);
            artists.put(id, updatedArtist);
            artistWriter.update(id, updatedArtist, files);
        }
        return updatedArtist;
    }

    @Override
    public List<Album> getAllAlbumsByArtist(final Integer artistId) {
        Artist artist = artists.get(artistId);
        if (artist != null) {
            List<Integer> albumsId = artist.getAlbumsId();
            List<Album> albumsInArtist = new ArrayList<>();

            for (Integer albumId : albumsId) {
                Album album = albumService.getById(albumId);
                if (album != null) {
                    albumsInArtist.add(album);
                }
            }

            return albumsInArtist;
        } else {
            return null;
        }
    }

    @Override
    public Album getAlbumInAristById(final Integer artistId, final Integer albumId) {
        Artist artist = artists.get(artistId);
        if (artist != null) {
            List<Integer> albumsId = artist.getAlbumsId();
            if (albumsId.contains(albumId)) {
                return albumService.getById(albumId);
            }
        }
        return null;
    }
}
