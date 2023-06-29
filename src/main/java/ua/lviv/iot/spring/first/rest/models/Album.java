package ua.lviv.iot.spring.first.rest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Integer releaseYear;
    private List<Integer> songsId = new ArrayList<>();


    @JsonIgnore
    public String getHeaders() {
        return "id, title, genre, releaseYear, songsId";
    }

    public String toCSV() {
        return id + ", " + title + ", " + genre + ", " + releaseYear + ", " + songsId;
    }

}
