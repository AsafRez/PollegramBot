package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainScreen extends Screen {

    public static int Users_from_Bot = 0;
    public static JComboBox<String> surveyCombo = new JComboBox<>();
    private JComboBox<String> questionCombo = new JComboBox<>();
    private JPanel graphContainer;
    private GraphPanel graphPanel;

    public MainScreen() {
        super("Main_Screen");

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // --- אתחול והוספת רכיבי ממשק משתמש קבועים (כפתורים ותוויות) ---
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

        JLabel comboLabel = new JLabel("סטטיסטיקת סקרים:");
        comboLabel.setForeground(Color.black);
        comboLabel.setFont(TITLE_FONT);
        comboLabel.setBounds(SCREEN_WIDTH - 200, 125, 150, 30);
        this.add(comboLabel);

        // --- אתחול והוספת ה-JComboBox-ים ---
        surveyCombo.setFont(TEXT_FONT);
        surveyCombo.setBounds(50, 125, 500, 30);
        this.add(surveyCombo);

        JLabel questionLabel = new JLabel("בחר שאלה:");
        questionLabel.setForeground(Color.black);
        questionLabel.setFont(TITLE_FONT);
        questionLabel.setBounds(SCREEN_WIDTH - 200, 175, 150, 30);
        this.add(questionLabel);

        questionCombo.setFont(TEXT_FONT);
        questionCombo.setBounds(50, 175, 500, 30);
        this.add(questionCombo);

        // --- אתחול והוספת פאנל הגרף ---
        this.graphContainer = new JPanel(new BorderLayout());
        this.graphContainer.setBounds(50, 225, 500, 275);
        this.add(this.graphContainer);

        this.graphPanel = new GraphPanel();
        this.graphContainer.add(this.graphPanel, BorderLayout.CENTER);

        // --- הוספת מאזינים לרכיבים (פעם אחת בלבד) ---
        surveyCombo.addActionListener(e -> {
            updateQuestionCombo();
        });

        questionCombo.addActionListener(e -> {
            displayQuestionStats();
        });

        // --- עדכון ראשוני של נתוני הסקרים והשאלות ---
        if (Bot.getInstance().getSurveys().isEmpty()) {
            surveyCombo.addItem("אין סקרים פעילים");
        } else {
            for (Survey s : Bot.getInstance().getSurveys()) {
                surveyCombo.addItem(s.getTitle());
            }
        }

        updateQuestionCombo(); // קריאה ראשונית לאחר שכל הרכיבים קיימים

        // --- קוד קיים עבור מונה משתמשים וכו' ---
        JLabel count_Users = Screen.createLabel("מספר משתמשים: " + Users_from_Bot, 200, SCREEN_HEIGHT - 100, 150);
        count_Users.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(count_Users);

        new Thread(() -> {
            try {
                while (true) {
                    // השתמש ב-SwingUtilities.invokeLater() כדי לעדכן את ה-GUI באופן בטוח
                    SwingUtilities.invokeLater(() -> {
                        // עדכון התווית של המשתמשים
                        count_Users.setText(("מספר משתמשים: " + Users_from_Bot));

                        // קבל את הסקר והשאלה הנבחרים כרגע
                        String selectedSurveyTitle = (String) surveyCombo.getSelectedItem();
                        String selectedQuestionText = (String) questionCombo.getSelectedItem();

                        // בצע עדכון רק אם יש סקר ושאלות נבחרים
                        if (selectedSurveyTitle != null && selectedQuestionText != null &&
                                !selectedSurveyTitle.equals("אין סקרים פעילים") &&
                                !selectedQuestionText.equals("אין שאלות זמינות")) {

                            Survey selectedSurvey = findSurveyByTitle(selectedSurveyTitle);
                            if (selectedSurvey != null) {
                                for (Question q : selectedSurvey.getQuestions()) {
                                    if (q.getQuestion().equals(selectedQuestionText)) {
                                        graphPanel.updateGraphData(q);
                                        graphPanel.repaint();
                                        break;
                                    }
                                }
                            }
                        }
                    });

                    Thread.sleep(1000); // המתן שנייה לפני הריענון הבא
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

//        new Thread(() -> {
//            try {
//                while (true) {
//                    SwingUtilities.invokeLater(() -> {
//                        count_Users.setText(("מספר משתמשים: " + Users_from_Bot));
//                    });
//                    Thread.sleep(1000);
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();

        this.setVisible(true);
        this.revalidate();
        this.repaint();
    }


    private void updateQuestionCombo() {
        questionCombo.removeAllItems();
        String selectedSurveyTitle = (String) surveyCombo.getSelectedItem();

        if (selectedSurveyTitle != null && !selectedSurveyTitle.equals("אין סקרים פעילים")) {
            Survey selectedSurvey = findSurveyByTitle(selectedSurveyTitle);
            if (selectedSurvey != null) {
                for (Question question : selectedSurvey.getQuestions()) {
                    questionCombo.addItem(question.getQuestion());
                }
            }
        } else {
            questionCombo.addItem("אין שאלות זמינות");
        }
        displayQuestionStats();
    }

    private void displayQuestionStats() {
        String selectedSurveyTitle = (String) surveyCombo.getSelectedItem();
        String selectedQuestionText = (String) questionCombo.getSelectedItem();

        if (selectedSurveyTitle == null || selectedQuestionText == null ||
                selectedSurveyTitle.equals("אין סקרים פעילים") ||
                selectedQuestionText.equals("אין שאלות זמינות")) {
            graphPanel.updateGraphData(new Question());
            return;
        }
        Survey selectedSurvey = findSurveyByTitle(selectedSurveyTitle);
        if (selectedSurvey == null) {
            return;
        }
        for (Question q : selectedSurvey.getQuestions()) {
            if (q.getQuestion().equals(selectedQuestionText)) {
                // העברת הנתונים הנכונים לפאנל הגרף
                graphPanel.updateGraphData(q);
                return;
            }
        }
    }

    // מתודה למציאת סקר לפי כותרת
    private Survey findSurveyByTitle(String title) {
        for (Survey survey : Bot.getInstance().getSurveys()) {
            if (survey.getTitle().equals(title)) {
                return survey;
            }
        }
        return null;
    }
}


