package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.MainScreen.*;

public class Bot extends TelegramLongPollingBot {
    private Map<Long,String> users;
    private static Bot instance;
    private Survey survey;
    public Bot() {
        users = new HashMap<>();
        if (instance == null) {
            instance = this;
        }
    }
    public static Bot getInstance() {
        return instance;
    }
    public void onUpdateReceived(Update update) {
        try {
            if(!users.containsKey(update.getMessage().getChatId())) {
                users.put(update.getMessage().getChatId(),update.getMessage().getFrom().getFirstName());
                MainScreen.Users_from_Bot++;
                System.out.println(users.size());
            }
        }
        catch (Exception e) {
            // הדפס את פרטי החריגה במידה וקרתה שגיאה
            e.printStackTrace();
        }
    }
    public void setSurvey(Survey survey) {
        this.survey = survey;
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

    public void send_Poll(Survey survey) {
        try{
        for(Question q:survey.getQuestions()) {
            SendPoll poll = new SendPoll();
            poll=new SendPoll();
            poll.setQuestion(q.getQuestion());
            poll.setOptions(q.getAnswers());
            for (Long id : users.keySet()) {
                poll.setChatId(id);
                execute(poll);
            }
        }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Map<Long,String> getUsers() {
        return users;
    }

}

