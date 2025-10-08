package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)

public class Question {
    private String question;
    private List<String> answers;
    public Question(String question,int numberOfAnswers) {
        this.answers = new ArrayList<>();
        this.statistics = new int[numberOfAnswers];
    }
    public Question() {
        this.answers = new ArrayList<>();
    }
    private int[] statistics;
    private String telegramPollId;

    public Question(String question, List<String> answers) {
        this.question = question;
        this.answers = answers;
        this.statistics = new int[answers.size()];
    }
    public void addAnswer(String answer) {
        this.answers.add(answer);


    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(List<String> answer) {
        this.answers = answer;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void incrementStatistic(int answerIndex) {
        if (answerIndex >= 0 && answerIndex < this.statistics.length) {
            this.statistics[answerIndex]++;
        }
    }

    public int[] getStatistics() {
        return statistics;
    }

    public void setTelegramPollId(String telegramPollId) {
        this.telegramPollId = telegramPollId;
    }

    public String getTelegramPollId() {
        return telegramPollId;
    }



@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String answer : answers) {
            sb.append("ת:"+answer).append("\n ");
        }
        return sb.toString();

    }

}
