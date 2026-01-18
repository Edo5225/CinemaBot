package CinemaBot.CinemaBot.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor(force = true)
public class Users {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;
    @Column(name = "telegram_id")
    private  Long telegramId;
    @Column(name = "username")
    private  String username;
    @Column(name = "create_date")
    private  LocalDateTime registeredAt;

    @OneToMany(mappedBy = "user")
    private List<WatchList> watchLists;
}
