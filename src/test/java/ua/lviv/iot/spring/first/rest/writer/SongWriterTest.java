package ua.lviv.iot.spring.first.rest.writer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.spring.first.rest.models.Song;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SongWriterTest {

    private static final String FILE_DIRECTORY = String.join(File.separator, "src", "test",
            "resources", "songs", "actual.csv") + File.separator;
    private static final String FILE_NAME_EXPECTED_TO_SAVE = "src/test/resources/songs/song_save.csv";
    private static final String FILE_NAME_EXPECTED_TO_DELETE = "src/test/resources/songs/song_delete.csv";
    private static final String FILE_NAME_EXPECTED_TO_PUT = "src/test/resources/songs/song_put.csv";
    private static final String FILE_DIRECTORY_FOR_READ = String.join(File.separator, "src", "test",
            "resources", "songs", "read");

    SongWriter songWriter;

    @BeforeEach
    void setUp() throws IOException {
        songWriter = new SongWriter();
        PrintWriter writer = new PrintWriter(FILE_DIRECTORY);
        writer.println("id, title, duration, releaseYear, lyrics");
        writer.println("1, Title1, 03:45, 2021, Lyrics1");
        writer.close();
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(FILE_DIRECTORY));
    }

    @Test
    void testSave() throws IOException {
        Song song = new Song(2, "Title2", "03:45", 2002, "Lyrics2");

        songWriter.save(song, FILE_DIRECTORY);

        Path expected = new File(FILE_NAME_EXPECTED_TO_SAVE).toPath();
        Path actual = new File(FILE_DIRECTORY).toPath();
        assertEquals(-1L, Files.mismatch(actual, expected));
    }

    @Test
    void testDelete() throws IOException {
        songWriter.delete(1, new File[]{new File(FILE_DIRECTORY)});

        Path expected = new File(FILE_NAME_EXPECTED_TO_DELETE).toPath();
        Path actual = new File(FILE_DIRECTORY).toPath();
        assertEquals(-1L, Files.mismatch(actual, expected));
    }

    @Test
    void testUpdate() throws IOException {
        Song song2 = new Song(1, "NewTitle1", "03:45", 2021, "NewLyrics1");

        songWriter.update(1, song2, new File[]{new File(FILE_DIRECTORY)});

        Path expected = new File(FILE_NAME_EXPECTED_TO_PUT).toPath();
        Path actual = new File(FILE_DIRECTORY).toPath();
        assertEquals(-1L, Files.mismatch(actual, expected));
    }

    @Test
    void testRead() throws IOException {
        Map<Integer, Song> actualSongs = songWriter.read(new File(FILE_DIRECTORY_FOR_READ));

        assertEquals(1, actualSongs.size());
        Song expectedSong = new Song(1, "TitleForRead", "03:45", 2021, "LyricsForRead");
        Song actualSong = actualSongs.get(expectedSong.getId());

        assertNotNull(actualSong, "The actual song is null");

        assertEquals(expectedSong.getId(), actualSong.getId());
        assertEquals(expectedSong.getTitle(), actualSong.getTitle());
        assertEquals(expectedSong.getDuration(), actualSong.getDuration());
        assertEquals(expectedSong.getReleaseYear(), actualSong.getReleaseYear());
        assertEquals(expectedSong.getLyrics(), actualSong.getLyrics());
    }
}