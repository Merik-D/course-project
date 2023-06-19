package ua.lviv.iot.spring.first.rest.writer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.spring.first.rest.models.MusicLabel;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MusicLabelWriterTest {

    private static final String FILE_DIRECTORY = String.join(File.separator, "src", "test",
            "resources", "music-labels", "actual.csv") + File.separator;
    private static final String FILE_NAME_EXPECTED_TO_SAVE = "src/test/resources/music-labels/music-label_save.csv";
    private static final String FILE_NAME_EXPECTED_TO_DELETE = "src/test/resources/music-labels/music-label_delete.csv";
    private static final String FILE_NAME_EXPECTED_TO_PUT = "src/test/resources/music-labels/music-label_put.csv";
    private static final String FILE_DIRECTORY_FOR_READ = String.join(File.separator, "src", "test",
            "resources", "music-labels", "read");

    MusicLabelWriter musicLabelWriter;

    @BeforeEach
    void setUp() throws IOException {
        musicLabelWriter = new MusicLabelWriter();
        PrintWriter writer = new PrintWriter(FILE_DIRECTORY);
        writer.println("id, title, dateOfEstablishment, artistsId");
        writer.println("1, Title1, 2000-01-01, [15, 20]");
        writer.close();
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(FILE_DIRECTORY));
    }

    @Test
    void testSave() throws IOException {
        List<Integer> artistsId = new ArrayList<>(List.of(1, 2));
        LocalDate dateOfBirth = LocalDate.of(2006, 12, 12);
        MusicLabel musicLabel = new MusicLabel(2, "Title2", dateOfBirth, artistsId);

        musicLabelWriter.save(musicLabel, FILE_DIRECTORY);

        Path expected = new File(FILE_NAME_EXPECTED_TO_SAVE).toPath();
        Path actual = new File(FILE_DIRECTORY).toPath();
        assertEquals(-1L, Files.mismatch(actual, expected));
    }

    @Test
    void testDelete() throws IOException {
        musicLabelWriter.delete(1, new File[]{new File(FILE_DIRECTORY)});

        Path expected = new File(FILE_NAME_EXPECTED_TO_DELETE).toPath();
        Path actual = new File(FILE_DIRECTORY).toPath();
        assertEquals(-1L, Files.mismatch(actual, expected));
    }

    @Test
    void testUpdate() throws IOException {
        List<Integer> newArtistsId = new ArrayList<>(List.of(3, 4));
        LocalDate dateOfBirth = LocalDate.of(2023, 06, 10);
        MusicLabel musicLabel2 = new MusicLabel(1, "NewTitle1", dateOfBirth, newArtistsId);

        musicLabelWriter.update(1, musicLabel2, new File[]{new File(FILE_DIRECTORY)});

        Path expected = new File(FILE_NAME_EXPECTED_TO_PUT).toPath();
        Path actual = new File(FILE_DIRECTORY).toPath();
        assertEquals(-1L, Files.mismatch(actual, expected));
    }

    @Test
    void testRead() throws IOException {
        List<Integer> artistsId = new ArrayList<>(List.of(10, 15));
        LocalDate dateOfEstablishment = LocalDate.of(1999, 12, 31);
        MusicLabel expectedMusicLabel = new MusicLabel(1, "TitleForRead", dateOfEstablishment, artistsId);

        Map<Integer, MusicLabel> actualMusicLabels = musicLabelWriter.read(new File(FILE_DIRECTORY_FOR_READ));
        MusicLabel actualMusiclabel = actualMusicLabels.get(expectedMusicLabel.getId());

        assertEquals(expectedMusicLabel.getId(), actualMusiclabel.getId());
        assertEquals(expectedMusicLabel.getTitle(), actualMusiclabel.getTitle());
        assertEquals(expectedMusicLabel.getDateOfEstablishment(), actualMusiclabel.getDateOfEstablishment());
        assertEquals(expectedMusicLabel.getArtistsId(), actualMusiclabel.getArtistsId());
    }
}