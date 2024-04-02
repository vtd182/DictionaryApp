package org.example.Controllers;

import org.example.Models.Word;
import org.example.Views.HomePageForm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePageFormListener implements ActionListener {
    HomePageForm homePageForm;

    public HomePageFormListener(HomePageForm homePageForm) {
        this.homePageForm = homePageForm;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == homePageForm.getBtnChange()) {
            onChangeButtonClicked();
        } else if (e.getSource() == homePageForm.getBtnDelete()) {
            onDeleteButtonClicked();
        } else if (e.getSource() == homePageForm.getBtnEditSave()) {
            onEditSaveButtonClicked();
        } else if (e.getSource() == homePageForm.getBtnFavorite()) {
            onFavoriteButtonClicked();
        } else if (e.getSource() == homePageForm.getRbtnZA()) {
            if (homePageForm.getRbtnZA().isSelected()) {
                homePageForm.refreshDictionary(false);
                homePageForm.getRbtnAZ().setSelected(false);
            } else {
                homePageForm.getRbtnZA().setSelected(true);
            }

        } else if (e.getSource() == homePageForm.getRbtnAZ()) {
            if (homePageForm.getRbtnAZ().isSelected()) {
                homePageForm.refreshDictionary(true);
                homePageForm.getRbtnZA().setSelected(false);
            } else {
                homePageForm.getRbtnAZ().setSelected(true);
            }
        }
    }

    private void onFavoriteButtonClicked() {
        var dictionaryManager = homePageForm.getDictionaryManager();
        var selectedWord = homePageForm.getSelectedWord();
        if (selectedWord == null || selectedWord.isEmpty() || selectedWord.equals("Selection Word")) {
            return;
        }
        if (dictionaryManager.isFavoriteWord(selectedWord, homePageForm.isVietnameseToEnglishMode())) {
            dictionaryManager.removeFavoriteWord(selectedWord, homePageForm.isVietnameseToEnglishMode());
        } else {
            var word = dictionaryManager.getWord(selectedWord, homePageForm.isVietnameseToEnglishMode());
            dictionaryManager.addFavoriteWord(selectedWord, word, homePageForm.isVietnameseToEnglishMode());
        }
        if (homePageForm.isFavoriteHomePage()) {
            homePageForm.refreshDictionary();
            homePageForm.getMeaningArea().setText("");
            homePageForm.getSelectedWordLabel().setText("Selection Word");
        }
        System.out.println("Favorite button clicked");
        homePageForm.switchFavoriteButtonIcon(dictionaryManager.isFavoriteWord(selectedWord, homePageForm.isVietnameseToEnglishMode()));
    }

    private void onChangeButtonClicked() {
        System.out.println("Change button clicked");
        homePageForm.switchMode();
        homePageForm.switchModeButtonIcon();
        homePageForm.refreshDictionary();
    }

    private void onDeleteButtonClicked() {
        System.out.println("Delete button clicked");
        String selectedWord = homePageForm.getSelectedWord();
        if (selectedWord == null || selectedWord.isEmpty() || selectedWord.equals("Selection Word")) {
            return;
        }
        if (homePageForm.showDeleteConfirmationDialog(selectedWord)) {
            // Xóa từ và cập nhật lại danh sách từ
            homePageForm.getDictionaryManager().deleteWord(selectedWord, homePageForm.isVietnameseToEnglishMode());
            homePageForm.refreshDictionary();
            homePageForm.getMeaningArea().setText("");
            homePageForm.getSelectedWordLabel().setText("Selection Word");
        }
    }

    private void onEditSaveButtonClicked() {
        System.out.println("Edit/Save button clicked");
        homePageForm.getDictionaryManager().saveFavoriteWordsToXML("Assets/Fav_Viet_Anh.xml", "Assets/Fav_Anh_Viet.xml");

        // Nếu màn hình đang ở chế độ chờ thì không làm gì cả
        var word = homePageForm.getSelectedWord();
        if (word == null || word.isEmpty() || word.equals("Selection Word")) {
            return;
        }

        // Lấy nghĩa mới từ ô nhập liệu
        String newMeaning = homePageForm.getMeaningArea().getText();
        homePageForm.switchEditingMode();
        var isVietnameseToEnglishMode = homePageForm.isVietnameseToEnglishMode();
        var selectedWord = homePageForm.getDictionaryManager().getWord(homePageForm.getSelectedWord(), isVietnameseToEnglishMode);

        // Nếu đang ở chế độ chỉnh sửa thì hiển thị ở dạng string
        if (homePageForm.isEditing()) {
            homePageForm.getMeaningArea().setText(selectedWord.toString());
        } else {
            // Nếu đang ở chế độ lưu thì lưu từ mới vào từ điển
            System.out.println("Save button clicked");
            if (newMeaning != null) {
                Word newWord = Word.parseFromString(newMeaning);
                if (!isVietnameseToEnglishMode) {
                    homePageForm.getDictionaryManager().getEnglishToVietnameseDictionary().put(word, newWord);
                    selectedWord = homePageForm.getDictionaryManager().getEnglishToVietnameseDictionary().get(word);
                } else {
                    homePageForm.getDictionaryManager().getVietnameseToEnglishDictionary().put(word, newWord);
                    selectedWord = homePageForm.getDictionaryManager().getVietnameseToEnglishDictionary().get(word);
                }
            }
            homePageForm.getMeaningArea().setText(selectedWord.toHtmlString());
        }
    }
}
