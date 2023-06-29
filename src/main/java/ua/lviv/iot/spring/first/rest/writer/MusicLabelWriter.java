package ua.lviv.iot.spring.first.rest.writer;

import org.springframework.stereotype.Component;
import ua.lviv.iot.spring.first.rest.models.MusicLabel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class MusicLabelWriter extends TemplateFileWriter<MusicLabel, Integer> {

    @Override
    protected String getHeaders(final MusicLabel entity) {
        return entity.getHeaders();
    }

    @Override
    protected MusicLabel extractFromCSV(final String lineFromCSV) throws NumberFormatException {
        String[] fields = lineFromCSV.split(",", 4);
        Integer id = Integer.parseInt(fields[0].trim());
        String title = fields[1].trim();
        LocalDate dateOfEstablishment = LocalDate.parse(fields[2].trim());

        String artistsIdsString = fields[3].replaceAll("\\[|\\]", "");
        String[] artistsIdArray = artistsIdsString.split(",");
        List<Integer> artistsId = new ArrayList<>();
        for (String artistId : artistsIdArray) {
            artistsId.add(Integer.parseInt(artistId.trim()));
        }

        return new MusicLabel(id, title, dateOfEstablishment, artistsId);
    }

    @Override
    protected String convertToCSV(final MusicLabel entity) {
        return entity.toCSV();
    }

    @Override
    protected Integer getIdFromEntity(final MusicLabel entity) {
        return entity.getId();
    }
}
