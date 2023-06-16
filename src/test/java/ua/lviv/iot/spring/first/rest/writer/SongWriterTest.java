package ua.lviv.iot.spring.first.rest.writer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.spring.first.rest.models.Song;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SongWriterTest {

    private static final String FILE_DIRECTORY = "src/test/resources";
    private static final String FILE_NAME_ACTUAL = "actual.csv";
    private static final String FILE_NAME_EXPECTED_TO_SAVE = "src/test/resources/songs/song_save.csv";
    private static final String FILE_NAME_EXPECTED_TO_DELETE = "src/test/resources/songs/song_delete.csv";

    private static final String FILE_NAME_EXPECTED_TO_PUT = "src/test/resources/songs/song_put.csv";

    SongWriter songWriter;
    Song song;

    @BeforeEach
    void setUp() throws IOException {
        song = new Song(1, "Title1", "03:45", 2021, "Lyrics1");
        List<File> files = Collections.singletonList(new File(FILE_NAME_ACTUAL));
        songWriter = new SongWriter(FILE_NAME_ACTUAL, files);
        songWriter.setFiles(files);
        songWriter.setFileDirectory(FILE_DIRECTORY);
        PrintWriter writer = new PrintWriter(FILE_NAME_ACTUAL);
        writer.println("id, title, duration, releaseYear, lyrics");
        writer.println("1, Title1, 03:45, 2021, Lyrics1");
        writer.close();
    }

    @AfterEach
    void tearDown() throws IOException {
        String filePath = FILE_DIRECTORY + "/" + FILE_NAME_ACTUAL;
        Files.deleteIfExists(Path.of(filePath));
    }

    @Test
    void testSave() throws IOException {
        Song song = new Song(2, "Title2", "03:45", 2002, "Lyrics2");

        songWriter.save(song);

        Path expected = new File(FILE_NAME_EXPECTED_TO_SAVE).toPath();
        Path actual = new File(FILE_NAME_ACTUAL).toPath();
        assertEquals(-1L, Files.mismatch(actual, expected));
    }

    @Test
    void testDelete() throws IOException {
        songWriter.delete(1);

        Path expected = new File(FILE_NAME_EXPECTED_TO_DELETE).toPath();
        Path actual = new File(FILE_NAME_ACTUAL).toPath();
        assertEquals(-1L, Files.mismatch(actual, expected));
    }

    @Test
    void testUpdate() throws IOException {
        Song song2 = new Song(1, "NewTitle1", "03:45", 2021, "NewLyrics1");

        songWriter.update(1, song2);

        Path expected = new File(FILE_NAME_EXPECTED_TO_PUT).toPath();
        Path actual = new File(FILE_NAME_ACTUAL).toPath();
        assertEquals(-1L, Files.mismatch(actual, expected));
    }

    @Test
    void testRead() throws IOException {
        Song expectedSong = new Song(1, "Title1", "03:45", 2021, "Lyrics1");

        Map<Integer, Song> actualSongs = songWriter.read();
        Song actualSong = actualSongs.get(expectedSong.getId());

        assertEquals(expectedSong.getId(), actualSong.getId());
        assertEquals(expectedSong.getTitle(), actualSong.getTitle());
        assertEquals(expectedSong.getDuration(), actualSong.getDuration());
        assertEquals(expectedSong.getReleaseYear(), actualSong.getReleaseYear());
        assertEquals(expectedSong.getLyrics(), actualSong.getLyrics());
    }
}