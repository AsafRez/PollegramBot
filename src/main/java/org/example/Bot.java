package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

import org.example.MainScreen.*;

public class Bot extends TelegramLongPollingBot {
    private Map<Long,String> users;
    private static Bot instance;
    private Set<Survey> survey;
    public Bot() {
        users = new HashMap<>();
        survey = new HashSet<>();
        if (instance == null) {
            instance = this;
        }
    }
    public static Bot getInstance() {
        return instance;
    }
    public void onUpdateReceived(Update update) {
        try {
            // --- בלוק 1: טיפול בתשובות סקר (PollAnswer)
            if (update.hasPollAnswer()) {
                org.telegram.telegrambots.meta.api.objects.polls.PollAnswer pollAnswer = update.getPollAnswer();

                // הדפסת פרטי התשובה כדי לבצע דיבוג
                System.out.println("Received a poll answer from user ID: " + pollAnswer.getUser().getId());
                System.out.println("Poll ID: " + pollAnswer.getPollId());

                // יציאה מהשיטה כדי למנוע את השגיאה הבאה
                return;
            }

            // --- בלוק 2: טיפול בהודעות טקסט רגילות (Message)
            // הקוד מגיע לכאן רק אם העדכון אינו תשובת סקר
            if (update.hasMessage() && update.getMessage().hasText()) {
                long chatId = update.getMessage().getChatId();

                if(!users.containsKey(chatId)) {
                    users.put(chatId, update.getMessage().getFrom().getFirstName());
                    MainScreen.Users_from_Bot++;
                    System.out.println("New user added. Total users: " + users.size());
                }
            }
        } catch (Exception e) {
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

    public void send_Poll(Survey survey) {
        try{
            this.survey.add(survey);
        for(Question q:survey.getQuestions()) {
            SendPoll poll = new SendPoll();
            poll=new SendPoll();
            poll.setQuestion(q.getQuestion());
            poll.setOptions(q.getAnswers());
            for (Long id : users.keySet()) {
                poll.setChatId(id);
                execute(poll);
//                Message poll =new
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

