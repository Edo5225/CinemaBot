package CinemaBot.CinemaBot.controllers;

import CinemaBot.CinemaBot.models.MovieView;
import CinemaBot.CinemaBot.services.MovieApiService;
import CinemaBot.CinemaBot.services.UserService;
import CinemaBot.CinemaBot.services.UserSessionService;
import CinemaBot.CinemaBot.services.WatchListService;
import CinemaBot.CinemaBot.util.MainKeyboardFactory;
import CinemaBot.CinemaBot.util.MovieFormatter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.List;

@Component
public class MessageHandler {

    private final UserService userService;
    private final MovieApiService movieService;
    private final UserSessionService sessionService;
    private final WatchListService watchListService;
    private final MovieFormatter formatter;
    private final MainKeyboardFactory mainKeyboardFactory;
    private final TelegramBotExecutor bot;
    private final FavoriteHandler favoriteHandler;

    public MessageHandler(UserService userService,
                          MovieApiService movieService,
                          UserSessionService sessionService,
                          WatchListService watchListService,
                          MovieFormatter formatter,
                          MainKeyboardFactory mainKeyboardFactory,
                          @Lazy TelegramBotExecutor bot,
                          FavoriteHandler favoriteHandler) {
        this.userService = userService;
        this.movieService = movieService;
        this.sessionService = sessionService;
        this.watchListService = watchListService;
        this.formatter = formatter;
        this.mainKeyboardFactory = mainKeyboardFactory;
        this.bot = bot;
        this.favoriteHandler = favoriteHandler;
    }

    public void handleMessage(Update update) {
        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        Long telegramId = update.getMessage().getFrom().getId();
        String username = update.getMessage().getFrom().getUserName();

        userService.saveUser(telegramId, username);

        if (text.equals("/start")) {
            sendStartMessage(chatId);
        } else if (text.startsWith("/searchall ")) {
            handleSearchAll(chatId, text);
        } else if (text.startsWith("/search ")) {
            handleSearch(chatId, text);
        } else if ("⭐ Избранное".equals(text)) {
            favoriteHandler.sendFavoriteMovie(chatId);
        } else {
            sendMessage(chatId, "Неизвестная команда 😕");
        }
    }

    private void sendStartMessage(long chatId) {
        SendMessage msg = SendMessage.builder()
                .chatId(chatId)
                .text("Добро пожаловать!")
                .replyMarkup(mainKeyboardFactory.mainMenu())
                .build();
        bot.executeSafe(msg);
    }

    private void sendMessage(long chatId, String text) {
        SendMessage msg = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        bot.executeSafe(msg);
    }

    private void handleSearch(long chatId, String text) {
        String query = text.substring("/search".length()).trim();
        sessionService.saveSession(chatId, query, null);
        MovieView movie = movieService.getMovieByIndex(query, 0);
        if (movie == null) {
            sendMessage(chatId, "Фильм не найден");
            return;
        }
        SendMessage msg = SendMessage.builder()
                .chatId(chatId)
                .text(formatter.formatMovie(movie))
                .replyMarkup(formatter.buildMovieKeyboard(movie, movie.getId(),
                        watchListService.checkFavoriteMovie(chatId, movie.getId())))
                .build();
        bot.executeSafe(msg);
        sessionService.saveSession(chatId, query, movie);
    }

    private void handleSearchAll(long chatId, String text) {
        String query = text.replace("/searchall", "").trim();
        List<MovieView> movies = movieService.findAllMovie(query);
        for (MovieView movie : movies) {
            SendMessage msg = SendMessage.builder()
                    .chatId(chatId)
                    .text(formatter.formatMovie(movie))
                    .replyMarkup(formatter.buildMovieKeyboard(movie, movie.getId(),
                            watchListService.checkFavoriteMovie(chatId, movie.getId())))
                    .build();
            bot.executeSafe(msg);
            sessionService.saveSession(chatId, query, movie);
        }
    }
}
