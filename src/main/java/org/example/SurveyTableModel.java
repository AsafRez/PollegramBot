package org.example;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class SurveyTableModel extends AbstractTableModel {

    private final List<Survey> surveys;
    private final String[] columnNames = {"Survey Name", "Questions Count"};

    public SurveyTableModel(List<Survey> surveys) {
        this.surveys = surveys;
    }

    @Override
    public int getRowCount() {
        return surveys.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Survey survey = surveys.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return survey.getTitle(); // צריך מתודה getName()
            case 1:
                return survey.getQuestions().size();
            default:
                return "";
        }
    }

}
