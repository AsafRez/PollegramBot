package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)

public class Survey {
    private static int counterId;
    private int id;
    private List<Question> questions;

    public Survey() {
        questions = new ArrayList<Question>();
        this.id = counterId;
        counterId++;
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
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Question question : questions) {
            sb.append("ש:").append(question.getQuestion()).append("\n");
            sb.append(question.toString()).append("\n");
        }
        return sb.toString();
    }


}
