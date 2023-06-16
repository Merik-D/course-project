package ua.lviv.iot.spring.first.rest.writer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.spring.first.rest.models.Album;
import ua.lviv.iot.spring.first.rest.models.Song;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlbumWriterTest {

    private static final String FILE_DIRECTORY = "src/test/resources";
    private static final String FILE_NAME_ACTUAL = "actual.csv";
    private static final String FILE_NAME_EXPECTED_TO_SAVE = "src/test/resources/albums/album_save.csv";
    private static final String FILE_NAME_EXPECTED_TO_DELETE = "src/test/resources/albums/album_delete.csv";

    private static final String FILE_NAME_EXPECTED_TO_PUT = "src/test/resources/albums/album_put.csv";

    AlbumWriter albumWriter;
    Album album;

    List<Integer> songsId = new ArrayList<>(List.of(9, 10));

    @BeforeEach
    void setUp() throws IOException {
        album = new Album(1, "Title1", "rap", 2021, songsId);
        List<File> files = Collections.singletonList(new File(FILE_NAME_ACTUAL));
        albumWriter = new AlbumWriter(FILE_NAME_ACTUAL, files);
        albumWriter.setFiles(files);
        albumWriter.setFileDirectory(FILE_DIRECTORY);
        PrintWriter writer = new PrintWriter(FILE_NAME_ACTUAL);
        writer.println("id, title, genre, releaseYear, songsId");
        writer.println(album.toCSV());
        writer.close();
    }

    @AfterEach
    void tearDown() throws IOException {
        String filePath = FILE_DIRECTORY + "/" + FILE_NAME_ACTUAL;
        Files.deleteIfExists(Path.of(filePath));
    }

    @Test
    void testSave() throws IOException {
        List<Integer> songsId = new ArrayList<>(List.of(1, 2));
        Album album = new Album(2, "Album2", "someGenre2", 2002, songsId);

        albumWriter.save(album);

        Path expected = new File(FILE_NAME_EXPECTED_TO_SAVE).toPath();
        Path actual = new File(FILE_NAME_ACTUAL).toPath();
        assertEquals(-1L, Files.mismatch(actual, expected));
    }

    @Test
    void testDelete() throws IOException {
        albumWriter.delete(1);

        Path expected = new File(FILE_NAME_EXPECTED_TO_DELETE).toPath();
        Path actual = new File(FILE_NAME_ACTUAL).toPath();
        assertEquals(-1L, Files.mismatch(actual, expected));
    }

    @Test
    void testUpdate() throws IOException {
        List<Integer> newSongsId = new ArrayList<>(List.of(3, 4));
        Album album2 = new Album(1, "NewAlbum", "NewSomeGenre", 2002, newSongsId);

        albumWriter.update(1, album2);

        Path expected = new File(FILE_NAME_EXPECTED_TO_PUT).toPath();
        Path actual = new File(FILE_NAME_ACTUAL).toPath();
        assertEquals(-1L, Files.mismatch(actual, expected));
    }

    @Test
    void testRead() throws IOException {
        List<Integer> songsId = new ArrayList<>(List.of(9));
        Album expectedAlbum = new Album(1, "Title1", "rap", 2021, songsId);

        Map<Integer, Album> actualAlbums = albumWriter.read();
        Album actualAlbum = actualAlbums.get(expectedAlbum.getId());

        assertEquals(expectedAlbum.getId(), actualAlbum.getId());
        assertEquals(expectedAlbum.getTitle(), actualAlbum.getTitle());
        assertEquals(expectedAlbum.getGenre(), actualAlbum.getGenre());
        assertEquals(expectedAlbum.getReleaseYear(), actualAlbum.getReleaseYear());
        assertEquals(expectedAlbum.getSongsId(), actualAlbum.getSongsId());
    }
}