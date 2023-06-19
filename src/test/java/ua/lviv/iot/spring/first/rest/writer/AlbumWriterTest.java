package ua.lviv.iot.spring.first.rest.writer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.spring.first.rest.models.Album;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlbumWriterTest {

    private static final String FILE_DIRECTORY = String.join(File.separator, "src", "test",
            "resources", "albums", "actual.csv") + File.separator;
    private static final String FILE_NAME_EXPECTED_TO_SAVE = "src/test/resources/albums/album_save.csv";
    private static final String FILE_NAME_EXPECTED_TO_DELETE = "src/test/resources/albums/album_delete.csv";
    private static final String FILE_NAME_EXPECTED_TO_PUT = "src/test/resources/albums/album_put.csv";
    private static final String FILE_DIRECTORY_FOR_READ = String.join(File.separator, "src", "test",
            "resources", "albums", "read");

    AlbumWriter albumWriter;

    @BeforeEach
    void setUp() throws IOException {
        albumWriter = new AlbumWriter();
        PrintWriter writer = new PrintWriter(FILE_DIRECTORY);
        writer.println("id, title, genre, releaseYear, songsId");
        writer.println("1, Album, someGenre, 2000, [4, 5]");
        writer.close();
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(FILE_DIRECTORY));
    }

    @Test
    void testSave() throws IOException {
        List<Integer> songsId = new ArrayList<>(List.of(1, 2));
        Album album = new Album(2, "Album", "someGenre", 2002, songsId);

        albumWriter.save(album, FILE_DIRECTORY);

        Path expected = new File(FILE_NAME_EXPECTED_TO_SAVE).toPath();
        Path actual = new File(FILE_DIRECTORY).toPath();
        assertEquals(-1L, Files.mismatch(actual, expected));
    }

    @Test
    void testDelete() throws IOException {
        albumWriter.delete(1, new File[]{new File(FILE_DIRECTORY)});

        Path expected = new File(FILE_NAME_EXPECTED_TO_DELETE).toPath();
        Path actual = new File(FILE_DIRECTORY).toPath();
        assertEquals(-1L, Files.mismatch(actual, expected));
    }

    @Test
    void testUpdate() throws IOException {
        List<Integer> newSongsId = new ArrayList<>(List.of(3, 4));
        Album album2 = new Album(1, "NewAlbum", "NewSomeGenre", 2002, newSongsId);

        albumWriter.update(1, album2, new File[]{new File(FILE_DIRECTORY)});

        Path expected = new File(FILE_NAME_EXPECTED_TO_PUT).toPath();
        Path actual = new File(FILE_DIRECTORY).toPath();
        assertEquals(-1L, Files.mismatch(actual, expected));
    }

    @Test
    void testRead() throws IOException {
        List<Integer> songsId = new ArrayList<>(List.of(9, 10));
        Album expectedAlbum = new Album(1, "TitleForRead", "GenreForRead", 2021, songsId);

        Map<Integer, Album> actualAlbums = albumWriter.read(new File(FILE_DIRECTORY_FOR_READ));
        Album actualAlbum = actualAlbums.get(expectedAlbum.getId());

        assertEquals(expectedAlbum.getId(), actualAlbum.getId());
        assertEquals(expectedAlbum.getTitle(), actualAlbum.getTitle());
        assertEquals(expectedAlbum.getGenre(), actualAlbum.getGenre());
        assertEquals(expectedAlbum.getReleaseYear(), actualAlbum.getReleaseYear());
        assertEquals(expectedAlbum.getSongsId(), actualAlbum.getSongsId());
    }
}