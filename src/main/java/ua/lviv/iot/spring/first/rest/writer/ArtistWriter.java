package ua.lviv.iot.spring.first.rest.writer;

import org.springframework.stereotype.Component;
import ua.lviv.iot.spring.first.rest.models.Artist;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ArtistWriter extends TemplateFileWriter<Artist, Integer> {

    @Override
    protected String getHeaders(Artist entity) {
        return entity.getHeaders();
    }

    @Override
    protected Artist extractFromCSV(final String lineFromCSV) throws NumberFormatException {
        String[] fields = lineFromCSV.split(",", 4);
        Integer id = Integer.parseInt(fields[0].trim());
        String name = fields[1].trim();
        LocalDate dateOfBirth = LocalDate.parse(fields[2].trim());

        String albumsIdsString = fields[3].replaceAll("\\[|\\]", "");
        String[] albumsIdArray = albumsIdsString.split(",");
        List<Integer> albumsId = new ArrayList<>();
        for (String artistId : albumsIdArray) {
            if (!artistId.trim().isEmpty()) {
                albumsId.add(Integer.parseInt(artistId.trim()));
            }
        }
        return new Artist(id, name, dateOfBirth, albumsId);
    }

    @Override
    protected String convertToCSV(Artist entity) {
        return entity.toCSV();
    }

    @Override
    protected Integer getIdFromEntity(Artist entity) {
        return entity.getId();
    }
}
