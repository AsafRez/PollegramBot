package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainScreen extends Screen {

    public static int Users_from_Bot = 0;
    private JTable surveyTable;

    public static JComboBox<String> surveyCombo = new JComboBox<>();

    public MainScreen() {
        super("Main_Screen");
        HashMap<String, List<Survey>> entered_subjects = new HashMap<>();

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JButton manualButton = new JButton();
        manualButton.setForeground(Color.black);
        manualButton.setFont(TITLE_FONT);
        manualButton.setBounds(SCREEN_WIDTH - 350, 50, 150, 40);
        manualButton.setText("סקר ידני");
        this.add(manualButton);
        manualButton.addActionListener(e -> {
            new ManulScreen();
        });


        JButton AiButton = new JButton();
        AiButton.setForeground(Color.black);
        AiButton.setFont(TITLE_FONT);
        AiButton.setBounds(200, manualButton.getY(), 150, 40);
        AiButton.setText("סקר באמצעות AI");
        this.add(AiButton);
        AiButton.addActionListener(e -> {
            new AIScreen();
        });


        JLabel comboLabel = new JLabel();
        comboLabel.setText("סטטיסטיקת סקרים:");
        comboLabel.setForeground(Color.black);
        comboLabel.setFont(TITLE_FONT);
        comboLabel.setBounds(SCREEN_WIDTH - 200, 125, 150, 30);
        this.add(comboLabel);

//        JComboBox<String> surveyCombo = new JComboBox<>();
        surveyCombo.setFont(TEXT_FONT);
        surveyCombo.setBounds(50, 125, 500, 30);
        if (Bot.getInstance().getSurveys().isEmpty()) {
            surveyCombo.addItem("אין סקרים פעילים");
        } else {
            for (Survey s : Bot.getInstance().getSurveys()) {
                surveyCombo.addItem(s.getTitle());
            }
            System.out.println(surveyCombo);
        }
            this.add(surveyCombo);

            JLabel count_Users = Screen.createLabel("מספר משתמשים: " + Users_from_Bot, 200, SCREEN_HEIGHT - 100, 150);
            count_Users.setAlignmentX(Component.LEFT_ALIGNMENT);
            this.add(count_Users);

            new Thread(() -> {
                try {
                    while (true) {
                        SwingUtilities.invokeLater(() -> {
                            // עדכון התווית של המשתמשים
                            count_Users.setText(("מספר משתמשים: " + Users_from_Bot));
                        });

                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            this.setVisible(true);
            this.revalidate();
            this.repaint();
    }
}

