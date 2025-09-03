package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final Font TITLE_FONT = new Font ("Arial", Font.PLAIN, 15);
    public static final Font TEXT_FONT = new Font ("Arial", Font.PLAIN, 18);
    public static final int RIGHT_COLUMN_WIDTH = SCREEN_WIDTH-150;
    private static ArrayList<Component> AI_component=null;
    private static ArrayList<Component> manual_component=null;
    private static Survey manual_survey;
    private static int manual_survey_counter =0;

    public static void main(String[] args) {
        manual_survey=new Survey();
        manual_component=new ArrayList<>();
        JFrame mainScreen = new JFrame();
        mainScreen.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        mainScreen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainScreen.setLocationRelativeTo(null);
        mainScreen.setResizable(false);
        mainScreen.setLayout(null);
        //JLabel
        JLabel surveySubjectLabel =createLabel("נושא הסקר",RIGHT_COLUMN_WIDTH,40,100);

        //JTexts
        JTextField surveySubject = createJTextField(40);


        //Manual looks
        JLabel question_label = createLabel("השאלה לסקר",RIGHT_COLUMN_WIDTH-25,120,150);
        mainScreen.add(question_label);
        manual_component.add(question_label);
        JLabel[] question_answers = new JLabel[4];
        for(int i=0;i<4;i++) {
            question_answers[i]=createLabel("תשובה "+(i+1), RIGHT_COLUMN_WIDTH - 25, 160+40*i, 150);
            mainScreen.add(question_answers[i]);
            manual_component.add(question_answers[i]);

       }
        JTextField question_Text = createJTextField(120);
        manual_component.add(question_Text);
        mainScreen.add(question_Text);
        JTextField []question_answers_text=new JTextField[4];
        for(int i=0;i<4;i++) {
            question_answers_text[i]=createJTextField(160+i*40);
            mainScreen.add(question_answers_text[i]);
            manual_component.add(question_answers_text[i]);
        }
        //Chat looks
        AI_component=new ArrayList<>();
        JLabel AILabel = createLabel("תיאור מפורט של הסקר",RIGHT_COLUMN_WIDTH-25,120,150);
        JLabel AI_number_Answer = createLabel("מספר תשובות",RIGHT_COLUMN_WIDTH-25,160,150);
        JLabel AI_number_quest = createLabel("מספר שאלות",RIGHT_COLUMN_WIDTH-25,200,150);
        mainScreen.add(AI_number_quest);
        AI_component.add(AI_number_quest);
        mainScreen.add(AI_number_Answer);
        AI_component.add(AI_number_Answer);
        mainScreen.add(AILabel);
        AI_component.add(AILabel);
        JTextField AI_Text = createJTextField(120);
        JTextField AI_number_answers = createJTextField(160);
        JTextField AI_number_question = createJTextField(200);
        mainScreen.add(AI_Text);
        AI_component.add(AI_Text);
        mainScreen.add(AI_number_answers);
        AI_component.add(AI_number_answers);
        mainScreen.add(AI_number_question);
        AI_component.add(AI_number_question);


        JCheckBox withAI = new JCheckBox();
        withAI.setFont(TITLE_FONT);
        withAI.setBounds(SCREEN_WIDTH - 200 ,80,50,40);
        withAI.setForeground(Color.BLACK);
        withAI.setText("AI");

        JButton submit = new JButton("החל");
        submit.setBounds(SCREEN_WIDTH/4,2*SCREEN_HEIGHT/3,150,25);
        submit.setFont(TEXT_FONT);
        submit.setForeground(Color.BLACK);
        submit.addActionListener(e -> {
                    if (withAI.isSelected()) {
                        ChatQuery newchatqwe = new ChatQuery(AI_Text.getText(),AI_number_question.getText(), AI_number_answers.getText());
                    } else {
                        if (manual_survey_counter < 3) {
                            manual_survey.addQuestion(new Question(question_Text.getText(), List.of(question_answers_text[0].getText(),question_answers_text[1].getText(),question_answers_text[2].getText(),question_answers_text[3].getText())));
                                    manual_survey_counter++;}
                    }
                });
        mainScreen.add(submit);

        withAI.addActionListener(e-> {
              if (withAI.isSelected()) {
                  submit.setText("הפק סקר");
                  for (Component component : AI_component) {
                      mainScreen.add(component);
                  }

                  for (Component component : manual_component) {
                      mainScreen.remove(component);
                  }
            }
              else if(AI_component!=null) {
                  manual_survey=new Survey();
                  submit.setText("הוסף שאלה");
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
