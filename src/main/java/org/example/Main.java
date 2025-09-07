package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final Font TITLE_FONT = new Font ("Arial", Font.PLAIN, 15);
    public static final Font TEXT_FONT = new Font ("Arial", Font.PLAIN, 18);
    public static final int RIGHT_COLUMN_WIDTH = SCREEN_WIDTH-150;
    private static ArrayList<Component> AI_component=null;
    private static ArrayList<Component> manual_component=null;
    private static int manual_survey_counter =0;
    private static Survey manual_survey=null;
    private static JFrame mainScreen;
    private static List<JTextField> question_answers_text;
    private static List<JLabel> question_answers;

    public static void main(String[] args) {
        manual_component = new ArrayList<>();
        mainScreen = new JFrame();
        mainScreen.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        mainScreen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainScreen.setLocationRelativeTo(null);
        mainScreen.setResizable(false);
        mainScreen.setLayout(null);
        //JLabel
        JLabel surveySubjectLabel = createLabel("נושא הסקר", RIGHT_COLUMN_WIDTH, 40, 100);
        HashMap<String, List<Survey>> entered_subjects = new HashMap<>();
        //JTexts
        JTextField surveySubject = createJTextField(40);


        //Manual looks
        JLabel question_label = createLabel("השאלה לסקר", RIGHT_COLUMN_WIDTH - 25, 120, 150);
        mainScreen.add(question_label);
        manual_component.add(question_label);
        question_answers = new ArrayList<>();
        JTextField question_Text = createJTextField(120);
        manual_component.add(question_Text);
        mainScreen.add(question_Text);
        question_answers_text = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            question_answers.add(createLabel("תשובה " + (i + 1), RIGHT_COLUMN_WIDTH - 25, 160 + (40 * i), 150));
            mainScreen.add(question_answers.getLast());
            manual_component.add(question_answers.getLast());
            question_answers_text.add(createJTextField(160 + (i* 40)));
            mainScreen.add(question_answers_text.getLast());
            manual_component.add(question_answers_text.getLast());
        }
        JButton add_answer = new JButton("הוסף תשובה");
        add_answer.setBounds((SCREEN_WIDTH / 2) + 150, 2 * SCREEN_HEIGHT / 3, 150, 25);
        add_answer.setFont(TEXT_FONT);
        add_answer.setForeground(Color.BLACK);
        mainScreen.add(add_answer);
        manual_component.add(add_answer);
        add_answer.addActionListener(e -> {
            if(question_answers.size()<4 && !question_answers_text.getFirst().getText().isEmpty()&&!question_answers_text.get(1).getText().isEmpty()) {
                question_answers.add(createLabel("תשובה "+(question_answers.size()+1), RIGHT_COLUMN_WIDTH - 25, 160 + 40 * (question_answers_text.size()), 150));
                question_answers_text.add(createJTextField(160 + (question_answers_text.size()) * 40));
                mainScreen.add(question_answers_text.getLast());
                manual_component.add(question_answers_text.getLast());
                mainScreen.add(question_answers.get(question_answers_text.size() - 1));
                manual_component.add(question_answers.get(question_answers_text.size() - 1));

                mainScreen.validate();
                mainScreen.repaint();
            }
        });

        //Chat looks
        AI_component=new ArrayList<>();
        JLabel AILabel = createLabel("תיאור מפורט של הסקר",RIGHT_COLUMN_WIDTH-25,120,150);
        JLabel AI_number_Answer = createLabel("מספר תשובות",RIGHT_COLUMN_WIDTH-25,160,150);
        JLabel AI_number_quest = createLabel("מספר שאלות",RIGHT_COLUMN_WIDTH-25,200,150);
        AI_component.add(AI_number_quest);
        AI_component.add(AI_number_Answer);
        AI_component.add(AILabel);
        JTextField AI_Text = createJTextField(120);
        JTextField AI_number_answers = createJTextField(160);
        JTextField AI_number_question = createJTextField(200);
        AI_component.add(AI_Text);
        AI_component.add(AI_number_answers);
        AI_component.add(AI_number_question);


        JCheckBox withAI = new JCheckBox();
        withAI.setFont(TITLE_FONT);
        withAI.setBounds(SCREEN_WIDTH - 200 ,80,50,40);
        withAI.setForeground(Color.BLACK);
        withAI.setText("AI");


            JButton Submit = new JButton("הפק סקר");
            Submit.setBounds(SCREEN_WIDTH / 4, 2 * SCREEN_HEIGHT / 3, 150, 25);
            Submit.setFont(TEXT_FONT);
            Submit.setForeground(Color.BLACK);
            Submit.addActionListener(e -> {
                System.out.println(question_Text.getText());
                if (!surveySubject.getText().isEmpty() && !AI_Text.getText().isEmpty()) {
                if (withAI.isSelected()) {
                        FinalSurvey.survey = ChatQuery.generate_ChatPoll(surveySubject.getText(), AI_Text.getText(), AI_number_question.getText(), AI_number_answers.getText());
                    } else if(manual_survey_counter!=0||!question_Text.getText().isEmpty()) {
                        manual_survey.setTitle(surveySubject.getText());
                        FinalSurvey.survey = manual_survey;
                    }
                    FinalSurvey finalSurvey = new FinalSurvey();
                    finalSurvey.setVisible(true);
                    mainScreen.dispose();
                }
                else{
                    JOptionPane.showMessageDialog(
                            mainScreen, // ההורה של הדיאלוג (ה-JFrame במקרה זה)
                            "יש למלא את כל השדות", // הודעת השגיאה
                            "שגיאת קלט", // כותרת החלון
                            JOptionPane.ERROR_MESSAGE);
                }

            });

            JButton addQuestion = new JButton("הוסף שאלה");
            addQuestion.setBounds(SCREEN_WIDTH / 2, 2 * SCREEN_HEIGHT / 3, 150, 25);
            addQuestion.setFont(TEXT_FONT);
            addQuestion.setForeground(Color.BLACK);
            addQuestion.addActionListener(e -> {
                if (!surveySubject.getText().isEmpty() && !question_Text.getText().isEmpty()) {
                    if (manual_survey_counter == 0) {
                        manual_survey = new Survey();
                    }
                    if (manual_survey_counter < 3) {
                        manual_survey.addQuestion(new Question(question_Text.getText(), List.of(question_answers_text.getFirst().getText(), question_answers_text.get(1).getText(), question_answers_text.get(2).getText(), question_answers_text.get(3).getText())));
                        manual_survey_counter++;
                        for (int i = 0; i < 4; i++) {
                            question_answers_text.get(i).setText("");
                        }
                        question_Text.setText("");

                    }
                    if (manual_survey_counter != 0 && manual_survey_counter < 3) {
                        mainScreen.add(Submit);
                        mainScreen.revalidate();
                        mainScreen.repaint();
                    } else {
                        mainScreen.remove(addQuestion);
                        mainScreen.revalidate();
                        mainScreen.repaint();
                    }
                } else{
                    JOptionPane.showMessageDialog(
                            mainScreen, // ההורה של הדיאלוג (ה-JFrame במקרה זה)
                            "יש למלא את כל השדות (מינימום 2 תשובות)", // הודעת השגיאה
                            "שגיאת קלט", // כותרת החלון
                            JOptionPane.ERROR_MESSAGE);
                }
            });


        mainScreen.add(addQuestion);
        withAI.addActionListener(e-> {
              if (withAI.isSelected()) {
                  mainScreen.add(Submit);
                  for (Component component : AI_component) {
                      mainScreen.add(component);
                  }
                    mainScreen.remove(addQuestion);
                  for (Component component : manual_component) {
                      mainScreen.remove(component);
                  }
            }
              else if(AI_component!=null) {
                  if (manual_survey_counter <3) {
                      mainScreen.add(addQuestion);
                  }
                  mainScreen.add(addQuestion);
                  if (manual_survey_counter !=0) {
                      mainScreen.add(Submit);
                  }
                  for (Component component : AI_component) {
                      mainScreen.remove(component);
                  }
                  for (Component component : manual_component) {
                      mainScreen.add(component);
                  }
              }
              mainScreen.revalidate();
              mainScreen.repaint();
            });


        mainScreen.add(surveySubjectLabel);
        mainScreen.add(surveySubject);
        mainScreen.add(withAI);
        mainScreen.setVisible(true);
    }
    private static JTextField createJTextField(int y) {
        JTextField field = new JTextField();
        field.setBounds(40,y, 600, 40);
        field.setFont(TEXT_FONT);
        field.setHorizontalAlignment(SwingConstants.RIGHT);
        return field;
    }
    private static JLabel createLabel(String text,int x,int y,int width) {
        JLabel label = new JLabel(text);
        label.setBounds(x,y,width, 40);
        label.setFont(TITLE_FONT);
        label.setForeground(Color.black);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

}
