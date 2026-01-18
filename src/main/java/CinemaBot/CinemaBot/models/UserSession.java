package CinemaBot.CinemaBot.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_session")
public class UserSession {
    @Id
    @Column(name = "user_id")
    private Long chatId; // Используем chatId как уникальный ключ

    @Column(name = "last_query")
    private String lastQuery; // Тут будем хранить текст поиска

    @Column(name = "create_date", updatable = false, insertable = false)
    private LocalDateTime createDate;

    @Column(name = "last_movie_id")
    private Long lastMovieId; // ID последнего фильма
    @Column(name = "last_movie_titles")
    private String lastMovieTitle; // title последнего фильма

    @Transient // чтобы JPA не пытался сохранять это поле
    private MovieView lastMovieView;
}
