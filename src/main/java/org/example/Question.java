package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)

public class Question {
    private String question;
    private List<String> answers;
    public Question() {
        this.answers = new ArrayList();
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


}
