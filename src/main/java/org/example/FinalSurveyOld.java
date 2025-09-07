package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;

import javax.swing.*;
import java.awt.*;


public class FinalSurveyOld extends JFrame {
    public static final int SCREEN_WIDTH = 600;
    public static final int SCREEN_HEIGHT = 1000;
    public static final Font TITLE_FONT = new Font ("Arial", Font.PLAIN, 15);
    public static final Font TEXT_FONT = new Font ("Arial", Font.PLAIN, 18);
    private static int Rows_Counter=1;
    public static Survey survey;
    public static TelegramBotsApi telegramBotsApi;
    public FinalSurveyOld() {
        this.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(null);
        JLabel title =createLabel(survey.getTitle(),0,0,SCREEN_WIDTH);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(title);
        JLabel[] question =new JLabel[survey.getQuestions().size()];
        for(int i=0;i<question.length;i++) {
            JLabel[] answers = new JLabel[survey.getQuestions().get(i).getAnswers().size()];
            question[i]=createLabel(survey.getQuestions().get(i).getQuestion(), 10, Rows_Counter * 30, SCREEN_WIDTH - 40);
            Rows_Counter++;
            for(int j=0;j<survey.getQuestions().get(i).getAnswers().size();j++) {
                answers[j]= createLabel(survey.getQuestions().get(i).getAnswers().get(j), 10, Rows_Counter*30, SCREEN_WIDTH -100);
                if(!answers[j].getText().isEmpty()) {
                Rows_Counter++;
                this.add(answers[j]);
            }
            }
            Rows_Counter++;

            this.add(question[i]);
        }
        JButton to_publish=new JButton("פרסם");
        to_publish.setFont(TEXT_FONT);
        to_publish.setHorizontalAlignment(SwingConstants.CENTER);
        to_publish.setBounds(SCREEN_WIDTH/2-75,Rows_Counter*30,150,50);
        to_publish.addActionListener(e -> {
            Bot.getInstance().send_Poll(survey);
            Rows_Counter=1;
            this.dispose();

        });
        this.add(to_publish);
        this.setSize(SCREEN_WIDTH,Rows_Counter*30+125);
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
