package org.example;

import javax.swing.*;
import java.awt.*;
import java.net.http.WebSocket;

public class Main {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final Font TITLE_FONT = new Font ("Arial", Font.PLAIN, 20);
    public static final Font TEXT_FONT = new Font ("Arial", Font.PLAIN, 18);
    public static void main(String[] args) {
        //ChatQuery newchatqwe=new ChatQuery("תן לי סקר לגבי קניית כלבים ולא אימוץ","1","2");


        JFrame mainScreen = new JFrame();
        mainScreen.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        mainScreen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainScreen.setLocationRelativeTo(null);
        mainScreen.setResizable(false);
        mainScreen.setLayout(null);

        JLabel surveySubjectLabel = new JLabel("נושא הסקר");
        surveySubjectLabel.setBackground(Color.black);
        surveySubjectLabel.setFont(TITLE_FONT);
        surveySubjectLabel.setBounds(SCREEN_WIDTH -150 ,40,150,40);
        JTextField surveySubject = new JTextField();
        surveySubject.setFont(TEXT_FONT);
        surveySubject.setBounds(SCREEN_WIDTH-760 ,40,600,40);

        JCheckBox withAI = new JCheckBox();
        withAI.setBounds(SCREEN_WIDTH - 200 ,80,20,40);
        JLabel withAILabel = new JLabel("הפק את הסקר באמצעות AI");
        withAILabel.setBounds(SCREEN_WIDTH - 450 ,80,250,40);
        withAILabel.setFont(TITLE_FONT);
        withAILabel.setBackground(Color.black);

        JLabel AILabel = new JLabel("תיאור מפורט של הסקר");
        AILabel.setBackground(Color.black);
        AILabel.setBounds(SCREEN_WIDTH-760 ,200,600,40);

        AILabel.setFont(TITLE_FONT);

        JTextField AIText = new JTextField();
        AIText.setFont(TEXT_FONT);
        AIText.setBounds(SCREEN_WIDTH-760 ,200,600,40);

        withAI.addActionListener(e-> {
              if (withAI.isSelected()) {
                mainScreen.add(AILabel);
                mainScreen.add(AIText);
            }
              else {
                  mainScreen.remove(AILabel);
                  mainScreen.remove(AIText);

              }
              mainScreen.revalidate();
              mainScreen.repaint();
            });




        mainScreen.add(surveySubjectLabel);
        mainScreen.add(surveySubject);
        mainScreen.add(withAI);
        mainScreen.add(withAILabel);
        mainScreen.setVisible(true);


    }
}
