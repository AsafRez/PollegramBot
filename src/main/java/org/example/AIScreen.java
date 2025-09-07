package org.example;

import javax.swing.*;

public class AIScreen extends Screen {



    public AIScreen(){
        super("AI Poll" );

        JLabel surveySubjectLabel = createLabel("נושא הסקר", RIGHT_COLUMN_WIDTH, 40, 100);
        this.add(surveySubjectLabel);

        JTextField surveySubject = createJTextField(40);
        this.add(surveySubject);


        JLabel AILabel = Screen.createLabel("תיאור מפורט של הסקר",Screen.RIGHT_COLUMN_WIDTH-25,120,150);
        JLabel AI_number_Answer = Screen.createLabel("מספר תשובות",Screen.RIGHT_COLUMN_WIDTH-25,160,150);
        JLabel AI_number_quest = Screen.createLabel("מספר שאלות",Screen.RIGHT_COLUMN_WIDTH-25,200,150);
        JTextField AI_Text = createJTextField(120);
        JTextField AI_number_answers = createJTextField(160);
        JTextField AI_number_question = createJTextField(200);

        this.add(AILabel);
        this.add(AI_number_Answer);
        this.add(AI_number_quest);
        this.add(AI_Text);
        this.add(AI_number_answers);
        this.add(AI_number_question);

        this.setVisible(true);
    }
}
