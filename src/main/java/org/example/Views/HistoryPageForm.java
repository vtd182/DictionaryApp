package org.example.Views;

import org.example.Controllers.HistoryPageFormListener;
import org.example.Models.WordFrequencyTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.util.Map;

public class HistoryPageForm {
    private JPanel HistoryPageForm;
    private JTable wordTable;
    private JRadioButton vERadioButton;
    private JRadioButton eVRadioButton;
    private JTextField tf_from_date;
    private JTextField tf_to_date;
    private JButton btn_search;

    public JRadioButton getvERadioButton() {
        return vERadioButton;
    }

    public JRadioButton geteVRadioButton() {
        return eVRadioButton;
    }

    public JTextField getTf_from_date() {
        return tf_from_date;
    }

    public JTextField getTf_to_date() {
        return tf_to_date;
    }

    public JButton getBtn_search() {
        return btn_search;
    }

    private void initComponents() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Word");
        model.addColumn("Frequency");
        wordTable.setModel(model);
    }

    private void initListeners() {
        ActionListener historyPageFormListener = new HistoryPageFormListener(this);
        btn_search.addActionListener(historyPageFormListener);
        eVRadioButton.addActionListener(historyPageFormListener);
        vERadioButton.addActionListener(historyPageFormListener);
    }

    public HistoryPageForm() {
        initComponents();
        initListeners();
        vERadioButton.setSelected(true);
    }
    public JPanel getHistoryPageForm() {
        return HistoryPageForm;
    }

    public void updateWordFrequencyTable(Map<String, Integer> wordFrequencyMap) {
        WordFrequencyTableModel model = new WordFrequencyTableModel(wordFrequencyMap);
        AbstractButton wordFrequencyTable;
        wordTable.setModel(model);
    }
}
