package com.agh.ui;

import javax.swing.*;

public class StorePanel extends JPanel{
    JButton logOutButton = createButton("Log out", 770, 60, 100, 50);
    StorePanel(String role, StoreFrame frame){
        setBounds(0, 0, 1000, 562);
        add(logOutButton);
        setLayout(null);
        setVisible(true);

        logOutButton.addActionListener(e -> frame.logOut());
    }

    JButton createButton(String text, int posX, int posY, int width, int height){
        JButton button = new JButton(text);
        button.setBounds(posX, posY, width, height);
        return button;
    }
}
