package ua.lviv.iot.spring.first.rest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MusicLabel {

    private Integer id;
    private String title;
    private LocalDate dateOfEstablishment;
    private List<Integer> artistsId = new ArrayList<>();

    @JsonIgnore
    public String getHeaders() {
        return "id, title, dateOfEstablishment, artistsId";
    }

    public String toCSV() {
        return id + ", " + title + ", " + dateOfEstablishment + ", " + artistsId;
    }

}
