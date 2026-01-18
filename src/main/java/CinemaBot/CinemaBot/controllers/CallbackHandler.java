package CinemaBot.CinemaBot.controllers;

import CinemaBot.CinemaBot.models.MovieView;
import CinemaBot.CinemaBot.services.MovieApiService;
import CinemaBot.CinemaBot.services.UserSessionService;
import CinemaBot.CinemaBot.services.WatchListService;
import CinemaBot.CinemaBot.util.MovieFormatter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CallbackHandler {

    private final FavoriteHandler favoriteHandler;
    private final MovieApiService movieService;
    private final UserSessionService sessionService;
    private final WatchListService watchListService;
    private final MovieFormatter formatter;
    private final TelegramBotExecutor telegramBotExecutor;

    public CallbackHandler(FavoriteHandler favoriteHandler,
                           MovieApiService movieService,
                           UserSessionService sessionService,
                           WatchListService watchListService,
                           MovieFormatter formatter, @Lazy TelegramBotExecutor telegramBotExecutor) {
        this.favoriteHandler = favoriteHandler;
        this.movieService = movieService;
        this.sessionService = sessionService;
        this.watchListService = watchListService;
        this.formatter = formatter;
        this.telegramBotExecutor = telegramBotExecutor;
    }

    public void handleCallback(Update update) {
        CallbackQuery callback = update.getCallbackQuery();
        if (callback == null || callback.getData() == null) return;

        String data = callback.getData();

        if ("noop".equals(data)) return;

        // ===== Обработка избранного =====
        if (data.startsWith("FAV_")) {
            favoriteHandler.handleFavorite(callback);
        }

        if (data.startsWith("movie|")) {
            handleMovieNavigation(callback);
        }
    }

    private void handleMovieNavigation(CallbackQuery callback) {
        int index = Integer.parseInt(callback.getData().split("\\|")[1]);
        long chatId = callback.getMessage().getChatId();

        String query = sessionService.getLastQuery(chatId);

        MovieView movie = movieService.getMovieByIndex(query, index);
        if (movie == null) return;

        sessionService.saveSession(chatId, query, movie);

        boolean isFavorite = watchListService.checkFavoriteMovie(
                callback.getFrom().getId(),
                movie.getId()
        );

        EditMessageText edit = EditMessageText.builder()
                .chatId(chatId)
                .messageId(callback.getMessage().getMessageId())
                .text(formatter.formatMovie(movie))
                .replyMarkup(formatter.buildMovieKeyboard(movie, movie.getId(), isFavorite))
                .build();

        telegramBotExecutor.executeSafe(edit);
    }
}
