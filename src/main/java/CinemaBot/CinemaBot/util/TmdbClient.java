package CinemaBot.CinemaBot.util;

import CinemaBot.CinemaBot.dto.MovieResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "tmdbClient", url = "https://api.themoviedb.org/3/")
public interface TmdbClient {
    @GetMapping("/search/multi")
    MovieResponse searchMovies(@RequestParam("api_key") String apiKey,
                               @RequestParam("query") String query,
                               @RequestParam("language") String lang);
}
