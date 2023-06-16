package ua.lviv.iot.spring.first.rest.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Album {

    private Integer id;
    private String title;
    private String genre;
    private int releaseYear;
    private List<Integer> songsId = new ArrayList<>();


    @JsonIgnore
    public String getHeaders() {
        return "id, title, genre, releaseYear, songsId";
    }

    public String toCSV() {
        return id + ", " + title + ", " + genre + ", " + releaseYear + ", " + songsId;
    }

    public List<Integer> getSongsId() {
        return new ArrayList<>(songsId);
    }

    public void setSongsId(final List<Integer> songsId) {
        this.songsId = new ArrayList<>(songsId);
    }

}
