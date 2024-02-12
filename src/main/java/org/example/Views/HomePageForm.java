package org.example.Views;

import org.example.Controllers.HomePageFormListener;
import org.example.Models.DictionaryManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeSet;

public class HomePageForm{

    private JList jlWord;
    private JButton btnChange;
    private JTextField tfSearch;
    private JButton btnFavorite;
    private JButton btnEditSave;
    private JButton btnDelete;
    private JTextArea taMeaning;
    private JPanel jpSearch;
    private JPanel jpSearchBar;
    private JLabel lbSelectionWord;
    private JPanel HomePageForm;
    private javax.swing.JSplitPane JSplitPanel;
    private JRadioButton rbtnAZ;
    private JRadioButton rbtnZA;
    private DictionaryManager dictionaryManager;

    public DictionaryManager getDictionaryManager() {
        return dictionaryManager;
    }
    private void initComponents() {
        rbtnAZ.setSelected(true);
    }

    private void initListeners() {
        ActionListener homePageFormListener = new HomePageFormListener(this);
        btnChange.addActionListener(homePageFormListener);
        btnDelete.addActionListener(homePageFormListener);
        btnEditSave.addActionListener(homePageFormListener);
        btnFavorite.addActionListener(homePageFormListener);

        jlWord.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    String selectedWord = (String) jlWord.getSelectedValue();
                    if (selectedWord != null) {
                        String meaning = dictionaryManager.getMeaning(selectedWord);
                        if (meaning != null) {
                            taMeaning.setText(meaning);
                        }
                    }
                }
            }
        });

        // Thêm ActionListener cho các RadioButton
        rbtnAZ.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (rbtnAZ.isSelected()) {
                    refreshDictionary(true); // Sắp xếp theo thứ tự AZ
                    rbtnZA.setSelected(false);
                }
            }
        });

        rbtnZA.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (rbtnZA.isSelected()) {
                    refreshDictionary(false); // Sắp xếp theo thứ tự ZA
                    rbtnAZ.setSelected(false);
                }
            }
        });
    }



    public void loadDictionary(String vietnameseToEnglishFilePath, String englishToVietnameseFilePath) {
        dictionaryManager.loadDictionariesFromXML(vietnameseToEnglishFilePath, englishToVietnameseFilePath);
        // Load từ điển từ DictionaryManager
        refreshDictionary();
    }

    public void refreshDictionary() {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String word : (dictionaryManager.isVietnameseToEnglishMode ?
                dictionaryManager.getVietnameseToEnglishDictionary().keySet() :
                dictionaryManager.getEnglishToVietnameseDictionary().keySet())) {
            model.addElement(word);
        }
        jlWord.setModel(model);
    }

    private void refreshDictionary(boolean ascendingOrder) {
        DefaultListModel<String> model = new DefaultListModel<>();
        TreeSet<String> sortedWords = new TreeSet<>();

        if (dictionaryManager.isVietnameseToEnglishMode) {
            sortedWords.addAll(dictionaryManager.getVietnameseToEnglishDictionary().keySet());
        } else {
            sortedWords.addAll(dictionaryManager.getEnglishToVietnameseDictionary().keySet());
        }

        // Thêm từng từ vào model theo thứ tự AZ hoặc ZA
        if (ascendingOrder) {
            for (String word : sortedWords) {
                model.addElement(word);
            }
        } else {
            for (String word : sortedWords.descendingSet()) {
                model.addElement(word);
            }
        }

        jlWord.setModel(model);
    }
    public HomePageForm() {
        initComponents();
        initListeners();
    }

    public JButton getBtnChange() {
        return btnChange;
    }

    public JButton getBtnFavorite() {
        return btnFavorite;
    }

    public JButton getBtnEditSave() {
        return btnEditSave;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public JSplitPane getJSplitPanel() {
        return JSplitPanel;
    }
    public void updateButtonContent() {

    }
    public JPanel getHomePageForm() {
        return HomePageForm;
    }

    public HomePageForm(DictionaryManager dictionaryManager) {
        this.dictionaryManager = dictionaryManager;
        initComponents();
        initListeners();
    }
}
