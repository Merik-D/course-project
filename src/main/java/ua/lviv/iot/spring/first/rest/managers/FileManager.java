package ua.lviv.iot.spring.first.rest.managers;

import org.springframework.stereotype.Component;
import ua.lviv.iot.spring.first.rest.models.Album;
import ua.lviv.iot.spring.first.rest.models.Artist;
import ua.lviv.iot.spring.first.rest.models.MusicLabel;
import ua.lviv.iot.spring.first.rest.models.Song;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

@Component
public class FileManager {
    public static final String PATH_TO_SONGS = String.join(File.separator, "src", "main",
            "resources", "songs") + File.separator;
    public static final String PATH_TO_ALBUMS = String.join(File.separator, "src", "main",
            "resources", "albums") + File.separator;
    public static final String PATH_TO_ARTISTS = String.join(File.separator, "src", "main",
            "resources", "artists") + File.separator;
    public static final String PATH_TO_MUSIC_LABELS = String.join(File.separator, "src", "main",
            "resources", "musiclabels") + File.separator;

    public String getMonthDirectoryPath(final Object entity) {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;

        return getYearDirectoryPath(entity) + month;
    }

    public File[] getFileFromCurrentMonth(final Object entity) {
        File monthDirectory = new File(getMonthDirectoryPath(entity));

        if (monthDirectory.exists() && monthDirectory.isDirectory()) {
            if (monthDirectory.listFiles() != null) {
                return monthDirectory.listFiles();
            }
        }
        return null;
    }

    public String getFilePath(final Object entity) {
        String monthDirectoryPath = getMonthDirectoryPath(entity) + File.separator;

        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return String.format("%s%s-%s.csv", monthDirectoryPath, entity.getClass().getSimpleName().toLowerCase(), formattedDate);
    }

    private String getYearDirectoryPath(final Object entity) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        if (entity instanceof Song) {
            return FileManager.PATH_TO_SONGS + year + File.separator;
        } else if (entity instanceof Album) {
            return FileManager.PATH_TO_ALBUMS + year + File.separator;
        } else if (entity instanceof Artist) {
            return FileManager.PATH_TO_ARTISTS + year + File.separator;
        } else if (entity instanceof MusicLabel) {
            return FileManager.PATH_TO_MUSIC_LABELS + year + File.separator;
        } else {
            return null;
        }
    }
}
