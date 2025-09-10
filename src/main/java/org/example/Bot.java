package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.polls.PollAnswer;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

import org.example.MainScreen.*;

public class Bot extends TelegramLongPollingBot {
    private Map<Long, String> users;
    private static Bot instance;
    private Set<Survey> survey;

    public Bot() {
        users = new HashMap<>();
        survey = new HashSet<>();
        if (instance == null) {
            instance = this;
        }
        LoadFromTextFile();
    }

    public static Bot getInstance() {
        return instance;
    }

    public Set<Survey> getSurveys() {
        return survey;
    }

    public void onUpdateReceived(Update update) {
        try {
            System.out.println(update.hasPollAnswer());
            if (update.hasPollAnswer()) {
                PollAnswer answer = update.getPollAnswer();
                System.out.println("Poll ID: " + answer.getPollId());
                System.out.println("User ID: " + answer.getUser().getId());
                System.out.println("Option IDs: " + answer.getOptionIds());

            }

            // --- בלוק 2: טיפול בהודעות טקסט רגילות (Message)
            // הקוד מגיע לכאן רק אם העדכון אינו תשובת סקר
            if (update.hasMessage() && update.getMessage().hasText()) {
                long chatId = update.getMessage().getChatId();

                if (!users.containsKey(chatId)) {
                    users.put(chatId, update.getMessage().getFrom().getFirstName());
                    appendUserToFile(chatId, update.getMessage().getFrom().getFirstName());
                    MainScreen.Users_from_Bot++;
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
        try {
            this.survey.add(survey);
            for (Question q : survey.getQuestions()) {
                SendPoll poll = new SendPoll();
                poll.setQuestion(q.getQuestion());
                poll.setOptions(q.getAnswers());
                poll.setIsAnonymous(false); // חשוב לקבל PollAnswer עם user

                for (Long chatId : users.keySet()) {
                    poll.setChatId(chatId);
                    execute(poll); // שולח סקר לצ'אט
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void appendUserToFile(Long key, String string) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/Users_file.txt", true))) { // append = true
            writer.write(key + "|" + string +"\n");
            System.out.println("User: " + string + "ID: " + key + " added to file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void LoadFromTextFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Users_file.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                this.users.put(Long.valueOf(parts[0]), parts[1]);
                MainScreen.Users_from_Bot ++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}