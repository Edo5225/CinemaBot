package CinemaBot.CinemaBot.models;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MovieView {
    String title;
    String date;
    String overview;
    Double rating;
    int index;
    int total;
    Long id;
}

