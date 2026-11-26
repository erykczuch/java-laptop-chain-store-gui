package com.agh.ui;

import com.agh.app.StoreFacade;
import com.agh.model.Branch;
import com.agh.model.Laptop;
import com.agh.model.LaptopCondition;
import com.agh.model.exception.BranchFullException;
import com.agh.model.exception.DuplicateLaptopException;
import com.agh.model.exception.InvalidPriceException;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StorePanel extends JPanel{
    String role;
    StoreFacade facade;
    JButton logOutButton = createButton("Log out", 860, 30, 100, 50);
    JButton addBranchButton = createButton("Add new branch", 80, 350, 200, 50);
    JButton removeBranchButton = createButton("Remove branch", 80, 420, 200, 50);
    JButton addLaptopButton = createButton("Add new laptop", 400, 350, 200, 50);
    JButton removeLaptopButton = createButton("Remove laptop", 400, 420, 200, 50);
    JButton sortBranchesByLoadButton = createButton("Sort branches by load", 700, 325, 200, 50);
    JButton changePriceButton = createButton("Change price", 700, 385, 200, 50);
    JButton changeConditionButton = createButton("Change condition", 700, 445, 200, 50);
    JTextField filterField = createTextField(500, 60, 150, 30);
    JLabel filterLabel = new JLabel("Filter by brand or model");
    JComboBox<Object> conditionFilter = new JComboBox<>();
    JLabel conditionLabel = new JLabel("Condition");

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
            new String[]{"Brand", "Model", "Price", "Condition", "Reserved"},
            List.of(
                    laptop -> laptop.brand,
                    laptop -> laptop.model,
                    laptop -> laptop.price,
                    laptop -> laptop.laptopCondition,
                    laptop -> laptop.reserved
            )
    );
    JTable laptopTable = new JTable(laptopModel);


    StorePanel(String role, StoreFrame frame, StoreFacade facade){
        this.role = role;
        this.facade = facade;
        setBounds(0, 0, 1000, 562);
        add(logOutButton);
        setLayout(null);
        setVisible(true);
        logOutButton.addActionListener(e -> frame.logOut());
        if(role == "worker")
            workerSetup();
        else if(role == "client")
            clientSetup();
    }

    void clientSetup(){
        //tables
        showTable(laptopTable, 500, 100, 380, 200);
        showTable(branchTable, 120, 100, 250, 200);
        loadTable(branchModel);
        branchTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            showFilteredLaptops();
        });
        //buttons
        JButton reserveButton = createButton("Reserve", 120, 350, 200, 50);
        add(reserveButton);
        reserveButton.addActionListener(e -> {
            int laptopRow = laptopTable.getSelectedRow();
            int branchRow = branchTable.getSelectedRow();
            if (laptopRow < 0 || branchRow < 0) return;
            facade.reserveLaptop(laptopModel.getRow(laptopRow));
            showFilteredLaptops();
        });
        JButton cancelReservationButton = createButton("Cancel reservation", 120, 420, 200, 50);
        add(cancelReservationButton);
        cancelReservationButton.addActionListener(e -> {
            int laptopRow = laptopTable.getSelectedRow();
            int branchRow = branchTable.getSelectedRow();
            if (laptopRow < 0 || branchRow < 0) return;
            facade.cancelReservation(laptopModel.getRow(laptopRow));
            showFilteredLaptops();
        });
        sortBranchesByLoadButton.setBounds(400, 350, 200, 50);
        add(sortBranchesByLoadButton);
        sortBranchesByLoadButton.addActionListener(e -> {
            JComboBox<String> order = new JComboBox<>(new String[]{"Ascending", "Descending"});
            JPanel form = new JPanel();
            form.add(new JLabel("Order"));
            form.add(order);
            int result = JOptionPane.showConfirmDialog(
                    this, form, "Sort branches", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result != JOptionPane.OK_OPTION) return;
            boolean descending = order.getSelectedItem().equals("Descending");
            branchModel.setRows(facade.sortBranchesByLoad(descending));
        });
        //filters
        add(filterField);
        filterField.addActionListener(e -> showFilteredLaptops());
        conditionFilter.addItem("All");
        for (LaptopCondition condition : LaptopCondition.values()) {
            conditionFilter.addItem(condition);
        }
        conditionFilter.setBounds(700, 60, 150, 30);
        add(conditionFilter);
        conditionFilter.addActionListener(e -> showFilteredLaptops());
        //labels
        filterLabel.setBounds(500, 40, 200, 20);
        add(filterLabel);
        conditionLabel.setBounds(700, 40, 200, 20);
        add(conditionLabel);
    }

    void workerSetup(){
        // tables
        showTable(laptopTable, 500, 100, 380, 200);
        showTable(branchTable, 120, 100, 250, 200);
        loadTable(branchModel);
        branchTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int row = branchTable.getSelectedRow();
            if (row < 0) return;
            Branch branch = branchModel.getRow(row);
            laptopModel.setRows(facade.laptops(branch));
        });
        // buttons
        add(addBranchButton);
        add(removeBranchButton);
        add(addLaptopButton);
        add(removeLaptopButton);
        add(sortBranchesByLoadButton);
        add(changePriceButton);
        add(changeConditionButton);
        addBranchButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Branch name");
            String capacityText = JOptionPane.showInputDialog(this, "Capacity");
            if (name == null || capacityText == null) return;
            facade.addBranch(name, Integer.parseInt(capacityText));
            loadTable(branchModel);
        });
        removeBranchButton.addActionListener(e -> {
            int row = branchTable.getSelectedRow();
            if (row < 0) return;
            facade.removeBranch(branchModel.getRow(row).branchName);
            loadTable(branchModel);
            laptopModel.setRows(List.of());
        });
        addLaptopButton.addActionListener(e -> {
            int row = branchTable.getSelectedRow();
            if (row < 0) return;
            JTextField model = new JTextField();
            JTextField brand = new JTextField();
            JTextField year = new JTextField();
            JTextField price = new JTextField();
            JTextField ram = new JTextField();
            JTextField disk = new JTextField();
            JTextField processor = new JTextField();
            JTextField battery = new JTextField();
            JTextField serial = new JTextField();
            JComboBox<LaptopCondition> condition = new JComboBox<>(LaptopCondition.values());

            JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
            form.add(new JLabel("Condition"));
            form.add(condition);
            form.add(new JLabel("Model"));
            form.add(model);
            form.add(new JLabel("Brand"));
            form.add(brand);
            form.add(new JLabel("Year"));
            form.add(year);
            form.add(new JLabel("Price"));
            form.add(price);
            form.add(new JLabel("RAM GB"));
            form.add(ram);
            form.add(new JLabel("Disk GB"));
            form.add(disk);
            form.add(new JLabel("Processor"));
            form.add(processor);
            form.add(new JLabel("Battery"));
            form.add(battery);
            form.add(new JLabel("Serial number"));
            form.add(serial);

            int result = JOptionPane.showConfirmDialog(
                    this, form, "Add laptop", JOptionPane.OK_CANCEL_OPTION);
            if (result != JOptionPane.OK_OPTION) return;
            Laptop laptop = new Laptop(
                    (LaptopCondition) condition.getSelectedItem(),
                    model.getText(),
                    brand.getText(),
                    Integer.parseInt(year.getText()),
                    Double.parseDouble(price.getText()),
                    Integer.parseInt(ram.getText()),
                    Integer.parseInt(disk.getText()),
                    processor.getText(),
                    Integer.parseInt(battery.getText()),
                    Integer.parseInt(serial.getText())
            );
            try {
                facade.addLaptop(branchModel.getRow(row), laptop);
                laptopModel.setRows(facade.laptops(branchModel.getRow(row)));
            } catch (BranchFullException | DuplicateLaptopException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
            laptopModel.setRows(facade.laptops(branchModel.getRow(row)));
        });
        removeLaptopButton.addActionListener(e -> {
            int laptopRow = laptopTable.getSelectedRow();
            int branchRow = branchTable.getSelectedRow();
            if (laptopRow < 0 || branchRow < 0) return;
            facade.removeLaptop(branchModel.getRow(branchRow), laptopModel.getRow(laptopRow));
            loadTable(branchModel);
            laptopModel.setRows(facade.laptops(branchModel.getRow(branchRow)));
        });
        sortBranchesByLoadButton.addActionListener(e -> {
            JComboBox<String> order = new JComboBox<>(new String[]{"Ascending", "Descending"});
            JPanel form = new JPanel();
            form.add(new JLabel("Order"));
            form.add(order);
            int result = JOptionPane.showConfirmDialog(
                    this, form, "Sort branches", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result != JOptionPane.OK_OPTION) return;
            boolean descending = false;
            if(order.getSelectedItem() == "Descending")
                descending = true;
            branchModel.setRows(facade.sortBranchesByLoad(descending));
        });
        changePriceButton.addActionListener(e -> {
            int laptopRow = laptopTable.getSelectedRow();
            int branchRow = branchTable.getSelectedRow();
            if (laptopRow < 0 || branchRow < 0) return;
            String price = JOptionPane.showInputDialog(this, "Change price");
            if (price == null) return;
            try {
                facade.changePrice(laptopModel.getRow(laptopRow), Double.parseDouble(price));
                laptopModel.setRows(facade.laptops(branchModel.getRow(branchRow)));
            } catch (InvalidPriceException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
            laptopModel.setRows(facade.laptops(branchModel.getRow(branchRow)));
        });
        changeConditionButton.addActionListener(e -> {
            int laptopRow = laptopTable.getSelectedRow();
            int branchRow = branchTable.getSelectedRow();
            if (laptopRow < 0 || branchRow < 0) return;
            JComboBox<LaptopCondition> condition = new JComboBox<>(LaptopCondition.values());
            JPanel form = new JPanel();
            form.add(new JLabel("Condition"));
            form.add(condition);
            int result = JOptionPane.showConfirmDialog(
                    this, form, "Change condition", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result != JOptionPane.OK_OPTION) return;

            facade.changeCondition(branchModel.getRow(branchRow), laptopModel.getRow(laptopRow),
                    condition.getItemAt(condition.getSelectedIndex()));
            laptopModel.setRows(facade.laptops(branchModel.getRow(branchRow)));
        });
        //filters
        add(filterField);
        filterField.addActionListener(e -> {
            int row = branchTable.getSelectedRow();
            if (row < 0) return;
            Branch branch = branchModel.getRow(row);
            laptopModel.setRows(facade.filter(branch, filterField.getText(), null));
        });
        filterField.addActionListener(e -> showFilteredLaptops());

        conditionFilter.addItem("All");
        for (LaptopCondition condition : LaptopCondition.values()) {
            conditionFilter.addItem(condition);
        }
        conditionFilter.setBounds(700, 60, 150, 30);
        add(conditionFilter);
        conditionFilter.addActionListener(e -> showFilteredLaptops());
        // label
        filterLabel.setBounds(500, 40, 200, 20);
        add(filterLabel);
        conditionLabel.setBounds(700, 40, 200, 20);
        add(conditionLabel);
    }
    void loadTable(TableModel model){
        model.setRows(new ArrayList<>(facade.branches().values()));
    }
    void showTable(JTable table, int x, int y, int width, int height){
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(x, y, width, height);
        add(scroll);
    }
    JButton createButton(String text, int posX, int posY, int width, int height){
        JButton button = new JButton(text);
        button.setBounds(posX, posY, width, height);
        return button;
    }
    JTextField createTextField(int posX, int posY, int width, int height) {
        JTextField textField = new JTextField();
        textField.setBounds(posX, posY, width, height);
        return textField;
    }
    void showFilteredLaptops() {
        int row = branchTable.getSelectedRow();
        if (row < 0) return;
        Object selected = conditionFilter.getSelectedItem();
        LaptopCondition condition = selected instanceof LaptopCondition value ? value : null;
        laptopModel.setRows(facade.filter(branchModel.getRow(row), filterField.getText(), condition));
    }
}
