package org.example.Views;

import org.example.Controllers.SettingPageFormListener;

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

    public JPanel getSettingPageForm() {
        return SettingPageForm;
    }

    public SettingPageForm() {
        initComponent();
        initListener();
    }

    private void initComponent() {
        setButtonIcon(btnSend, "Assets/send.png", 32, 32);
        vERadioButton.setSelected(true);
    }

    private void initListener() {
        ActionListener settingPageFormListener = new SettingPageFormListener(this);
        btnSend.addActionListener(settingPageFormListener);
        btnSave.addActionListener(settingPageFormListener);
        eVRadioButton.addActionListener(settingPageFormListener);
        vERadioButton.addActionListener(settingPageFormListener);
    }
    private void setButtonIcon(JButton button, String iconPath, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(iconPath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage));
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
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
