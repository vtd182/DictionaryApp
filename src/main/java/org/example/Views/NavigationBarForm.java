package org.example.Views;

import org.example.Controllers.NavigationBarFromListener;
import org.example.Helper.ConstantString;
import org.example.Helper.GlobalFunction;
import org.example.Models.AutoSaveManager;
import org.example.Models.DictionaryManager;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


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

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                int choice = JOptionPane.showConfirmDialog(NavigationBarForm.this,
                        "Bạn có muốn lưu các thay đổi không?", "Xác nhận",
                        JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    // Lưu các thay đổi
                    DictionaryManager.getInstance().saveFavoriteWordsToXML(ConstantString.VIETNAMESE_TO_ENGLISH_FAVORITE_FILE_PATH,
                            ConstantString.ENGLISH_TO_VIETNAMESE_FAVORITE_FILE_PATH);
                    DictionaryManager.getInstance().saveDictionaryToXML(ConstantString.VIETNAMESE_TO_ENGLISH_FILE_PATH,
                            ConstantString.ENGLISH_TO_VIETNAMESE_FILE_PATH);
                    System.out.println("Save completed.");
                    dispose(); // Đóng cửa sổ
                } else if (choice == JOptionPane.NO_OPTION) {
                    dispose(); // Đóng cửa sổ
                }
            }
        });
    }
    public NavigationBarForm() {
        // load data
        GlobalFunction.checkAndCreateFile();
        try {
            DictionaryManager.getInstance().loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println(DictionaryManager.getInstance().getErrorList().size());
        if (!DictionaryManager.getInstance().getErrorList().isEmpty()) {   // Nếu có lỗi khi load dữ liệu
            StringBuilder errorString = new StringBuilder();
            var errorSet = DictionaryManager.getInstance().getErrorList();
            for (var errorCode : errorSet) {
                switch (errorCode) {
                    case DictionaryManager.ERROR_VIETNAMESE_TO_ENGLISH_DICTIONARY:
                        errorString.append("Can not find " + ConstantString.VIETNAMESE_TO_ENGLISH_FILE_PATH + "\n");
                        break;
                    case DictionaryManager.ERROR_ENGLISH_TO_VIETNAMESE_DICTIONARY:
                        errorString.append("Can not find " + ConstantString.ENGLISH_TO_VIETNAMESE_FILE_PATH + "\n");
                        break;
                    case DictionaryManager.ERROR_VIETNAMESE_TO_ENGLISH_FAVORITE_WORDS:
                        errorString.append("Can not find " + ConstantString.VIETNAMESE_TO_ENGLISH_FAVORITE_FILE_PATH + "\n");
                        break;
                    case DictionaryManager.ERROR_ENGLISH_TO_VIETNAMESE_FAVORITE_WORDS:
                        errorString.append("Can not find " + ConstantString.ENGLISH_TO_VIETNAMESE_FAVORITE_FILE_PATH + "\n");
                        break;
                    case DictionaryManager.ERROR_ENGLISH_TO_VIETNAMESE_SEARCH_FREQUENCY_MAP:
                        errorString.append("Can not find " + ConstantString.ENGLISH_TO_VIETNAMESE_SEARCH_FREQUENCY_FILE_PATH + "\n");
                        break;
                    case DictionaryManager.ERROR_VIETNAMESE_TO_ENGLISH_SEARCH_FREQUENCY_MAP:
                        errorString.append("Can not find " + ConstantString.VIETNAMESE_TO_ENGLISH_SEARCH_FREQUENCY_FILE_PATH + "\n");
                        break;
                }
            }
            JOptionPane.showMessageDialog(null, "Load fail: \n" + errorString,
                    "ERROR", JOptionPane.ERROR_MESSAGE);

        }
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
