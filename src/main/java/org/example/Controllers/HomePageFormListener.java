package org.example.Controllers;

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
            System.out.println("Change button clicked");
            homePageForm.getDictionaryManager().switchMode();
            homePageForm.refreshDictionary();
        } else if (e.getSource() == homePageForm.getBtnDelete()) {
            System.out.println("Delete button clicked");
        } else if (e.getSource() == homePageForm.getBtnEditSave()) {
            System.out.println("Edit/Save button clicked");
            homePageForm.getDictionaryManager().saveFavoriteWordsToXML
                    ("Assets/Fav_Viet_Anh.xml",
                    "Assets/Fav_Anh_Viet.xml");
        } else if (e.getSource() == homePageForm.getBtnFavorite()) {
            var dictionaryManager = homePageForm.getDictionaryManager();
            var selectedWord = homePageForm.getSelectedWord();
            var isVietnameseToEnglishMode = dictionaryManager.isVietnameseToEnglishMode;
            if (dictionaryManager.isFavoriteWord(selectedWord)) {
                dictionaryManager.removeFavoriteWord(selectedWord);
                homePageForm.refreshDictionary();
            } else {
                dictionaryManager.addFavoriteWord(selectedWord, dictionaryManager.getWord(selectedWord));
                homePageForm.refreshDictionary();
            }
            System.out.println("Favorite button clicked");
            homePageForm.switchFavoriteButtonIcon(dictionaryManager.isFavoriteWord(selectedWord));
        }
    }
}
