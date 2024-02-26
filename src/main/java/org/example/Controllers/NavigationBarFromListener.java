package org.example.Controllers;

import org.example.Views.HomePageForm;
import org.example.Views.NavigationBarForm;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NavigationBarFromListener implements ActionListener {
    private NavigationBarForm navigationBarForm;

    public NavigationBarFromListener(NavigationBarForm navigationBarForm) {
        this.navigationBarForm = navigationBarForm;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == navigationBarForm.getBtnHomePage()) {
            System.out.println("Home button clicked");
            ((CardLayout) navigationBarForm.getJpContentArea().getLayout()).show(navigationBarForm.getJpContentArea(), "HomePage");
            navigationBarForm.getHomePageForm().setIsFavoriteHomePage(false);
            navigationBarForm.getHomePageForm().refreshDictionary();
            navigationBarForm.validate();

        } else if (e.getSource() == navigationBarForm.getBtnFavoritePage()) {
            System.out.println("Favorite button clicked");
            ((CardLayout) navigationBarForm.getJpContentArea().getLayout()).show(navigationBarForm.getJpContentArea(), "FavoritePage");
            navigationBarForm.getHomePageFormForFavorite().setIsFavoriteHomePage(true);
            navigationBarForm.getHomePageFormForFavorite().refreshDictionary();
            navigationBarForm.validate();
        }
    }
}
