package com.agh.ui;

import javax.swing.*;

public class StoreFrame extends JFrame {
    int CardLayout = 0;     // 0 - login, 1 - store
    public StoreFrame() {
        //JFrame frame = new JFrame("Store Manager");
        setTitle("Store Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 562);
        LoginPanel panel = new LoginPanel(this);
        add(panel);
        setLayout(null);
        setVisible(true);
    }

    void changeStore(String role){
        getContentPane().removeAll();
        add(new StorePanel(role, this));
        revalidate();
        repaint();
    }

    void logOut(){
        getContentPane().removeAll();
        add(new LoginPanel(this));
        revalidate();
        repaint();
    }


}
