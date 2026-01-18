package CinemaBot.CinemaBot.util;

import CinemaBot.CinemaBot.models.MovieView;
import CinemaBot.CinemaBot.models.WatchList;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.ArrayList;
import java.util.List;

@Component
public class MovieFormatter {

    public String formatMovie(MovieView movie) {
        return String.format("🎬 %s\n📅 %s\n\n📝 %s\n\n⭐ Рейтинг: %s",
                movie.getTitle(), movie.getDate(), longestOverview(movie.getOverview()), movie.getRating());
    }

    public String formatFavoriteMovie(WatchList watchList){
        return String.format("🎬 %s", watchList.getMovieTitle());
    }

    public InlineKeyboardMarkup buildMovieKeyboard(
            MovieView movie,
            Long movieId,
            boolean isFavorite
    ) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        // Ряд навигации
        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();

        if (movie.getIndex() > 0) {
            inlineKeyboardButtons.add(InlineKeyboardButton.builder()
                    .text("⬅️")
                    .callbackData("movie|" + (movie.getIndex() - 1))
                    .build());
        }

        inlineKeyboardButtons.add(InlineKeyboardButton.builder()
                .text((movie.getIndex() + 1) + " / " + movie.getTotal())
                .callbackData("noop")
                .build());

        if (movie.getIndex() < movie.getTotal() - 1) {
            inlineKeyboardButtons.add(InlineKeyboardButton.builder()
                    .text("➡️")
                    .callbackData("movie|" + (movie.getIndex() + 1))
                    .build());
        }

        keyboard.add(inlineKeyboardButtons);

        // Ряд избранного
        InlineKeyboardButton favButton = InlineKeyboardButton.builder()
                .text(isFavorite ? "❌ Убрать из избранного" : "⭐ В избранное")
                .callbackData(
                        (isFavorite ? "FAV_DEL:" : "FAV_ADD:") + movieId
                )
                .build();

        keyboard.add(List.of(favButton));

        return InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();
    }

    public InlineKeyboardMarkup buildFavoriteMovieKeyboardToRemoveFromFavorite(Long movieTmdbId){
        InlineKeyboardButton removeFromFavorite = InlineKeyboardButton.builder()
                .text("❌ Убрать из избранного")
                .callbackData("FAV_DEL:" + movieTmdbId)
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(List.of(removeFromFavorite)))
                .build();
    }

    public InlineKeyboardMarkup buildFavoriteToggleOnly(Message message, Long movieId, boolean isFavorite) {
        InlineKeyboardMarkup oldMarkup = message.getReplyMarkup();
        List<List<InlineKeyboardButton>> oldKeyboard = oldMarkup != null ? oldMarkup.getKeyboard() : new ArrayList<>();
        List<List<InlineKeyboardButton>> newKeyboard = new ArrayList<>();
        for (int i = 0; i < oldKeyboard.size() - 1; i++) {
            newKeyboard.add(new ArrayList<>(oldKeyboard.get(i)));
        }
        InlineKeyboardButton favButton = InlineKeyboardButton.builder()
                .text(isFavorite ? "❌ Убрать" : "⭐ Добавить")
                .callbackData((isFavorite ? "FAV_DEL:" : "FAV_ADD:") + movieId)
                .build();
        newKeyboard.add(List.of(favButton));
        return InlineKeyboardMarkup.builder()
                .keyboard(newKeyboard)
                .build();
    }

    private String longestOverview(String text) {
        if (text.length() > 500) return text.substring(0, 500) + "...";
        return text;
    }
}
