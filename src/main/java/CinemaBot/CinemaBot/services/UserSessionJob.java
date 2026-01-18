package CinemaBot.CinemaBot.services;

import CinemaBot.CinemaBot.repositories.UserSessionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserSessionJob {
    private final UserSessionRepository userSessionRepository;

    public UserSessionJob(UserSessionRepository userSessionRepository) {
        this.userSessionRepository = userSessionRepository;
    }

    @Scheduled(fixedRate = 999930000)
    @Transactional
    public void clearUserSession(){
        System.out.println("Job start");
        userSessionRepository.deleteAllSession();
        System.out.println("Job end");
    }
}
