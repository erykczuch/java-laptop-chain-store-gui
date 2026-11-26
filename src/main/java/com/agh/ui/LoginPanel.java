package com.agh.ui;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    JButton clientButton = new JButton("Log in as a client");
    JButton workerButton = new JButton("Log in as a worker");

    LoginPanel(StoreFrame frame) {
        setLayout(new GridBagLayout());
        setVisible(true);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 120, 0));
        buttons.add(clientButton);
        buttons.add(workerButton);
        add(buttons);

        clientButton.addActionListener(e -> frame.changeStore("client"));
        workerButton.addActionListener(e -> frame.changeStore("worker"));
    }
}
