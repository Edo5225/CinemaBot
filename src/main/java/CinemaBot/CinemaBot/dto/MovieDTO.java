package CinemaBot.CinemaBot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("title")
    String title;
    @JsonProperty("name")
    String name;
    @JsonProperty("vote_average")
    public Double rating;
    @JsonProperty("release_date")
    public String releaseDate;
    @JsonProperty("first_air_date")
    public String firstAirDate;
    @JsonProperty("overview")
    public String overview;
}
