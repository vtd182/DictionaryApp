package org.example.Helper;

import javax.swing.*;
import java.awt.*;

public class GlobalFunction {
    public static void setButtonIcon(JButton button, String iconPath, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(GlobalFunction.class.getResource(iconPath));
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage));
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
    }
}
