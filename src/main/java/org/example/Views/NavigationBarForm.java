package org.example.Views;

import org.example.Controllers.NavigationBarFromListener;
import org.example.Helper.ConstantString;
import org.example.Models.AutoSaveManager;
import org.example.Models.DictionaryManager;

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
    private HistoryPageForm historyPageForm;
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

        homePageForm = new HomePageForm();
        homePageFormForFavorite = new HomePageForm();
        settingPageForm = new SettingPageForm();
        historyPageForm = new HistoryPageForm();


        jpContentArea.add("HomePage", homePageForm.getHomePageForm());
        jpContentArea.add("FavoritePage", homePageFormForFavorite.getHomePageForm());
        jpContentArea.add("SettingPage", settingPageForm.getSettingPageForm());
        jpContentArea.add("HistoryPage", historyPageForm.getHistoryPageForm());

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
        AutoSaveManager.getInstance().startAutoSave();
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
