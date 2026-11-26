package com.agh.ui;

import com.agh.app.StoreFacade;

import javax.swing.*;
import java.awt.*;

public class StoreFrame extends JFrame {
    int CardLayout = 0;     // 0 - login, 1 - store
    StoreFacade store;
    public StoreFrame(StoreFacade store) {
        this.store = store;
        setTitle("Store Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 562);
        setLayout(new BorderLayout());
        add(new LoginPanel(this), BorderLayout.CENTER);
        setVisible(true);
    }

    void changeStore(String role){
        getContentPane().removeAll();
        add(new StorePanel(role, this, store), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    void logOut(){
        getContentPane().removeAll();
        add(new LoginPanel(this), BorderLayout.CENTER);
        revalidate();
        repaint();
    }


}
