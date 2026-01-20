package CinemaBot.CinemaBot.services;

import CinemaBot.CinemaBot.dto.MovieDTO;
import CinemaBot.CinemaBot.models.MovieView;
import CinemaBot.CinemaBot.repositories.WatchlistRepository;
import CinemaBot.CinemaBot.util.TmdbClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class MovieApiService {
    private final TmdbClient tmdbClient;
    public MovieApiService(TmdbClient tmdbClient, WatchlistRepository watchlistRepository) {
        this.tmdbClient = tmdbClient;
    }

    @Value("${tmdb.api.key}") // Берем ключ из application.properties
    private String apiKey;

    // Получаем один фильм по индексу
    public MovieView getMovieByIndex(String query, int index) {
        var response = tmdbClient.searchMovies(apiKey, query, "ru-RU");
        if (response == null || response.getResults() == null || response.getResults().isEmpty())
            return null;
        List<MovieDTO> movies = response.getResults().stream()
                .filter(dto -> !"person".equals(dto.getMediaType()))
                .toList();
//        var sortMovies = sortMovies(movies); TODO добавить метод с сортировкой
        if (index < 0 || index >= movies.size()) return null;
        var dto = movies.get(index);
        String title = dto.getTitle() != null ? dto.getTitle() : dto.getName();
        String date = dto.getReleaseDate() != null ? dto.getReleaseDate() : dto.getFirstAirDate();
        String overview = (dto.getOverview().isBlank() || dto.getOverview() == null) ? "Описание отсутствует" : dto.getOverview();
        String rating = dto.getRating() != null ? dto.getRating().toString() : "нет рейтинга";
        return new MovieView(title, date, overview, Double.parseDouble(rating), index, movies.size(), dto.getId());
    }

    public List<MovieView> findAllMovie(String title) {
        var movieResponse = tmdbClient.searchMovies(apiKey, title, "ru-RU");
        if (movieResponse == null || movieResponse.getResults() == null || movieResponse.getResults().isEmpty()) {
            return Collections.emptyList();
        }
        List<MovieView> list = new ArrayList<>();
        int i = 0;
        List<MovieDTO> movieDTO = movieResponse.getResults();
        for (MovieDTO dto : movieDTO) {
            String name;
            String date;
            String overview = dto.getOverview().isEmpty() ? "Описание отсутствует" : dto.getOverview();
            String rating = dto.getRating() != null ? dto.getRating().toString() : "нет рейтинга";
            if (dto.getTitle() != null && dto.getReleaseDate() != null){
                name = dto.getTitle();
                date = dto.getReleaseDate();
            }else {
                name = dto.getName();
                date = dto.getFirstAirDate();
            }
            list.add(new MovieView(name, date, overview, Double.parseDouble(rating), i++, movieResponse.getResults().size(), dto.getId()));
        }
        return list;
    }

    public List<MovieDTO> sortMovies(List<MovieDTO> movies){
        return movies.stream().sorted(Comparator.comparing(MovieDTO::getRating)).toList().reversed();
    }
}
