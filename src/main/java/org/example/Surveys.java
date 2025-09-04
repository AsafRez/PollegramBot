package org.example;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Surveys {
    private Set<Survey> surveys = new HashSet<>();

    public void addSurvey (Survey survey){
        surveys.add(survey);
    }

}
