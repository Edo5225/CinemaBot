package CinemaBot.CinemaBot.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramBotController extends TelegramLongPollingBot{

    private final String botName;
    private final MessageHandler messageHandler;
    private final CallbackHandler callbackHandler;

    public TelegramBotController(@Value("${bot.token}") String botToken,
                                 @Value("${bot.name}") String botName,
                                 MessageHandler messageHandler,
                                 CallbackHandler callbackHandler)
    {
        super(botToken);
        this.botName = botName;
        this.messageHandler = messageHandler;
        this.callbackHandler = callbackHandler;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            messageHandler.handleMessage(update);
        } else if (update.hasCallbackQuery()) {
            callbackHandler.handleCallback(update);
        }
    }
}
