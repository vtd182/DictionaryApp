package org.example.Views;

import org.example.Controllers.NavigationBarFromListener;

import javax.swing.*;
import java.awt.event.ActionListener;

public class NavigationBarForm extends JFrame{
    private JPanel NavigationBarForm;
    private JPanel jpNavigationBar;
    private JPanel jpContentArea;
    private JButton btnHomePage;
    private JButton btnSettingPage;

    private void initComponents() {
        this.setContentPane(NavigationBarForm);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int defaultWidth = 900;
        int defaultHeight = 600;
        this.setSize(defaultWidth, defaultHeight);

//        ImageIcon originalIcon = new ImageIcon("Assets/send-message.png");
//        Image scaledImage = originalIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
//        btn_send.setIcon(new ImageIcon(scaledImage));
//        btn_send.setOpaque(false);
//        btn_send.setBorderPainted(false);
//
//        htmlDocument = (HTMLDocument) display_message.getDocument();

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initListeners() {
        ActionListener navigationBarFromListener = new NavigationBarFromListener(this);
        btnHomePage.addActionListener(navigationBarFromListener);
        btnSettingPage.addActionListener(navigationBarFromListener);
    }
    public NavigationBarForm() {
        initComponents();
        initListeners();
    }
}
