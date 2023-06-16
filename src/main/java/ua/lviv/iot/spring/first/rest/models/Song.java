package ua.lviv.iot.spring.first.rest.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Song {

    private Integer id;
    private String title;
    private String duration;
    private int releaseYear;
    private String lyrics;

    public void setDuration(final String duration) {
        if (isValidDurationFormat(duration)) {
            this.duration = duration;
        } else {
            throw new IllegalArgumentException("Invalid duration format. Expected format: mm:ss");
        }
    }

    private boolean isValidDurationFormat(final String duration) {
        String regex = "(\\d{2}):(\\d{2})";
        return duration.matches(regex);
    }

//    public void setDuration(String duration) {
//        String[] parts = duration.split(":");
//        int minutes = Integer.parseInt(parts[0]);
//        int seconds = Integer.parseInt(parts[1]);
//
//        this.duration = String.format("%02d:%02d", minutes, seconds);
//    }
//
//    public String getDurationFormatted() {
//        int minutes = duration / 60;
//        int seconds = duration % 60;
//        return String.format("%02d:%02d", minutes, seconds);
//    }

    @JsonIgnore
    public String getHeaders() {
        return "id, title, duration, releaseYear, lyrics";
    }

    public String toCSV() {
        return id + ", " + title + ", " + duration + ", " + releaseYear + ", " + lyrics;
    }
}
