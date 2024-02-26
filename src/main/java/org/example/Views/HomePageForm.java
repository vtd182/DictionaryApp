package org.example.Views;

import org.example.Controllers.HomePageFormListener;
import org.example.Models.DictionaryManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.TreeSet;

public class HomePageForm{

    private static HomePageForm instance = null;
    private JList jlWord;
    private JButton btnChange;
    private JTextField tfSearch;
    private JButton btnFavorite;
    private JButton btnEditSave;
    private JButton btnDelete;
    private JPanel jpSearch;
    private JPanel jpSearchBar;
    private JLabel lbSelectionWord;
    private JPanel HomePageForm;
    private javax.swing.JSplitPane JSplitPanel;
    private JRadioButton rbtnAZ;
    private JRadioButton rbtnZA;
    private JLabel lbSuggestMessage;
    private JEditorPane MeaningArea;
    private DictionaryManager dictionaryManager;
    private boolean isFavoriteHomePage = false;
    public DictionaryManager getDictionaryManager() {
        return dictionaryManager;
    }

    public void setIsFavoriteHomePage(boolean isFavoriteHomePage) {
        this.isFavoriteHomePage = isFavoriteHomePage;
        this.getDictionaryManager().setIsFavoriteMode(isFavoriteHomePage);
    }
    public static HomePageForm getInstance() {
        if (instance == null) {
            instance = new HomePageForm();
        }
        return instance;
    }
    private void initComponents() {
        rbtnAZ.setSelected(true);
        lbSuggestMessage.setText("");
        lbSelectionWord.setText("Selection Word");

        setButtonIcon(btnFavorite, "Assets/un_star.png", 32, 32);
        setButtonIcon(btnEditSave, "Assets/edit.png", 32, 32);
        setButtonIcon(btnDelete, "Assets/delete.png", 32, 32);
        setButtonIcon(btnChange, "Assets/ic1.png", 40, 40);
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
                        String htmlMeaning = dictionaryManager.getHtmlMeaning(selectedWord);
                        lbSelectionWord.setText(selectedWord);
                        System.out.println("Selected word: " + selectedWord);
                        if (dictionaryManager.isFavoriteWord(selectedWord)) {
                            setButtonIcon(btnFavorite, "Assets/star.png", 32, 32);
                        } else {
                            setButtonIcon(btnFavorite, "Assets/un_star.png", 32, 32);
                        }

                        if (meaning != null) {
                            MeaningArea.setText(htmlMeaning);
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

        tfSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("[tfSearch] Enter pressed");
                String keyword = tfSearch.getText().trim();
                searchAndUpdateList(keyword);
                DefaultListModel<String> currentModel = (DefaultListModel<String>) jlWord.getModel();
                if (currentModel.size() == 0) {
                    suggestWords(keyword);
                }
            }
        });

        tfSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                System.out.println("[tfSearch] Changed");
                String keyword = tfSearch.getText().trim();
                searchAndUpdateList(keyword);
                if (keyword.length() == 0) {
                    setDefaultValueForSuggest();
                }
            }

            public void removeUpdate(DocumentEvent e) {
                System.out.println("[tfSearch] Removed");
                String keyword = tfSearch.getText().trim();
                searchAndUpdateList(keyword);
                if (keyword.length() == 0) {
                    setDefaultValueForSuggest();
                }
            }

            public void insertUpdate(DocumentEvent e) {
                System.out.println("[tfSearch] Inserted");
                String keyword = tfSearch.getText().trim();
                searchAndUpdateList(keyword);
                if (keyword.length() == 0) {
                    setDefaultValueForSuggest();
                }
            }
        });

        lbSuggestMessage.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String suggestedWord = lbSuggestMessage.getText().substring(13); // Bỏ "Did you mean: "
                suggestedWord = suggestedWord.substring(0, suggestedWord.length() - 1).trim(); // Bỏ dấu "?"
                tfSearch.setText(suggestedWord);
                searchAndUpdateList(suggestedWord);
                lbSuggestMessage.setText("");
                jlWord.setSelectedIndex(0);
            }
        });
    }

    public void loadDictionary(String vietnameseToEnglishFilePath, String englishToVietnameseFilePath) {
        dictionaryManager = new DictionaryManager();
        dictionaryManager.loadDictionariesFromXML(vietnameseToEnglishFilePath, englishToVietnameseFilePath);
        refreshDictionary();
    }

    public void loadFavoriteDictionary(String vietnameseToEnglishFavoriteFilePath, String englishToVietnameseFavoriteFilePath) {
        dictionaryManager.loadFavoriteWordsFromXML(vietnameseToEnglishFavoriteFilePath, englishToVietnameseFavoriteFilePath);
        refreshDictionary();
    }

    public void refreshDictionary() {
        DefaultListModel<String> model = new DefaultListModel<>();
        System.out.println("[refreshDictionary] Refreshing dictionary");
        if (!isFavoriteHomePage) {
            for (String word : (dictionaryManager.isVietnameseToEnglishMode ?
                    dictionaryManager.getVietnameseToEnglishDictionary().keySet() :
                    dictionaryManager.getEnglishToVietnameseDictionary().keySet())) {
                model.addElement(word);
            }
        } else {
            for (String word : (dictionaryManager.isVietnameseToEnglishMode ?
                    dictionaryManager.getVietnameseToEnglishFavoriteWords().keySet() :
                    dictionaryManager.getEnglishToVietnameseFavoriteWords().keySet())) {
                model.addElement(word);
            }
        }
        jlWord.setModel(model);
    }

    private void refreshDictionary(boolean ascendingOrder) {
        DefaultListModel<String> model = new DefaultListModel<>();
        TreeSet<String> sortedWords = new TreeSet<>();

        if (!isFavoriteHomePage) {
            if (dictionaryManager.isVietnameseToEnglishMode) {
                sortedWords.addAll(dictionaryManager.getVietnameseToEnglishDictionary().keySet());
            } else {
                sortedWords.addAll(dictionaryManager.getEnglishToVietnameseDictionary().keySet());
            }
        } else {
            if (dictionaryManager.isVietnameseToEnglishMode) {
                sortedWords.addAll(dictionaryManager.getVietnameseToEnglishFavoriteWords().keySet());
            } else {
                sortedWords.addAll(dictionaryManager.getEnglishToVietnameseFavoriteWords().keySet());
            }
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

    public HomePageForm(boolean isFavoriteHomePage) {
        this.isFavoriteHomePage = isFavoriteHomePage;
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

    public JPanel getHomePageForm() {
        return HomePageForm;
    }

    private void searchAndUpdateList(String keyword) {
        System.out.println("[searchAndUpdateList] Searching for: " + keyword);
        DefaultListModel<String> model = new DefaultListModel<>();
        TreeSet<String> relatedWords = dictionaryManager.search(keyword);

        for (String word : relatedWords) {
            model.addElement(word);
        }

        jlWord.setModel(model);
    }

    private void suggestWords(String keyword) {
        String suggestedWord = dictionaryManager.suggest(keyword);
        if (suggestedWord != null) {
            lbSuggestMessage.setText("Did you mean: " + suggestedWord + "?");
        } else {
            lbSuggestMessage.setText("");
        }
    }

    private void setDefaultValueForSuggest() {
        lbSuggestMessage.setText("");
    }

    private void setButtonIcon(JButton button, String iconPath, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(iconPath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage));
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
    }

    public void switchFavoriteButtonIcon(boolean isFavorite) {
        if (isFavorite) {
            setButtonIcon(btnFavorite, "Assets/star.png", 32, 32);
        } else {
            setButtonIcon(btnFavorite, "Assets/un_star.png", 32, 32);
        }
    }

    public String getSelectedWord() {
        return lbSelectionWord.getText();
    }
}
