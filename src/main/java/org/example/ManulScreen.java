package org.example;

import javax.swing.*;

public class ManulScreen extends Screen {


    public ManulScreen(){
        super("Manul Poll" );

        JLabel surveySubjectLabel = createLabel("נושא הסקר", RIGHT_COLUMN_WIDTH, 40, 100);
        this.add(surveySubjectLabel);

         JTextField surveySubject = createJTextField(40);
        this.add(surveySubject);

        JLabel question_label = createLabel("השאלה לסקר",RIGHT_COLUMN_WIDTH-25,120,150);
        this.add(question_label);
        JLabel[] question_answers = new JLabel[4];
        for(int i=0;i<4;i++) {
            question_answers[i]=createLabel("תשובה "+(i+1), RIGHT_COLUMN_WIDTH - 25, 160+40*i, 150);
            this.add(question_answers[i]);

        }
        JTextField question_Text = createJTextField(120);
        this.add(question_Text);
        JTextField []question_answers_text=new JTextField[4];
        for(int i=0;i<4;i++) {
            question_answers_text[i]=createJTextField(160+i*40);
            this.add(question_answers_text[i]);
        }
        this.setVisible(true);
    }
}
