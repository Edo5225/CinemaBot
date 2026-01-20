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
    private String title;
    @JsonProperty("name")
    String name;
    @JsonProperty("vote_average")
    private  Double rating;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("first_air_date")
    private String firstAirDate;
    @JsonProperty("overview")
    private String overview;
    @JsonProperty("media_type")
    private String mediaType;
}
