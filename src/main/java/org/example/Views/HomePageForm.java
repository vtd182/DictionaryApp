package org.example.Views;

import org.example.Controllers.HomePageFormListener;
import org.example.Helper.ConstantString;
import org.example.Models.DictionaryManager;
import org.example.Models.Word;

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
import java.util.*;
import java.util.List;

public class HomePageForm {
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
    private JScrollPane jScrollPane;
    private DictionaryManager dictionaryManager;
    private boolean isFavoriteHomePage = false;
    private boolean isEditing = false;
    private boolean isVietnameseToEnglishMode = true;

    public DictionaryManager getDictionaryManager() {
        return dictionaryManager;
    }

    public void setIsFavoriteHomePage(boolean isFavoriteHomePage) {
        this.isFavoriteHomePage = isFavoriteHomePage;
        this.getDictionaryManager().setIsFavoriteMode(isFavoriteHomePage);
    }

    public boolean isFavoriteHomePage() {
        return isFavoriteHomePage;
    }

    public JLabel getSelectedWordLabel() {
        return lbSelectionWord;
    }

    private void initComponents() {
        rbtnAZ.setSelected(true);
        lbSuggestMessage.setText("");
        lbSelectionWord.setText("Selection Word");

        setButtonIcon(btnFavorite, ConstantString.IC_STAR, 32, 32);
        setButtonIcon(btnEditSave, ConstantString.IC_EDIT, 32, 32);
        setButtonIcon(btnDelete, ConstantString.IC_DELETE, 32, 32);
        setButtonIcon(btnChange, ConstantString.IC_VIETNAMESE_TO_ENGLISH, 40, 40);
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
                        if (isEditing) {
                            switchEditingMode();
                        }

                        lbSelectionWord.setText(selectedWord);
                        if (dictionaryManager.isFavoriteWord(selectedWord, isVietnameseToEnglishMode)) {
                            setButtonIcon(btnFavorite, ConstantString.IC_STAR_FILL, 32, 32);
                        } else {
                            setButtonIcon(btnFavorite, ConstantString.IC_STAR, 32, 32);
                        }

                        try {
                            String htmlMeaning = dictionaryManager.getHtmlMeaning(selectedWord, isVietnameseToEnglishMode);
                            if (htmlMeaning != null) {
                                MeaningArea.setText(htmlMeaning);
                            }
                        } catch (Exception e) {
                            MeaningArea.setText(ConstantString.ERROR_GET_WORD_MESSAGE_HTML);
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
                } else {
                    rbtnAZ.setSelected(true);
                }
            }
        });

        rbtnZA.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (rbtnZA.isSelected()) {
                    refreshDictionary(false); // Sắp xếp theo thứ tự ZA
                    rbtnAZ.setSelected(false);
                } else {
                    rbtnZA.setSelected(true);
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
                } else {
                    if (currentModel.contains(keyword)) {
                        var selectedIndex = currentModel.indexOf(keyword);
                        jlWord.ensureIndexIsVisible(selectedIndex);

                        Point p = jlWord.indexToLocation(selectedIndex);
                        // Nếu vị trí có thể được xác định
                        if (p != null) {
                            // Lấy vị trí hiện tại của khu vực cuộn
                            Point currentScroll = jScrollPane.getViewport().getViewPosition();
                            // Xác định vị trí mới để cuộn
                            Point newScroll = new Point(currentScroll.x, p.y);
                            // Cuộn khu vực cuộn đến vị trí mới
                            jScrollPane.getViewport().setViewPosition(newScroll);
                        }
                        jlWord.setSelectedIndex(selectedIndex);
                    }
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
                DefaultListModel<String> currentModel = (DefaultListModel<String>) jlWord.getModel();
                if (currentModel.size() == 0) {
                    suggestWords(suggestedWord);
                } else {
                    if (currentModel.contains(suggestedWord)) {
                        var selectedIndex = currentModel.indexOf(suggestedWord);
                        jlWord.ensureIndexIsVisible(selectedIndex);

                        Point p = jlWord.indexToLocation(selectedIndex);
                        // Nếu vị trí có thể được xác định
                        if (p != null) {
                            // Lấy vị trí hiện tại của khu vực cuộn
                            Point currentScroll = jScrollPane.getViewport().getViewPosition();
                            // Xác định vị trí mới để cuộn
                            Point newScroll = new Point(currentScroll.x, p.y);
                            // Cuộn khu vực cuộn đến vị trí mới
                            jScrollPane.getViewport().setViewPosition(newScroll);
                        }
                        jlWord.setSelectedIndex(selectedIndex);
                    }
                }
            }
        });
    }

    public void loadDictionary(String vietnameseToEnglishFilePath, String englishToVietnameseFilePath) {
        dictionaryManager.loadDictionariesFromXML(vietnameseToEnglishFilePath, englishToVietnameseFilePath);
        refreshDictionary();
    }

    public void loadFavoriteDictionary(String vietnameseToEnglishFavoriteFilePath, String englishToVietnameseFavoriteFilePath) {
        dictionaryManager.loadFavoriteWordsFromXML(vietnameseToEnglishFavoriteFilePath, englishToVietnameseFavoriteFilePath);
        refreshDictionary();
    }

    public void refreshDictionary() {
        DefaultListModel<String> model = new DefaultListModel<>();
        try {
            if (!isFavoriteHomePage) {
                for (String word : (isVietnameseToEnglishMode ?
                        dictionaryManager.getVietnameseToEnglishDictionary().keySet() :
                        dictionaryManager.getEnglishToVietnameseDictionary().keySet())) {
                    model.addElement(word);
                }
            } else {
                System.out.println("[refreshDictionary] Refreshing favorite dictionary");
                for (String word : dictionaryManager.getFavoriteWords(isVietnameseToEnglishMode).keySet()) {
                    model.addElement(word);
                }
            }
            jlWord.setModel(model);
        } catch (Exception e) {
            System.out.println("[refreshDictionary] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void refreshDictionary(boolean ascendingOrder) {
        DefaultListModel<String> model = new DefaultListModel<>();
        TreeSet<String> sortedWords = new TreeSet<>();

        if (!isFavoriteHomePage) {
            if (isVietnameseToEnglishMode) {
                sortedWords.addAll(dictionaryManager.getVietnameseToEnglishDictionary().keySet());
            } else {
                sortedWords.addAll(dictionaryManager.getEnglishToVietnameseDictionary().keySet());
            }
        } else {
            if (isVietnameseToEnglishMode) {
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
        System.out.println("[HomePageForm] Initializing HomePageForm");
        initComponents();
        initListeners();
        dictionaryManager = DictionaryManager.getInstance();
        refreshDictionary();
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

    public JEditorPane getMeaningArea() {
        return MeaningArea;
    }

    private void searchAndUpdateList(String keyword) {
        System.out.println("[searchAndUpdateList] Searching for: " + keyword);
        DefaultListModel<String> model = new DefaultListModel<>();
        TreeSet<String> relatedWords = dictionaryManager.search(keyword, isVietnameseToEnglishMode);

        for (String word : relatedWords) {
            model.addElement(word);
        }

        jlWord.setModel(model);
    }

    private void suggestWords(String keyword) {
        String suggestedWord = dictionaryManager.suggest(keyword, isVietnameseToEnglishMode);
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
        ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(iconPath)));
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage));
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
    }

    public void switchFavoriteButtonIcon(boolean isFavorite) {
        if (isFavorite) {
            setButtonIcon(btnFavorite, ConstantString.IC_STAR_FILL, 32, 32);
        } else {
            setButtonIcon(btnFavorite, ConstantString.IC_STAR, 32, 32);
        }
    }

    public void switchModeButtonIcon() {
        if (isVietnameseToEnglishMode) {
            setButtonIcon(btnChange, ConstantString.IC_VIETNAMESE_TO_ENGLISH, 40, 40);
        } else {
            setButtonIcon(btnChange, ConstantString.IC_ENGLISH_TO_VIETNAMESE, 40, 40);
        }
    }

    public boolean isVietnameseToEnglishMode() {
        return isVietnameseToEnglishMode;
    }

    public boolean isEditing() {
        return isEditing;
    }
    public void switchEditingMode() {
        if (isEditing) {
            isEditing = false;
            setButtonIcon(btnEditSave, ConstantString.IC_EDIT, 32, 32);
            MeaningArea.setEditable(false);
            MeaningArea.setContentType("text/html");
        } else {
            isEditing = true;
            setButtonIcon(btnEditSave, ConstantString.IC_SAVE, 32, 32);
            MeaningArea.setEditable(true);
            MeaningArea.setContentType("text");
        }
    }

    public String getSelectedWord() {
        return lbSelectionWord.getText();
    }

    public boolean showDeleteConfirmationDialog(String wordToDelete) {
        int result = JOptionPane.showConfirmDialog(this.HomePageForm,
                "Are you sure you want to delete '" + wordToDelete + "'?", "Delete Confirmation",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    public void switchMode() {
        isVietnameseToEnglishMode = !isVietnameseToEnglishMode;
        refreshDictionary();
    }
}
