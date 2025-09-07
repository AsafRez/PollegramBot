package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class MainScreen extends Screen {


    public MainScreen() {
        super("Main Screen");
        HashMap<String, List<Survey>> entered_subjects = new HashMap<>();

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JButton manualButton = new JButton();
        manualButton.setForeground(Color.black);
        manualButton.setFont(TITLE_FONT);
        manualButton.setBounds(SCREEN_WIDTH - 350, 100, 150, 40);
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

    }
}
