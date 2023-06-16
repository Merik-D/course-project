package ua.lviv.iot.spring.first.rest.service.impl;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ua.lviv.iot.spring.first.rest.models.Artist;
import ua.lviv.iot.spring.first.rest.models.MusicLabel;
import ua.lviv.iot.spring.first.rest.service.ArtistService;
import ua.lviv.iot.spring.first.rest.service.MusicLabelService;
import ua.lviv.iot.spring.first.rest.writer.MusicLabelWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MusicLabelServiceImpl implements MusicLabelService {
    private final Map<Integer, MusicLabel> musicLabels;
    private int nextAvailableId = 1;
    private final MusicLabelWriter musicLabelWriter;
    private final ArtistService artistService;

    public MusicLabelServiceImpl(final MusicLabelWriter musicLabelWriter, final ArtistService artistService) throws IOException {
        this.musicLabelWriter = musicLabelWriter;
        this.musicLabels = musicLabelWriter.read();
        this.artistService = artistService;
        int maxId = musicLabels.keySet().stream()
                .max(Integer::compareTo)
                .orElse(0);
        this.nextAvailableId = maxId + 1;
    }

    @Override
    public MusicLabel create(final MusicLabel musicLabel) throws IOException {
        musicLabel.setId(nextAvailableId++);
        musicLabels.put(musicLabel.getId(), musicLabel);
        musicLabelWriter.save(musicLabel);
        return musicLabel;
    }

    public MusicLabel getById(final Integer id) {
        return musicLabels.get(id);
    }

    public Collection<MusicLabel> getAll() {
        return musicLabels.values();
    }

    public MusicLabel delete(final Integer id) throws IOException {
        musicLabelWriter.delete(id);
        return musicLabels.remove(id);
    }

    public MusicLabel update(final Integer id, final MusicLabel updatedMusicLabel) throws IOException {
        updatedMusicLabel.setId(id);
        musicLabels.put(id, updatedMusicLabel);
        musicLabelWriter.update(id, updatedMusicLabel);
        return updatedMusicLabel;
    }

    @Override
    public List<Artist> getAllArtistsInMusicLabel(final Integer musicLabelId) {
        MusicLabel musicLabel = musicLabels.get(musicLabelId);
        if (musicLabel != null) {
            List<Integer> artistsId = musicLabel.getArtistsId();
            List<Artist> artistsInMusicLabel = new ArrayList<>();

            for (Integer artistId : artistsId) {
                Artist artist = artistService.getById(artistId);
                if (artist != null) {
                    artistsInMusicLabel.add(artist);
                }
            }

            return artistsInMusicLabel;
        } else {
            return null;
        }
    }

    @Override
    public Artist getArtistInMusicLabelById(final Integer musicLabelId, final Integer artistId) {
        MusicLabel musicLabel = musicLabels.get(musicLabelId);
        if (musicLabel != null) {
            List<Integer> songsId = musicLabel.getArtistsId();
            if (songsId.contains(artistId)) {
                return artistService.getById(artistId);
            }
        }
        return null;
    }

}
