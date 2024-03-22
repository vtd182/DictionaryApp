package org.example.Models;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Map;

public class WordFrequencyTableModel extends AbstractTableModel {
    private final Map<String, Integer> wordFrequencyMap;
    private final String[] columnNames = {"Word", "Frequency"};
    private final Object[][] data;

    public WordFrequencyTableModel(Map<String, Integer> wordFrequencyMap) {
        this.wordFrequencyMap = wordFrequencyMap;
        this.data = wordFrequencyMap.entrySet().stream()
                .map(e -> new Object[]{e.getKey(), e.getValue()})
                .toArray(Object[][]::new);
    }

    @Override
    public int getRowCount() {
        return wordFrequencyMap.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }
}
