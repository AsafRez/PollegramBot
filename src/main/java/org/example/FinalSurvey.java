package org.example;

import javax.swing.*;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FinalSurvey extends Screen{

    private static int Rows_Counter=1;
    public static Survey survey;
    private ScheduledExecutorService delay_timer = Executors.newSingleThreadScheduledExecutor();


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

        JLabel delay_label =createLabel("פרסום בעיכוב",SCREEN_WIDTH/2,Rows_Counter*30,150);
        JTextField delay=new JTextField(2);
//        ((AbstractDocument) delay.getDocument()).setDocumentFilter(new NumericFilter());
        delay.setText("0");
        delay.setBounds(SCREEN_WIDTH/2-75, Rows_Counter*30, 150, 20);
        this.add(delay_label);
        this.add(delay);

        JButton to_publish=new JButton("פרסם");
        to_publish.setFont(TEXT_FONT);
        to_publish.setHorizontalAlignment(SwingConstants.CENTER);
        to_publish.setBounds(SCREEN_WIDTH/2-75,delay.getY()+30,150,50);
        to_publish.addActionListener(e -> {
            if (Objects.equals(delay.getText(), "0")){
                if(send_toBot()) {
                    Rows_Counter = 1;
                    this.dispose();
                    JOptionPane.showMessageDialog(
                            this, // שימוש ב-this
                            "הסקר יפורסם עכשיו.", // הודעת השגיאה
                            "שליחה", // כותרת החלון
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }else {
                try {
                    // 1. נסה להמיר את הטקסט למספר שלם
                    int delayValue = Integer.parseInt(delay.getText());

                    // 2. בדוק אם המספר בטווח הרצוי (1-99)
                    if (delayValue > 0 && delayValue <= 99) {
                        delay_timer.schedule(this::send_toBot, Integer.parseInt(delay.getText()), TimeUnit.MINUTES);

                        // מקרה הצלחה
                        Rows_Counter = 1;
                        this.dispose();
                        JOptionPane.showMessageDialog(
                                this,
                                "הסקר יפורסם בעוד " + delayValue + " דקות.",
                                "שליחה בהשהייה",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // המספר הוא 0, שלילי, או מעל 99
                        JOptionPane.showMessageDialog(
                                this,
                                "יש להכניס ערך מספרי בין 1 ל-99.",
                                "ערך מחוץ לטווח",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (NumberFormatException s) {
                    // 3. אם ההמרה נכשלה (הטקסט לא היה מספר)
                    JOptionPane.showMessageDialog(
                            this,
                            "הכנס רק מספרים שלמים (לדוגמה: 5, 30).",
                            "קלט לא חוקי",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        this.add(to_publish);
        this.setSize(SCREEN_WIDTH,Rows_Counter*30+125);
    }
    private boolean send_toBot(){
        if (MainScreen.Users_from_Bot < 3) {
            JOptionPane.showMessageDialog(
                    this, // שימוש ב-this
                    "קהילה קטנה מידי :(", // הודעת השגיאה
                    "שגיאה", // כותרת החלון
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (Bot.getInstance().getActiveSurvey() == null || Bot.getInstance().getActiveSurvey().isClosed()) {
            Bot.getInstance().send_Poll(survey);
            return true;
        } else {
            JOptionPane.showMessageDialog(
                    this, // שימוש ב-this
                    "כבר יש סקר פעיל, לא ניתן לפתוח סקרים במקביל", // הודעת השגיאה
                    "שגיאה", // כותרת החלון
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }
}
