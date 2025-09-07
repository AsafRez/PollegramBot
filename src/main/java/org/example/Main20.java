package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main20 {
    public static void main(String[] args) {
        MainScreen mainScreen = new MainScreen();
        mainScreen.setVisible(true);
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new Bot());
            FinalSurvey.telegramBotsApi = telegramBotsApi;
        } catch (TelegramApiException s) {
            throw new RuntimeException(s);
        }
    }
}
