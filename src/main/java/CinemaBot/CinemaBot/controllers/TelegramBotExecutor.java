package CinemaBot.CinemaBot.controllers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBotExecutor {

    private final TelegramBotController bot;

    public TelegramBotExecutor(TelegramBotController bot) {
        this.bot = bot;
    }

    public void executeSafe(BotApiMethod<?> method) {
        try {
            bot.execute(method);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void answerCallback(CallbackQuery callback) {
        AnswerCallbackQuery answer = AnswerCallbackQuery.builder()
                .callbackQueryId(callback.getId())
                .text("Готово!")
                .showAlert(false)
                .build();
        executeSafe(answer);
    }
}
