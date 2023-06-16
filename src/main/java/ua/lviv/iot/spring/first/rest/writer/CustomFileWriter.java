package ua.lviv.iot.spring.first.rest.writer;
//src.test.res
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class CustomFileWriter<T> {

    protected static final String FILE_EXTENSION = ".csv";
    private String filePrefix;
    private String fileDirectory;
    private final DateTimeFormatter formatter;
    private String fileName;

    public CustomFileWriter(final String filePrefix) {
        this.filePrefix = filePrefix;
        this.formatter = DateTimeFormatter.ofPattern(
                "'" + filePrefix + "'yyyy-MM-dd'" + FILE_EXTENSION + "'");
        this.fileName = generateFileName();
    }

    protected String generateFileName() {
        String currentDateFile = LocalDate.now().format(formatter);
        return currentDateFile;
    }

    protected List<File> findInputFiles(final String filePrefix, String fileDirectiry) {
        List<File> matchingFiles = new ArrayList<>();
        File folder = new File(fileDirectiry);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(FILE_EXTENSION));
        LocalDate currentDate = LocalDate.now();

        for (File file : files) {
            String fileName = file.getName();
            if (fileName.startsWith(filePrefix) && fileName.endsWith(FILE_EXTENSION)) {
                try {
                    LocalDate fileDate = LocalDate.parse(fileName, formatter);

                    if (fileDate.getMonth().equals(currentDate.getMonth()) && fileDate.getYear() == currentDate.getYear()) {
                        matchingFiles.add(file);
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid file name format: " + fileName);
                }
            }
        }

        return matchingFiles;
    }

    protected void createDirectoryIfNotExists(String directoryPath) {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                System.out.println("Directory created: " + directoryPath);
            } catch (IOException e) {
                System.out.println("Failed to create directory: " + directoryPath);
                e.printStackTrace();
            }
        }
    }

    public abstract void save(T object) throws IOException;

    public abstract void delete(Integer id) throws IOException;

    public abstract void update(Integer id, T updatedObject) throws IOException;

    public abstract Map<Integer, T> read() throws IOException;

}
