package org.example;

import com.sun.tools.javac.Main;
import org.telegram.telegrambots.meta.TelegramBotsApi;

import javax.swing.*;

public class FinalSurvey extends Screen{

    private static int Rows_Counter=1;
    public static Survey survey;
    public static TelegramBotsApi telegramBotsApi;

    public FinalSurvey(){
        super("Final Survey" );

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
            if (Bot.getInstance().getActiveSurvey()==null ||  Bot.getInstance().getActiveSurvey().isClosed()) {
                Bot.getInstance().send_Poll(survey);
                Rows_Counter = 1;
                this.dispose();
            }else {
                JOptionPane.showMessageDialog(
                        this, // שימוש ב-this
                        "כבר יש סקר פעיל, לא ניתן לפתוח סקרים במקביל", // הודעת השגיאה
                        "שגיאה", // כותרת החלון
                        JOptionPane.ERROR_MESSAGE);
            }

        });
        this.add(to_publish);
        this.setSize(SCREEN_WIDTH,Rows_Counter*30+125);
    }

}
