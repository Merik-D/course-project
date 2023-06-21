package ua.lviv.iot.spring.first.rest.service.impl;

import org.springframework.stereotype.Service;
import ua.lviv.iot.spring.first.rest.managers.FileManager;
import ua.lviv.iot.spring.first.rest.models.Artist;
import ua.lviv.iot.spring.first.rest.models.MusicLabel;
import ua.lviv.iot.spring.first.rest.service.ArtistService;
import ua.lviv.iot.spring.first.rest.service.MusicLabelService;
import ua.lviv.iot.spring.first.rest.writer.MusicLabelWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class MusicLabelServiceImpl implements MusicLabelService {
    private final MusicLabel entityInstance = new MusicLabel();
    private final Map<Integer, MusicLabel> musicLabels;
    private final FileManager manager = new FileManager();
    private Integer nextAvailableId;
    private final MusicLabelWriter musicLabelWriter;
    private final ArtistService artistService;

    public MusicLabelServiceImpl(final MusicLabelWriter musicLabelWriter, final ArtistService artistService) throws IOException {
        this.musicLabelWriter = musicLabelWriter;
        this.musicLabels = musicLabelWriter.read(new File(manager.getMonthDirectoryPath(entityInstance)));
        this.artistService = artistService;
        this.nextAvailableId = musicLabels.keySet().stream()
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }

    @Override
    public MusicLabel create(final MusicLabel musicLabel) throws IOException {
        String fileName = manager.getFilePath(musicLabel);
        musicLabel.setId(nextAvailableId++);
        musicLabels.put(musicLabel.getId(), musicLabel);
        musicLabelWriter.save(musicLabel, fileName);
        return musicLabel;
    }

    public MusicLabel getById(final Integer id) {
        return musicLabels.get(id);
    }

    public Collection<MusicLabel> getAll() {
        return musicLabels.values();
    }

    public MusicLabel delete(final Integer id) throws IOException {
        File[] files = manager.getFileFromCurrentMonth(entityInstance);
        if (files != null) {
            musicLabelWriter.delete(id, files);
        }
        return musicLabels.remove(id);
    }

    public MusicLabel update(final Integer id, final MusicLabel updatedMusicLabel) throws IOException {
        File[] files = manager.getFileFromCurrentMonth(entityInstance);
        if (files != null) {
            updatedMusicLabel.setId(id);
            musicLabels.put(id, updatedMusicLabel);
            musicLabelWriter.update(id, updatedMusicLabel, files);
        }
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
