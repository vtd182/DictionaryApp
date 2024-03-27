package org.example.Controllers;

import org.example.Models.DictionaryManager;
import org.example.Views.HistoryPageForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class HistoryPageFormListener implements ActionListener {
    HistoryPageForm historyPageForm;

    private final DictionaryManager dictionaryManager = DictionaryManager.getInstance();
    Vector<Integer> errorList = new Vector<>();

    public HistoryPageFormListener(HistoryPageForm historyPageForm) {
        this.historyPageForm = historyPageForm;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == historyPageForm.getBtn_search()) {
            onSearchButtonClicked();
        } else if (e.getSource() == historyPageForm.geteVRadioButton()) {
            if (historyPageForm.geteVRadioButton().isSelected()) {
                historyPageForm.getvERadioButton().setSelected(false);
            } else {
                historyPageForm.geteVRadioButton().setSelected(true);
            }
            onSearchButtonClicked();
        } else if (e.getSource() == historyPageForm.getvERadioButton()) {
            if (historyPageForm.getvERadioButton().isSelected()) {
                historyPageForm.geteVRadioButton().setSelected(false);
            } else {
                historyPageForm.getvERadioButton().setSelected(true);
            }
            onSearchButtonClicked();
        }
    }

    private void onSearchButtonClicked() {
        if (historyPageForm.getTf_from_date().getText().isEmpty() || historyPageForm.getTf_to_date().getText().isEmpty()) {
            return;
        }

        String fromDateStr = historyPageForm.getTf_from_date().getText();
        String toDateStr = historyPageForm.getTf_to_date().getText();

        LocalDate fromDate = parseDate(fromDateStr);
        LocalDate toDate = parseDate(toDateStr);

        if (fromDate == null || toDate == null) {
            JOptionPane.showMessageDialog(null, "Your input date is invalid", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        validateDate(fromDate, toDate);
        if (errorList.isEmpty()) {
            Map<String, Integer> searchFrequencyInRange = new HashMap<>();
            if (historyPageForm.geteVRadioButton().isSelected()) {
                searchFrequencyInRange = getSearchFrequencyInRange(dictionaryManager.getEnglishToVietnameseSearchFrequencyMap(), fromDate, toDate);
            } else if (historyPageForm.getvERadioButton().isSelected()) {
                searchFrequencyInRange = getSearchFrequencyInRange(dictionaryManager.getVietnameseToEnglishSearchFrequencyMap(), fromDate, toDate);
            }
            historyPageForm.updateWordFrequencyTable(searchFrequencyInRange);
        } else {
            showError();
        }
    }

    private void validateDate(LocalDate fromDate, LocalDate toDate) {
        errorList.clear();
        if (fromDate.isAfter(toDate)) {
            errorList.add(1);
        }
        if (fromDate.isAfter(LocalDate.now())) {
            errorList.add(2);
        }
        if (toDate.isAfter(LocalDate.now())) {
            errorList.add(3);
        }
    }

    private void showError() {
        if (errorList.contains(1)) {
            JOptionPane.showMessageDialog(null, "From date must be before to date", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (errorList.contains(2)) {
            JOptionPane.showMessageDialog(null, "From date must be before current date", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (errorList.contains(3)) {
            JOptionPane.showMessageDialog(null, "To date must be before current date", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Map<String, Integer> getSearchFrequencyInRange(Map<LocalDate, Map<String, Integer>> searchFrequencyMap,
                                                          LocalDate startDate, LocalDate endDate) {
        Map<String, Integer> result = new HashMap<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (searchFrequencyMap.containsKey(date)) {
                Map<String, Integer> wordFrequencyMap = searchFrequencyMap.get(date);
                for (Map.Entry<String, Integer> entry : wordFrequencyMap.entrySet()) {
                    String word = entry.getKey();
                    int frequency = entry.getValue();

                    result.put(word, result.getOrDefault(word, 0) + frequency);
                }
            }
        }
        return result;
    }

    private LocalDate parseDate(String dateStr) {
        try {
            String[] parts = dateStr.split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            return LocalDate.of(year, month, day);
        } catch (Exception e) {
            return null;
        }
    }
}
