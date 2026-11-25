package com.agh.ui;

import com.agh.model.Branch;
import com.agh.model.Laptop;

import javax.swing.*;
import java.util.List;

public class StorePanel extends JPanel{
    JButton logOutButton = createButton("Log out", 770, 60, 100, 50);
    TableModel<Branch> branchModel = new TableModel<>(
            new String[]{"Name", "Laptops", "Capacity"},
            List.of(
                    branch->branch.branchName,
                    branch->branch.laptopList.size(),
                    branch->branch.maxCapacity
            )
    );
    JTable branchTable = new JTable(branchModel);
    TableModel<Laptop> laptopModel = new TableModel<>(
            new String[]{"Name", "Laptops", "Capacity"},
            List.of(
                    laptop -> laptop.brand,
                    laptop -> laptop.price,
                    laptop -> laptop.reserved
            )
    );
    JTable laptopTable = new JTable(laptopModel);

    StorePanel(String role, StoreFrame frame){
        setBounds(0, 0, 1000, 562);
        add(logOutButton);
        setLayout(null);
        setVisible(true);

        logOutButton.addActionListener(e -> frame.logOut());
    }

    void workerSetup(){

    }

    JButton createButton(String text, int posX, int posY, int width, int height){
        JButton button = new JButton(text);
        button.setBounds(posX, posY, width, height);
        return button;
    }
}
