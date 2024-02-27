package org.example.Views;

import org.example.Controllers.NavigationBarFromListener;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Set;

public class NavigationBarForm extends JFrame {
    private JPanel NavigationBarForm;
    private JPanel jpNavigationBar;
    private JPanel jpContentArea;
    private JButton btnHomePage;
    private JButton btnFavoritePage;
    private javax.swing.JSplitPane JSplitPane;
    private JButton btnHistoryPage;
    private JButton btnSettingPage;
    private JButton btnTranslate;
    private HomePageForm homePageForm;

    private HomePageForm homePageFormForFavorite;
    private SettingPageForm settingPageForm;

    public HomePageForm getHomePageFormForFavorite() {
        return homePageFormForFavorite;
    }
    public SettingPageForm getSettingPageForm() {
        return settingPageForm;
    }

    public HomePageForm getHomePageForm() {
        return homePageForm;
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

        homePageForm = new HomePageForm();
        homePageForm.loadDictionary(vietnameseToEnglishFilePath, englishToVietnameseFilePath);
        homePageForm.loadFavoriteDictionary(vietnameseToEnglishFavoriteFilePath, englishToVietnameseFavoriteFilePath);

        homePageFormForFavorite = new HomePageForm();
        homePageFormForFavorite.loadDictionary(vietnameseToEnglishFilePath, englishToVietnameseFilePath);
        homePageFormForFavorite.loadFavoriteDictionary(vietnameseToEnglishFavoriteFilePath, englishToVietnameseFavoriteFilePath);

        settingPageForm = new SettingPageForm();

        jpContentArea.add("HomePage", homePageForm.getHomePageForm());
        jpContentArea.add("FavoritePage", homePageFormForFavorite.getHomePageForm());
        jpContentArea.add("SettingPage", settingPageForm.getSettingPageForm());

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initListeners() {
        ActionListener navigationBarFromListener = new NavigationBarFromListener(this);
        btnHomePage.addActionListener(navigationBarFromListener);
        btnFavoritePage.addActionListener(navigationBarFromListener);
        btnHistoryPage.addActionListener(navigationBarFromListener);
        btnSettingPage.addActionListener(navigationBarFromListener);
        btnTranslate.addActionListener(navigationBarFromListener);
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

    public JButton getBtnHistoryPage() {
        return btnHistoryPage;
    }

    public JButton getBtnSettingPage() {
        return btnSettingPage;
    }

    public JButton getBtnTranslate() {
        return btnTranslate;
    }

    public JPanel getJpContentArea() {
        return jpContentArea;
    }

}
