package ua.lviv.iot.spring.first.rest.writer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.spring.first.rest.models.Artist;

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

class ArtistWriterTest {

    private static final String FILE_DIRECTORY = String.join(File.separator, "src", "test",
            "resources", "artists", "actual.csv") + File.separator;
    private static final String FILE_NAME_EXPECTED_TO_SAVE = "src/test/resources/artists/artist_save.csv";
    private static final String FILE_NAME_EXPECTED_TO_DELETE = "src/test/resources/artists/artist_delete.csv";
    private static final String FILE_NAME_EXPECTED_TO_PUT = "src/test/resources/artists/artist_put.csv";
    private static final String FILE_DIRECTORY_FOR_READ = String.join(File.separator, "src", "test",
            "resources", "artists", "read");

    ArtistWriter artistWriter;

    @BeforeEach
    void setUp() throws IOException {
        artistWriter = new ArtistWriter();
        PrintWriter writer = new PrintWriter(FILE_DIRECTORY);
        writer.println("id, name, dateOfBirth, albumsId");
        writer.println("1, Name1, 2005-04-15, [3, 7]");
        writer.close();
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(FILE_DIRECTORY));
    }

    @Test
    void testSave() throws IOException {
        List<Integer> albumsId = new ArrayList<>(List.of(1, 2));
        LocalDate dateOfBirth = LocalDate.of(2006, 12, 12);
        Artist artist = new Artist(2, "Name2", dateOfBirth, albumsId);

        artistWriter.save(artist, FILE_DIRECTORY);

        Path expected = new File(FILE_NAME_EXPECTED_TO_SAVE).toPath();
        Path actual = new File(FILE_DIRECTORY).toPath();
        assertEquals(-1L, Files.mismatch(actual, expected));
    }

    @Test
    void testDelete() throws IOException {
        artistWriter.delete(1, new File[]{new File(FILE_DIRECTORY)});

        Path expected = new File(FILE_NAME_EXPECTED_TO_DELETE).toPath();
        Path actual = new File(FILE_DIRECTORY).toPath();
        assertEquals(-1L, Files.mismatch(actual, expected));
    }

    @Test
    void testUpdate() throws IOException {
        List<Integer> newAlbumsId = new ArrayList<>(List.of(3, 4));
        LocalDate dateOfBirth = LocalDate.of(2023, 06, 10);
        Artist artist2 = new Artist(1, "NewName1", dateOfBirth, newAlbumsId);

        artistWriter.update(1, artist2, new File[]{new File(FILE_DIRECTORY)});

        Path expected = new File(FILE_NAME_EXPECTED_TO_PUT).toPath();
        Path actual = new File(FILE_DIRECTORY).toPath();
        assertEquals(-1L, Files.mismatch(actual, expected));
    }

    @Test
    void testRead() throws IOException {
        List<Integer> albumsId = new ArrayList<>(List.of(10, 15));
        LocalDate dateOfBirth = LocalDate.of(1999, 12, 31);
        Artist expectedArtist = new Artist(1, "NameForRead", dateOfBirth, albumsId);

        Map<Integer, Artist> actualArtists = artistWriter.read(new File(FILE_DIRECTORY_FOR_READ));
        Artist actualArtist = actualArtists.get(expectedArtist.getId());

        assertEquals(expectedArtist.getId(), actualArtist.getId());
        assertEquals(expectedArtist.getName(), actualArtist.getName());
        assertEquals(expectedArtist.getDateOfBirth(), actualArtist.getDateOfBirth());
        assertEquals(expectedArtist.getAlbumsId(), actualArtist.getAlbumsId());
    }
}