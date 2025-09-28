package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Survey {
    private static int counterId;
    private int id;
    private List<Question> questions;
    private String title;
    private boolean isClosed = false;
    private String Telegramid;
    private int[] statistics = new int[4];
    private Map<String, Question> pollIdToQuestionMap = new HashMap<>();


    public String getTelegramid() {
        return Telegramid;
    }

    public void setTelegramid(String telegramid) {
        Telegramid = telegramid;
    }

    public Survey() {
        questions = new ArrayList<Question>();
        this.id = counterId;
        counterId++;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public List<Question> getQuestions() {
        return questions;
    }
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public int[] getStatistics() {
        return statistics;
    }

    public void setStatistics(int input) {
        this.statistics [input] ++;
    }

    public Map<String, Question> getPollIdToQuestionMap() {
        return pollIdToQuestionMap;
    }

    public String statisticToString() {
        return "Survey{" +
                "statistics=" + Arrays.toString(statistics) +
                '}';
    }

    public String toString() {
        return title != null ? title : "סקר ללא שם";
    }
}
