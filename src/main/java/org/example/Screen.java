package org.example;

import javax.swing.*;
import java.awt.*;

public class Screen extends JFrame{

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final Font TITLE_FONT = new Font ("Arial", Font.PLAIN, 15);
    public static final Font TEXT_FONT = new Font ("Arial", Font.PLAIN, 18);
    public static final int RIGHT_COLUMN_WIDTH = SCREEN_WIDTH-150;



    public Screen(String name) {
    JFrame screen = new JFrame();
        this.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setTitle(name);
        this.setLayout(null);
    }

    public static JLabel createLabel(String text,int x,int y,int width) {
        JLabel label = new JLabel(text);
        label.setBounds(x,y,width, 40);
        label.setFont(TITLE_FONT);
        label.setForeground(Color.black);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    public static JTextField createJTextField(int y) {
        JTextField field = new JTextField();
        field.setBounds(40,y, 600, 40);
        field.setFont(TEXT_FONT);
        field.setHorizontalAlignment(SwingConstants.RIGHT);
        return field;
    }
}
