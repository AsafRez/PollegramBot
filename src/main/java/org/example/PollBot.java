package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class PollBot extends TelegramLongPollingBot{
    private Map<Long,String> users;
    public PollBot() {
        users = new Hashtable<>();
    }

    @Override
    public void onUpdateReceived(Update update) {
        // מומלץ להוסיף טיפול בחריגים עבור הפעולה כולה
        try {
            // בדוק אם העדכון מכיל הודעה ומידע על השולח
            // הוסף את המשתמש לרשימה
            System.out.println(update.getMessage().getFrom().getFirstName());
            System.out.println(update.getMessage().getChatId());
            Subs newsb=new Subs(update.getMessage().getChatId(),update.getMessage().getFrom().getFirstName());
            if(!users.containsKey(update.getMessage().getChatId())) {
                users.put(update.getMessage().getChatId(),update.getMessage().getFrom().getFirstName());
            }

            // הדפס את גודל הרשימה פעם אחת בלבד לאחר הוספת המשתמש
            System.out.println("User added. Total users: " + users.size());
        }
        catch (Exception e) {
            // הדפס את פרטי החריגה במידה וקרתה שגיאה
            e.printStackTrace();
        }
    }
    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }
    @Override
    public String getBotUsername() {
        return "PollesGram_Bot";
    }
    public String getBotToken() {
        return "7607509792:AAFtAghav5RbhXQ5IcMp9qNoPiBvyK71Bh8";
    }

}