package CinemaBot.CinemaBot.controllers;

import CinemaBot.CinemaBot.models.UserSession;
import CinemaBot.CinemaBot.models.WatchList;
import CinemaBot.CinemaBot.services.UserSessionService;
import CinemaBot.CinemaBot.services.WatchListService;
import CinemaBot.CinemaBot.util.MovieFormatter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import java.util.List;

@Component
public class FavoriteHandler {

    private final WatchListService watchListService;
    private final UserSessionService sessionService;
    private final MovieFormatter formatter;
    private final TelegramBotExecutor telegramBotExecutor;

    public FavoriteHandler(WatchListService watchListService,
                           UserSessionService sessionService,
                           MovieFormatter formatter,
                           @Lazy TelegramBotExecutor telegramBotExecutor) {
        this.watchListService = watchListService;
        this.sessionService = sessionService;
        this.formatter = formatter;
        this.telegramBotExecutor = telegramBotExecutor;

    }

    public void handleFavorite(CallbackQuery callback) {
        String data = callback.getData();
        long chatId = callback.getMessage().getChatId();
        int messageId = callback.getMessage().getMessageId();
        Long telegramId = callback.getFrom().getId();
        Long movieId = Long.parseLong(data.split(":")[1]);

        boolean nowFavorite;
        if (data.startsWith("FAV_ADD")) {
            UserSession session = sessionService.getSession(chatId);
            String movieTitle = session != null ? session.getLastMovieTitle() : "Фильм";
            watchListService.addToFavorite(telegramId, movieId, movieTitle);
            nowFavorite = true;
        } else {
            watchListService.removeFromFavorite(telegramId, movieId);
            nowFavorite = false;
        }
        telegramBotExecutor.answerCallback(callback);
        EditMessageReplyMarkup editMarkup = EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(formatter.buildFavoriteToggleOnly(callback.getMessage(), movieId, nowFavorite))
                .build();
        telegramBotExecutor.executeSafe(editMarkup);
    }

    public void sendFavoriteMovie(long chatId) {
        List<WatchList> watchList = watchListService.findAllFavoriteMovies(chatId);
        for (WatchList list : watchList) {
            SendMessage msg = SendMessage.builder()
                    .chatId(chatId)
                    .text(formatter.formatFavoriteMovie(list))
                    .replyMarkup(formatter.buildFavoriteMovieKeyboardToRemoveFromFavorite(list.getMovieTmdbId()))
                    .build();
            telegramBotExecutor.executeSafe(msg);

        }
    }
}

