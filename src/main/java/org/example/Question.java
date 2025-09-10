package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)

public class Question {
    private String question;
    private List<String> answers;
    public Question() {
        this.answers = new ArrayList<>();
    }
    public Question(String question, List<String> answers) {
        this.question = question;
        this.answers = answers;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String answer : answers) {
            sb.append("ת:"+answer).append("\n ");
        }
        return sb.toString();

    }

}
