package org.example.Views;

import org.example.Controllers.SettingPageFormListener;
import org.example.Helper.ConstantString;
import org.example.Helper.GlobalFunction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SettingPageForm {
    private JTabbedPane tabbedPane;
    private JPanel SettingPageForm;
    private JPanel AddNewWordPanel;
    private JPanel AboutPanel;
    private JRadioButton vERadioButton;
    private JRadioButton eVRadioButton;
    private JTextField tfInputWord;
    private JButton btnSend;
    private JEditorPane DisplayWordArea;
    private JButton btnSave;
    private JComboBox comboBox_time_save;
    private JButton btn_submit;

    public JPanel getSettingPageForm() {
        return SettingPageForm;
    }

    public SettingPageForm() {
        initComponent();
        initListener();
        comboBox_time_save.setSelectedIndex(1);
    }

    public JComboBox getComboBox_time_save() {
        return comboBox_time_save;
    }

    public JButton getBtn_submit() {
        return btn_submit;
    }

    private void initComponent() {
        GlobalFunction.setButtonIcon(btnSend, ConstantString.IC_SEND, 32, 32);
        vERadioButton.setSelected(true);
        comboBox_time_save.addItem("1 minute");
        comboBox_time_save.addItem("5 minutes");
        comboBox_time_save.addItem("10 minutes");
    }

    private void initListener() {
        ActionListener settingPageFormListener = new SettingPageFormListener(this);
        btnSend.addActionListener(settingPageFormListener);
        btnSave.addActionListener(settingPageFormListener);
        eVRadioButton.addActionListener(settingPageFormListener);
        vERadioButton.addActionListener(settingPageFormListener);
        comboBox_time_save.addActionListener(settingPageFormListener);
    }

    public JButton getBtnSend() {
        return btnSend;
    }

    public JButton getBtnSave() {
        return btnSave;
    }

    public JEditorPane getDisplayWordArea() {
        return DisplayWordArea;
    }

    public JTextField getTfInputWord() {
        return tfInputWord;
    }

    public JRadioButton getvERadioButton() {
        return vERadioButton;
    }

    public JRadioButton geteVRadioButton() {
        return eVRadioButton;
    }

    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this.SettingPageForm, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this.SettingPageForm, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
