package org.example;

import java.util.HashMap;
import java.util.HashSet;

public class Surveys {
    private HasT<Survey> surveys;
    public Surveys() {
        surveys = new HashSet<>();
    }
    public void addSurvey(Survey survey) {
        surveys.add(survey);
    }
}
