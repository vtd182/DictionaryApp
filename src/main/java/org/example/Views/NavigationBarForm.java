package org.example.Views;

import org.example.Controllers.NavigationBarFromListener;
import org.example.Models.DictionaryManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

public class NavigationBarForm extends JFrame {
    private JPanel NavigationBarForm;
    private JPanel jpNavigationBar;
    private JPanel jpContentArea;
    private JButton btnHomePage;
    private JButton btnFavoritePage;
    private javax.swing.JSplitPane JSplitPane;

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

        DictionaryManager dictionaryManager = new DictionaryManager();

        // Load từ điển từ hai file XML khác nhau

        // Load all word from file
        String vietnameseToEnglishFilePath = "Assets/Viet_Anh.xml";
        String englishToVietnameseFilePath = "Assets/Anh_Viet.xml";

        // Load favorite word
        String vietnameseToEnglishFavoriteFilePath = "";
        String englishToVietnameseFavoriteFilePath = "";

        dictionaryManager.loadDictionariesFromXML(vietnameseToEnglishFilePath, englishToVietnameseFilePath);

        HomePageForm homePageForm = new HomePageForm(dictionaryManager);
        homePageForm.loadDictionary(vietnameseToEnglishFilePath, englishToVietnameseFilePath);

        HomePageForm homePageFormForFavorite = new HomePageForm(dictionaryManager);

        jpContentArea.add("HomePage", homePageForm.getHomePageForm());
        jpContentArea.add("FavoritePage", homePageFormForFavorite.getHomePageForm());

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initListeners() {
        ActionListener navigationBarFromListener = new NavigationBarFromListener(this);
        btnHomePage.addActionListener(navigationBarFromListener);
        btnFavoritePage.addActionListener(navigationBarFromListener);
    }
    public NavigationBarForm() {
        initComponents();
        initListeners();
    }

    public JButton getBtnHomePage() {
        return btnHomePage;
    }

    public JButton getBtnFavoritePage() {
        return btnFavoritePage;
    }

    public JPanel getJpContentArea() {
        return jpContentArea;
    }

}
