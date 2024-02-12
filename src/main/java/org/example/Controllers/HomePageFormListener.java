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
        } else if (e.getSource() == homePageForm.getBtnFavorite()) {
            System.out.println("Favorite button clicked");
        }
    }
}
