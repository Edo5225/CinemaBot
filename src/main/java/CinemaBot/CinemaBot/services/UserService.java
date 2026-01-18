package CinemaBot.CinemaBot.services;

import CinemaBot.CinemaBot.models.Users;
import CinemaBot.CinemaBot.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveUser(Long telegram_id, String username) {
        var session = userRepository.findByTelegramId(telegram_id).orElse(new Users());
        session.setTelegramId(telegram_id);
        session.setUsername(username);
        userRepository.save(session);
    }
}
