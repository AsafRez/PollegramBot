package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.polls.StopPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.polls.PollAnswer;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Bot extends TelegramLongPollingBot {
    private Map<Long, String> users;
    private static Bot instance;
    private Set<Survey> survey;
    private Survey activeSurvey = null;
    private Integer activePollId = null;
    private Map<Long,Integer> answeredUsers = new HashMap<>();
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


    public Bot() {
        users = new HashMap<>();
        survey = new HashSet<>();
        if (instance == null) {
            instance = this;
        }
        LoadFromTextFile();
    }



    @Override
    public String getBotUsername() {
        return "PollesGram_Bot";
    }

    public String getBotToken() {
        return "7607509792:AAFtAghav5RbhXQ5IcMp9qNoPiBvyK71Bh8";
    }

    public static Bot getInstance() {
        return instance;
    }

    public Survey getActiveSurvey() {
        return activeSurvey;
    }

    public void onUpdateReceived(Update update) {
        try {

            // תשובה לסקר
            if (update.hasPollAnswer()) {
                PollAnswer answer = update.getPollAnswer();
                if(answeredUsers.containsKey(answer.getUser().getId())) {
                    answeredUsers.replace(answer.getUser().getId(), answeredUsers.get(answer.getUser().getId()) + 1);
                }else {
                    answeredUsers.put(answer.getUser().getId(),1);
                }
                System.out.println("המשתמש: " + answer.getUser() + "ענה על הסקר");
                System.out.println(answer.getOptionIds().get(0));
                activeSurvey.setStatistics(answer.getOptionIds().get(0));
                System.out.println(activeSurvey.statisticToString());

                // אם כל המשתמשים ענו → סגירה מיידית
                if (activeSurvey != null && answeredUsers.size()== users.size()){
                    for(int count : answeredUsers.values()) {
                        if(count!=activeSurvey.getQuestions().size()) {
                            return;
                        }
                    }
                    System.out.println("כל המשתמשים ענו הסקר, הסקר נסגר ✅ ");
                    closeActivePoll();
                }
                return;

            }

            // הודעת טקסט
            if (update.hasMessage() && update.getMessage().hasText()) {
                String text = update.getMessage().getText().trim();
                long chatId = update.getMessage().getChatId();

                if (text.equalsIgnoreCase("hi") || text.equalsIgnoreCase("היי") || text.equalsIgnoreCase("/start")) {
                    if (!users.containsKey(chatId)) {
                        String firstName = update.getMessage().getFrom().getFirstName();
                        users.put(chatId, firstName);
                        appendUserToFile(chatId, firstName);

                        String messageText = "🎉 חבר חדש הצטרף לקהילה: " + firstName +
                                "\n👥 גודל הקהילה כעת: " + users.size();

                        for (Long otherChatId : users.keySet()) {
                            if (!otherChatId.equals(chatId)) {
                                sendTextMessage(otherChatId, messageText);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }



    public void send_Poll(Survey survey) {
        try {
            // בדיקה אם כבר קיים סקר פעיל
            if (activeSurvey != null && !activeSurvey.isClosed()) {
                System.out.println("כבר יש סקר פעיל, אי אפשר לפתוח חדש");
                return;
            }

            if (users.isEmpty()) {
                System.out.println("אין משתמשים לשליחת הסקר");
                return;
            }

            // שליחת הסקר לכל המשתמשים
            boolean isFirst = true;
            for (Long chatId : users.keySet()) {
                for (Question q : survey.getQuestions()) {
                    SendPoll poll = new SendPoll();
                    poll.setQuestion(q.getQuestion());
                    poll.setOptions(q.getAnswers());
                    poll.setIsAnonymous(false);
                    poll.setChatId(chatId);

                    Message msg = execute(poll);

                    // שמירה של מזהה הסקר והסקר הפעיל פעם אחת בלבד
                    if (isFirst) {
                        activePollId = msg.getMessageId(); // מזהה Poll ראשון
                        activeSurvey = survey;
                        activeSurvey.setTelegramid(String.valueOf(activePollId));
                        activeSurvey.setClosed(false);
                        answeredUsers.clear();


                        // סגירה אוטומטית אחרי 5 דקות
                        scheduler.schedule(this::closeActivePoll, 5, TimeUnit.MINUTES);
                        System.out.println(scheduler.toString());
                        isFirst = false;
                    }
                }
            }
            if (this.survey.isEmpty()){
                MainScreen.surveyCombo.removeAllItems();
                MainScreen.surveyCombo.addItem(activeSurvey.getTitle());
            }else {
            MainScreen.surveyCombo.addItem(activeSurvey.getTitle());
            }
            this.survey.add(activeSurvey);
            System.out.println("✅ הסקר '" + survey.getTitle() + "' נשלח לכל המשתמשים");

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
    private void sendTextMessage(Long chatId, String text) {
        try {
            execute(new SendMessage(chatId.toString(), text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private void closeActivePoll() {
        if (activeSurvey == null || activeSurvey.isClosed()) return;

        try {
            Long firstChatId = users.keySet().iterator().next();
            StopPoll stopPoll = new StopPoll(firstChatId.toString(), activePollId);
            execute(stopPoll);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        // עדכון אובייקט הסקר
        activeSurvey.setClosed(true);

        activePollId = null;
        activeSurvey = null;
        answeredUsers.clear();
        System.out.println("✅ הסקר נסגר");
    }


}