package ua.lviv.iot.spring.first.rest.service.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ua.lviv.iot.spring.first.rest.models.Album;
import ua.lviv.iot.spring.first.rest.models.Artist;
import ua.lviv.iot.spring.first.rest.service.AlbumService;
import ua.lviv.iot.spring.first.rest.service.ArtistService;
import ua.lviv.iot.spring.first.rest.writer.ArtistWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Scope("singleton")
public class ArtistServiceImpl implements ArtistService {

    private final Map<Integer, Artist> artists;
    private int nextAvailableId = 1;

    private final ArtistWriter artistWriter;
    private final AlbumService albumService;

    public ArtistServiceImpl(final ArtistWriter artistWriter, final AlbumService albumService) throws IOException {
        this.artistWriter = artistWriter;
        this.artists = artistWriter.read();
        this.albumService = albumService;
        int maxId = artists.keySet().stream()
                .max(Integer::compareTo)
                .orElse(0);
        this.nextAvailableId = maxId + 1;
    }

    @Override
    public Artist create(final Artist artist) throws IOException {
        artist.setId((Integer) nextAvailableId++);
        artists.put(artist.getId(), artist);
        artistWriter.save(artist);
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
        artistWriter.delete(id);
        return artists.remove(id);
    }

    @Override
    public Artist update(final Integer id, final Artist updatedArtist) throws IOException {
        updatedArtist.setId(id);
        artists.put(id, updatedArtist);
        artistWriter.update(id, updatedArtist);
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
