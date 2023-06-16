package ua.lviv.iot.spring.first.rest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Artist {

    private Integer id;
    private String name;
    private LocalDate dateOfBirth;
    private List<Integer> albumsId = new ArrayList<>();
    //private List<Integer> labelsId = new ArrayList<>();

    @JsonIgnore
    public String getHeaders() {
        return "id, name, dateOfBirth, albumsId";
    }

    public String toCSV() {
        return id + ", " + name + ", " + dateOfBirth + ", " + albumsId;
    }

    public List<Integer> getAlbumsId() {
        if (albumsId != null) {
            return Collections.unmodifiableList(albumsId);
        }
        return Collections.emptyList();
    }

//    public List<Integer> getLabelsId() {
//        if (labelsId != null) {
//            return Collections.unmodifiableList(labelsId);
//        }
//        return Collections.emptyList();
//    }
}
