// בקובץ חדש, לדוגמה, GraphPanel.java

package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GraphPanel extends JPanel {

    private DefaultCategoryDataset dataset;
    private ChartPanel chartPanel;

    public GraphPanel() {
        this.setLayout(new BorderLayout());
        this.dataset = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createBarChart(
                "תוצאות סקר",
                "אפשרויות",
                "מספר הצבעות",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        this.chartPanel = new ChartPanel(chart);
        this.add(chartPanel, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(500, 350));
    }

    public void updateGraphData(Question question) {
        dataset.clear();
        int[] stats = question.getStatistics();
        List<String> answers = question.getAnswers();

        for (int i = 0; i < answers.size(); i++) {
            dataset.addValue(stats[i], "מספר הצבעות", answers.get(i));
        }
    }
}