package ua.lviv.iot.spring.first.rest.writer;

import org.springframework.stereotype.Component;
import ua.lviv.iot.spring.first.rest.models.Song;

@Component
public class SongWriter extends TemplateFileWriter<Song, Integer> {

    @Override
    protected Song extractFromCSV(final String lineFromCSV) {
        String[] fields = lineFromCSV.split(",", 5);
        Integer id = Integer.valueOf(fields[0].trim());
        String title = fields[1].trim();
        String duration = fields[2].trim();
        int releaseYear = Integer.parseInt(fields[3].trim());
        String lyrics = fields[4].trim();
        return new Song(id, title, duration, releaseYear, lyrics);
    }

    @Override
    protected String getHeaders(final Song entity) {
        return entity.getHeaders();
    }

    @Override
    protected String convertToCSV(final Song entity) {
        return entity.toCSV();
    }

    @Override
    protected Integer getIdFromEntity(final Song entity) {
        return entity.getId();
    }
}
