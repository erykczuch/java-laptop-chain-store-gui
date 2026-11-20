package com.agh.ui;

import javax.swing.*;

public class LoginPanel extends JPanel {
    JButton clientButton = createButton("Log in as a client", 220, 200, 220, 50);
    JButton workerButton = createButton("Log in as a worker", 560, 200, 220, 50);
    LoginPanel(StoreFrame frame){
        setBounds(0, 0, 1000, 562);
        add(clientButton);
        add(workerButton);
        setLayout(null);
        setVisible(true);

        clientButton.addActionListener(e -> frame.changeStore("client"));
        workerButton.addActionListener(e -> frame.changeStore("worker"));
    }

    JButton createButton(String text, int posX, int posY, int width, int height){
        JButton button = new JButton(text);
        button.setBounds(posX, posY, width, height);
        return button;
    }
}
