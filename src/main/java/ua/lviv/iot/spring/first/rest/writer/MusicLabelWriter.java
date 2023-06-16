package ua.lviv.iot.spring.first.rest.writer;

import lombok.Setter;
import org.springframework.stereotype.Component;
import ua.lviv.iot.spring.first.rest.models.MusicLabel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Component
public class MusicLabelWriter extends CustomFileWriter<MusicLabel> {

    private static final String FILE_PREFIX = "music-label-";
    private String fileDirectory = "src/main/resources/music-label";
    private List<File> files = findInputFiles(FILE_PREFIX, fileDirectory);

    public MusicLabelWriter() {
        super(FILE_PREFIX);
        this.files = files;
        createDirectoryIfNotExists(fileDirectory);
        this.setFileName(generateFileName());
    }

    public MusicLabelWriter(String fileName, List<File> files) {
        super("");
        this.setFileName(fileName);
        this.files = files;
    }

    @Override
    public Map<Integer, MusicLabel> read() throws IOException {
        Map<Integer, MusicLabel> musicLabels = new HashMap<>();
        List<File> matchingFiles = findInputFiles(FILE_PREFIX, fileDirectory);

        for (File file : matchingFiles) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), StandardCharsets.UTF_8))) {
                reader.readLine();

                String line;
                while ((line = reader.readLine()) != null) {
                    MusicLabel musicLabel = extractFromCSV(line);
                    musicLabels.put(musicLabel.getId(), musicLabel);
                }
            }

        }

        return musicLabels;
    }

    @Override
    public void save(final MusicLabel musicLabel) throws IOException {
        String filePath = fileDirectory + "/" + getFileName();
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(filePath, true), StandardCharsets.UTF_8))) {
            File file = new File(filePath);
            boolean fileExists = file.exists();

            if (!fileExists || file.length() == 0) {
                writer.println(musicLabel.getHeaders());
            }

            writer.println(musicLabel.toCSV());
            System.out.println("Music label saved successfully.");
        }
    }

    @Override
    public void delete(final Integer id) throws IOException {
        for (File file : files) {
            List<String> lines = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), StandardCharsets.UTF_8))) {
                String headerLine = reader.readLine();
                lines.add(headerLine);

                String line;
                while ((line = reader.readLine()) != null) {
                    MusicLabel currentMusicLabel = extractFromCSV(line);
                    if (currentMusicLabel.getId().equals(id)) {
                        continue;
                    }
                    lines.add(line);
                }
            }

            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(file), StandardCharsets.UTF_8))) {
                for (String updatedLine : lines) {
                    writer.println(updatedLine);
                }
            }
        }
    }

    @Override
    public void update(final Integer id, final MusicLabel updatedMusicLabel) throws IOException {
        boolean updated = false;

        for (File file : files) {
            List<String> updatedLines = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), StandardCharsets.UTF_8))) {
                String headerLine = reader.readLine();
                updatedLines.add(headerLine);

                String line;
                while ((line = reader.readLine()) != null) {
                    MusicLabel currentMusicLabel = extractFromCSV(line);
                    if (currentMusicLabel.getId().equals(id)) {
                        updatedLines.add(updatedMusicLabel.toCSV());
                        updated = true;
                    } else {
                        updatedLines.add(line);
                    }
                }
            }

            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(file), StandardCharsets.UTF_8))) {
                for (String updatedLine : updatedLines) {
                    writer.println(updatedLine);
                }
            }

            if (updated) {
                System.out.println("Song updated successfully in file: " + file.getName());
                break;
            }
        }
    }

    private static MusicLabel extractFromCSV(final String lineFromCSV) throws NumberFormatException {
        String[] fields = lineFromCSV.split(",", -1);
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
}
