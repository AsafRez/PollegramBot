package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ManulScreen extends Screen {

    private int manual_survey_counter=0;
    private Survey manual_survey;
    public ManulScreen() {
        super("Manul Poll");
        manual_survey = new Survey();
        JLabel surveySubjectLabel = createLabel("נושא הסקר", RIGHT_COLUMN_WIDTH, 40, 100);
        this.add(surveySubjectLabel);

        JTextField surveySubject = createJTextField(40);
        this.add(surveySubject);

        JLabel question_label = createLabel("השאלה לסקר", RIGHT_COLUMN_WIDTH - 25, 120, 150);
        this.add(question_label);

        List<JLabel> question_answers = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            question_answers.add(createLabel("תשובה " + (i + 1), RIGHT_COLUMN_WIDTH - 25, 160 + 40 * i, 150));
            this.add(question_answers.get(i));
        }
        JTextField question_Text = createJTextField(120);
        this.add(question_Text);
        List<JTextField> question_answers_text = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            question_answers_text.add(createJTextField(160 + i * 40));
            this.add(question_answers_text.get(i));
        }
        JButton add_answer = new JButton("הוסף תשובה");
        add_answer.setBounds((SCREEN_WIDTH / 2) + 150, 2 * SCREEN_HEIGHT / 3, 150, 25);
        add_answer.setFont(TEXT_FONT);
        add_answer.setForeground(Color.BLACK);
        this.add(add_answer); // שימוש ב-this
        add_answer.addActionListener(e -> {
            if (question_answers.size() < 4 && !question_answers_text.getFirst().getText().isEmpty() && !question_answers_text.get(1).getText().isEmpty()) {
                question_answers.add(createLabel("תשובה " + (question_answers.size() + 1), RIGHT_COLUMN_WIDTH - 25, 160 + 40 * (question_answers_text.size()), 150));
                question_answers_text.add(createJTextField(160 + (question_answers_text.size()) * 40));
                this.add(question_answers_text.getLast());
                this.add(question_answers.get(question_answers_text.size() - 1));
                this.validate();
                this.repaint();
            }
        });
        JButton addQuestion = new JButton("הוסף שאלה");
        addQuestion.setBounds(SCREEN_WIDTH / 2, 2 * SCREEN_HEIGHT / 3, 150, 25);
        addQuestion.setFont(TEXT_FONT);
        addQuestion.setForeground(Color.BLACK);
        JButton Submit = new JButton("הפק סקר");
        Submit.setBounds(SCREEN_WIDTH / 4, 2 * SCREEN_HEIGHT / 3, 150, 25);
        Submit.setFont(TEXT_FONT);
        Submit.setForeground(Color.BLACK);
        Submit.addActionListener(e -> {
            if (!surveySubject.getText().isEmpty() || manual_survey_counter != 0 || !question_Text.getText().isEmpty()) {
                manual_survey.setTitle(surveySubject.getText());
                FinalSurvey.survey = (manual_survey);
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

        addQuestion.addActionListener(e -> {
            if (!surveySubject.getText().isEmpty() && !question_Text.getText().isEmpty()) {

                if (manual_survey_counter == 0) {
                    manual_survey = new Survey();
                }
                if (manual_survey_counter < 3) {
                    List<String> answers=new ArrayList<>();
                    for (int i = 0; i < question_answers_text.size(); i++) {
                        answers.add(question_answers_text.get(i).getText());
                    }
                    manual_survey.addQuestion(new Question(question_Text.getText(), answers));
                    manual_survey_counter++;
                    for (int i = 0; i < question_answers_text.size(); i++) {
                        question_answers_text.get(i).setText("");
                    }
                    question_Text.setText("");

                }
                if (manual_survey_counter != 0 && manual_survey_counter < 3) {
                    this.add(Submit);
                    this.revalidate();
                    this.repaint();
                } else {
                    this.remove(addQuestion);
                    this.revalidate();
                    this.repaint();
                }
            } else {
                JOptionPane.showMessageDialog(
                        this, // שימוש ב-this
                        "יש למלא את כל השדות (מינימום 2 תשובות)", // הודעת השגיאה
                        "שגיאת קלט", // כותרת החלון
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        this.add(addQuestion);
        this.setVisible(true);
    }
}
