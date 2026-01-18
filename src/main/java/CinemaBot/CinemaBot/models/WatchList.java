package CinemaBot.CinemaBot.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "watchlist")
public class WatchList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "user_id",          // колонка в watchlist
            referencedColumnName = "telegram_id" // колонка в users
    )
    private Users user;

    @Column(name = "movie_tmdb_id", nullable = false)
    private Long movieTmdbId;

    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(name = "movie_title")
    private String movieTitle;
}
