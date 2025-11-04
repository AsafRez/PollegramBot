package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.polls.StopPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.polls.PollAnswer;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.example.MainScreen.*;

public class Bot extends TelegramLongPollingBot {
    private Map<Long, String> users;
    private static Bot instance;
    private Set<Survey> survey;
    private Survey activeSurvey = null;
    private Integer activePollId = null;
    private Set<Long> answeredUsers = new HashSet<>();
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private Map<Long, Integer> userProgress = new HashMap<>();



    public Bot() {
        users = new HashMap<>();
        survey = new HashSet<>();
        if (instance == null) {
            instance = this;
        }
        if (new File("src/Users_file.txt").exists()){
            LoadFromTextFile();
        }else{
            try {
                new FileWriter("src/Users_file.txt", true);
            }
            catch (Exception e){

            }
        }
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

    public Set<Survey> getSurveys() {
        return survey;
    }

    public Survey getActiveSurvey() {
        return activeSurvey;
    }

    public void onUpdateReceived(Update update) {
        try {
            // תשובה לסקר
            if (update.hasPollAnswer()) {
                PollAnswer answer = update.getPollAnswer();
                long userId = answer.getUser().getId();
                String pollId = answer.getPollId();
                Question answeredQuestion = activeSurvey.getPollIdToQuestionMap().get(pollId);
                if (answeredQuestion != null) {
                    // עדכון התקדמות המשתמש
                    userProgress.put(userId, userProgress.getOrDefault(userId, 0) + 1);
                    // עדכון הסטטיסטיקה של השאלה הספציפית
                    int answerIndex = answer.getOptionIds().get(0);
                    answeredQuestion.incrementStatistic(answerIndex);
                    System.out.println("המשתמש: " + answer.getUser().getFirstName() + " ענה על שאלה מספר " + userProgress.get(userId));
                    activeSurvey.setStatistics(answer.getOptionIds().get(0));
                    checkAndCloseSurvey();
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
                        String messageText = "🎉 הצטרפת לקהילה, ברוך הבא! ";
                        sendTextMessage(chatId, messageText);
                        MainScreen.Users_from_Bot++;

                        messageText = "🎉 חבר חדש הצטרף לקהילה: " + firstName +
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
                System.out.println("כבר יש סקר פעיל, אי אפשר לפתוח חדש :(");
                return;

            }

            if (users.isEmpty()) {
                System.out.println("אין משתמשים לשליחת הסקר");
                return;
            }
            userProgress.clear();
            for (Long chatId : users.keySet()) {
                userProgress.put(chatId, 0);
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

                    if (msg.getPoll() != null && msg.getPoll().getId() != null) {
                        q.setTelegramPollId(msg.getPoll().getId());
                        survey.getPollIdToQuestionMap().put(msg.getPoll().getId(), q);
                    }


                    // שמירה של מזהה הסקר והסקר הפעיל פעם אחת בלבד
                    if (isFirst) {
                        activePollId = msg.getMessageId(); // מזהה Poll ראשון
                        activeSurvey = survey;
                        activeSurvey.setTelegramid(String.valueOf(activePollId));
                        activeSurvey.setClosed(false);
                        answeredUsers.clear();


                        // סגירה אוטומטית אחרי 5 דקות
                        scheduler.schedule(this::closeActivePoll, 5, TimeUnit.MINUTES);
                        isFirst = false;
                    }
                }
            }
            if (this.survey.isEmpty()){
                MainScreen.surveyCombo.removeAllItems();
                MainScreen.surveyCombo.addItem(activeSurvey);
            }else {
            MainScreen.surveyCombo.addItem(activeSurvey);
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
            System.out.println("User: " + string + " ID: " + key + " added to file");
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
            System.out.println("error reading users file");
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

    private void checkAndCloseSurvey() {
        if (activeSurvey == null || activeSurvey.isClosed()) {
            return;
        }
        int totalQuestions = activeSurvey.getQuestions().size();

        for (Long userId : userProgress.keySet()) {
            if (userProgress.get(userId) < totalQuestions) {
                return;

            }
        }
        System.out.println("כל המשתמשים ענו על כל השאלות, הסקר נסגר ✅ ");
        closeActivePoll();
    }
}