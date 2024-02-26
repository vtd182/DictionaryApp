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

    private HomePageForm homePageForm;
    private HomePageForm homePageFormForFavorite;

    public HomePageForm getHomePageForm() {
        return homePageForm;
    }

    public HomePageForm getHomePageFormForFavorite() {
        return homePageFormForFavorite;
    }

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
        // Load từ điển từ hai file XML khác nhau

        // Load all word from file
        String vietnameseToEnglishFilePath = "Assets/Viet_Anh.xml";
        String englishToVietnameseFilePath = "Assets/Anh_Viet.xml";

        // Load favorite word
        String vietnameseToEnglishFavoriteFilePath = "Assets/Fav_Viet_Anh.xml";
        String englishToVietnameseFavoriteFilePath = "Assets/Fav_Anh_Viet.xml";

        homePageForm = HomePageForm.getInstance();
        homePageForm.loadDictionary(vietnameseToEnglishFilePath, englishToVietnameseFilePath);
        homePageForm.loadFavoriteDictionary(vietnameseToEnglishFavoriteFilePath, englishToVietnameseFavoriteFilePath);

        homePageFormForFavorite = HomePageForm.getInstance();
        homePageFormForFavorite.loadDictionary(vietnameseToEnglishFilePath, englishToVietnameseFilePath);
        homePageFormForFavorite.loadFavoriteDictionary(vietnameseToEnglishFavoriteFilePath, englishToVietnameseFavoriteFilePath);

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
