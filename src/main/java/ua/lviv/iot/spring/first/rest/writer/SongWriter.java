package ua.lviv.iot.spring.first.rest.writer;

import org.springframework.stereotype.Component;
import ua.lviv.iot.spring.first.rest.models.Song;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    protected String getHeaders(Song entity) {
        return entity.getHeaders();
    }

    @Override
    protected String convertToCSV(Song entity) {
        return entity.toCSV();
    }

    @Override
    protected Integer getIdFromEntity(Song entity) {
        return entity.getId();
    }
}
