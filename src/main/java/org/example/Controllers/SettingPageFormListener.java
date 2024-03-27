package org.example.Controllers;

import org.example.Helper.ConstantString;
import org.example.Models.AutoSaveManager;
import org.example.Models.DictionaryManager;
import org.example.Models.Word;
import org.example.Views.SettingPageForm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingPageFormListener implements ActionListener {
    SettingPageForm settingPageForm;

    public SettingPageFormListener(SettingPageForm settingPageForm) {
        this.settingPageForm = settingPageForm;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == settingPageForm.getBtnSend()) {
            System.out.println("Send button clicked");
            onSendButtonClicked();
        } else if (e.getSource() == settingPageForm.getBtnSave()) {
            System.out.println("Save button clicked");
            onSaveButtonClicked();
        } else if (e.getSource() == settingPageForm.geteVRadioButton()) {
            if (settingPageForm.geteVRadioButton().isSelected()) {
                System.out.println("eV radio button clicked");
                settingPageForm.getvERadioButton().setSelected(false);
            } else {
                settingPageForm.geteVRadioButton().setSelected(true);
            }
        } else if (e.getSource() == settingPageForm.getvERadioButton()) {
            if (settingPageForm.getvERadioButton().isSelected()) {
                System.out.println("vE radio button clicked");
                settingPageForm.geteVRadioButton().setSelected(false);
            } else {
                settingPageForm.getvERadioButton().setSelected(true);
            }
        } else if (e.getSource() == settingPageForm.getComboBox_time_save()) {
            onComboboxChange();
        }
    }

    private void onComboboxChange() {
        String selectedTime = (String) settingPageForm.getComboBox_time_save().getSelectedItem();
        if (selectedTime.equals("5 minutes")) {
            AutoSaveManager.getInstance().setSaveIntervalMinutes(5);
        } else if (selectedTime.equals("10 minutes")) {
            AutoSaveManager.getInstance().setSaveIntervalMinutes(10);
        } else if (selectedTime.equals("1 minute")) {
            AutoSaveManager.getInstance().setSaveIntervalMinutes(1);
        }
    }

    private void onSendButtonClicked() {
        String inputWord = settingPageForm.getTfInputWord().getText();
        if (inputWord.isEmpty()) {
            return;
        }
        String displayWord = "@" + inputWord + " /Pronunciation [optional]/\n" + ConstantString.PATTERN_FOR_WORD_MEANING;
        settingPageForm.getDisplayWordArea().setText(displayWord);
        settingPageForm.getTfInputWord().setText("");
    }

    private void onSaveButtonClicked() {
        String inputWord = settingPageForm.getDisplayWordArea().getText();
        if (inputWord.isEmpty()) {
            return;
        }
        try {
            Word word = Word.parseFromString(inputWord);
            if (settingPageForm.geteVRadioButton().isSelected()) {
                DictionaryManager.getInstance().getEnglishToVietnameseDictionary().put(word.getWord(), word);
            } else {
                DictionaryManager.getInstance().getVietnameseToEnglishDictionary().put(word.getWord(), word);
            }
        } catch (Exception e) {
            settingPageForm.showErrorMessage("Invalid word format");
        }
        settingPageForm.showSuccessMessage("Add word successfully");
        settingPageForm.getDisplayWordArea().setText("");
    }
}
