package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class PollBot_IR extends TelegramLongPollingBot{
    private Map<Long,String> users;
    private final Survey survey;
    public PollBot_IR(Survey survey) {
        users = new Hashtable<>();
        this.survey = survey;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if(!users.containsKey(update.getMessage().getChatId())) {
                users.put(update.getMessage().getChatId(),update.getMessage().getFrom().getFirstName());
            }

            if(!users.isEmpty()){
                this.runBot();

            }
        }
        catch (Exception e) {
            // הדפס את פרטי החריגה במידה וקרתה שגיאה
            e.printStackTrace();
        }
    }
    private void runBot() throws TelegramApiException {
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