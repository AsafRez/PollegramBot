package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainScreen extends Screen {

    public static int Users_from_Bot = 0;
    private JTable surveyTable;

    public MainScreen() {
        super("Main_IR Screen");
        HashMap<String, List<Survey>> entered_subjects = new HashMap<>();

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.setVisible(true);
        JButton manualButton = new JButton();
        manualButton.setForeground(Color.black);
        manualButton.setFont(TITLE_FONT);
        manualButton.setBounds(SCREEN_WIDTH - 350, 50, 150, 40);
        manualButton.setText("סקר ידני");
        this.add(manualButton);
        manualButton.addActionListener(e -> { new ManulScreen();
        });


        JButton AiButton = new JButton();
        AiButton.setForeground(Color.black);
        AiButton.setFont(TITLE_FONT);
        AiButton.setBounds(200, manualButton.getY(), 150, 40);
        AiButton.setText("סקר באמצעות AI");
        this.add(AiButton);
        AiButton.addActionListener(e -> { new AIScreen();
        });


        JLabel count_Users =Screen.createLabel("מספר משתמשים: "+Users_from_Bot,200,SCREEN_HEIGHT-100,150);
        count_Users.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(count_Users);
       new Thread(() -> {
           try {
               Thread.sleep(1000);
           while (true) {
               count_Users.setText(("מספר משתמשים: "+Users_from_Bot));
               this.revalidate();
               this.repaint();
           }

           }catch(InterruptedException e){
               e.printStackTrace();
           }

       }).start();

//        JTable polls = new JTable();
//        polls.addRowSelectionInterval(100,500);
//        polls.setBounds(100,100,500,500);
//        polls.setGridColor(Color.black);
//        List<Survey> surveyList = new ArrayList<>(Bot.getInstance().getSurveys());
//
//        SurveyTableModel model = new SurveyTableModel(surveyList);
//        surveyTable = new JTable(model);
//
//        add(new JScrollPane(surveyTable), BorderLayout.CENTER);
//        this.add(polls);
    }
}
