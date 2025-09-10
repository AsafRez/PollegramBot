package org.example;

import javax.swing.*;
import java.awt.*;

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

        JButton Submit = new JButton("הפק סקר");
        Submit.setBounds(SCREEN_WIDTH / 4, 2 * SCREEN_HEIGHT / 3, 150, 25);
        Submit.setFont(TEXT_FONT);
        Submit.setForeground(Color.BLACK);
        Submit.addActionListener(e -> {
            if (!surveySubject.getText().isEmpty() && !AI_Text.getText().isEmpty()) {
                FinalSurvey.survey = (ChatQuery.generate_ChatPoll(surveySubject.getText(), AI_Text.getText(), AI_number_question.getText(), AI_number_answers.getText()));
                FinalSurvey finalSurvey = new FinalSurvey();
                finalSurvey.setVisible(true);
                this.dispose(); // שימוש ב-this
            } else {
                JOptionPane.showMessageDialog(
                        this, // שימוש ב-this
                        "יש למלא את כל השדות", // הודעת השגיאה
                        "שגיאת קלט", // כותרת החלון
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        this.add(Submit);
        this.setVisible(true);
    }
}
