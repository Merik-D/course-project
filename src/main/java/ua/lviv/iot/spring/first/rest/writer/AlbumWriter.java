package ua.lviv.iot.spring.first.rest.writer;

import org.springframework.stereotype.Component;
import ua.lviv.iot.spring.first.rest.models.Album;

import java.util.List;
import java.util.ArrayList;

@Component
public class AlbumWriter extends TemplateFileWriter<Album, Integer> {

    @Override
    protected String getHeaders(final Album entity) {
        return entity.getHeaders();
    }

    @Override
    protected Album extractFromCSV(final String lineFromCSV) {
        String[] fields = lineFromCSV.split(",", 5);
        Integer id = Integer.valueOf(fields[0].trim());
        String title = fields[1].trim();
        String genre = fields[2].trim();
        Integer releaseYear = Integer.valueOf(fields[3].trim());
        String songsIdsString = fields[4].replaceAll("\\[|\\]", "");
        String[] songsIdArray = songsIdsString.split(",");
        List<Integer> songsId = new ArrayList<>();
        for (String songId : songsIdArray) {
            songsId.add(Integer.valueOf(songId.trim()));
        }

        return new Album(id, title, genre, releaseYear, songsId);
    }

    @Override
    protected String convertToCSV(final Album entity) {
        return entity.toCSV();
    }

    @Override
    protected Integer getIdFromEntity(final Album entity) {
        return entity.getId();
    }
}
