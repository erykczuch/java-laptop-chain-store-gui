package com.agh.ui;

import com.agh.app.StoreFacade;
import com.agh.model.Branch;
import com.agh.model.Laptop;
import com.agh.model.LaptopCondition;
import com.agh.model.exception.BranchFullException;
import com.agh.model.exception.DuplicateLaptopException;
import com.agh.model.exception.InvalidPriceException;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StorePanel extends JPanel {
    String role;
    StoreFacade facade;
    JButton logOutButton = new JButton("Log out");
    JButton addBranchButton = new JButton("Add new branch");
    JButton removeBranchButton = new JButton("Remove branch");
    JButton addLaptopButton = new JButton("Add new laptop");
    JButton removeLaptopButton = new JButton("Remove laptop");
    JButton sortBranchesByLoadButton = new JButton("Sort branches by load");
    JButton changePriceButton = new JButton("Change price");
    JButton changeConditionButton = new JButton("Change condition");
    JTextField filterField = new JTextField(12);
    JLabel filterLabel = new JLabel("Filter by brand or model");
    JComboBox<Object> conditionFilter = new JComboBox<>();
    JLabel conditionLabel = new JLabel("Condition");

    TableModel<Branch> branchModel = new TableModel<>(
            new String[]{"Name", "Laptops", "Capacity"},
            List.of(
                    branch -> branch.branchName,
                    branch -> branch.laptopList.size(),
                    branch -> branch.maxCapacity
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

    StorePanel(String role, StoreFrame frame, StoreFacade facade) {
        this.role = role;
        this.facade = facade;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        setVisible(true);
        logOutButton.addActionListener(e -> frame.logOut());
        if (role == "worker")
            workerSetup();
        else if (role == "client")
            clientSetup();
    }

    void clientSetup() {
        buildCommonLayout();
        loadTable(branchModel);
        branchTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            showFilteredLaptops();
        });

        JButton reserveButton = new JButton("Reserve");
        JButton cancelReservationButton = new JButton("Cancel reservation");
        reserveButton.addActionListener(e -> {
            int laptopRow = laptopTable.getSelectedRow();
            int branchRow = branchTable.getSelectedRow();
            if (laptopRow < 0 || branchRow < 0) return;
            facade.reserveLaptop(laptopModel.getRow(laptopRow));
            showFilteredLaptops();
        });
        cancelReservationButton.addActionListener(e -> {
            int laptopRow = laptopTable.getSelectedRow();
            int branchRow = branchTable.getSelectedRow();
            if (laptopRow < 0 || branchRow < 0) return;
            facade.cancelReservation(laptopModel.getRow(laptopRow));
            showFilteredLaptops();
        });
        sortBranchesByLoadButton.addActionListener(e -> sortBranches());

        JPanel left = buttonColumn(reserveButton, cancelReservationButton);
        JPanel middle = buttonColumn(sortBranchesByLoadButton);
        add(buttonBar(left, middle, new JPanel()), BorderLayout.SOUTH);
        setupFilters();
    }

    void workerSetup() {
        buildCommonLayout();
        loadTable(branchModel);
        branchTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int row = branchTable.getSelectedRow();
            if (row < 0) return;
            Branch branch = branchModel.getRow(row);
            laptopModel.setRows(facade.laptops(branch));
        });

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
        sortBranchesByLoadButton.addActionListener(e -> sortBranches());
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

        JPanel left = buttonColumn(addBranchButton, removeBranchButton);
        JPanel middle = buttonColumn(addLaptopButton, removeLaptopButton);
        JPanel right = buttonColumn(sortBranchesByLoadButton, changePriceButton, changeConditionButton);
        add(buttonBar(left, middle, right), BorderLayout.SOUTH);
        setupFilters();
    }

    void buildCommonLayout() {
        JPanel top = new JPanel(new BorderLayout(10, 0));
        JPanel filters = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filters.add(filterLabel);
        filters.add(filterField);
        filters.add(conditionLabel);
        filters.add(conditionFilter);
        top.add(filters, BorderLayout.CENTER);
        top.add(logOutButton, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JPanel tables = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 10, 10);
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.weightx = 0.4;
        tables.add(new JScrollPane(branchTable), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        gbc.insets = new Insets(10, 0, 10, 0);
        tables.add(new JScrollPane(laptopTable), gbc);
        add(tables, BorderLayout.CENTER);
    }

    void setupFilters() {
        conditionFilter.removeAllItems();
        conditionFilter.addItem("All");
        for (LaptopCondition condition : LaptopCondition.values()) {
            conditionFilter.addItem(condition);
        }
        filterField.addActionListener(e -> showFilteredLaptops());
        conditionFilter.addActionListener(e -> showFilteredLaptops());
    }

    void sortBranches() {
        JComboBox<String> order = new JComboBox<>(new String[]{"Ascending", "Descending"});
        JPanel form = new JPanel();
        form.add(new JLabel("Order"));
        form.add(order);
        int result = JOptionPane.showConfirmDialog(
                this, form, "Sort branches", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;
        boolean descending = "Descending".equals(order.getSelectedItem());
        branchModel.setRows(facade.sortBranchesByLoad(descending));
    }

    JPanel buttonColumn(JButton... buttons) {
        JPanel column = new JPanel(new GridLayout(0, 1, 0, 10));
        for (JButton button : buttons) {
            column.add(button);
        }
        return column;
    }

    JPanel buttonBar(JPanel left, JPanel middle, JPanel right) {
        JPanel bar = new JPanel(new GridLayout(1, 3, 40, 0));
        bar.add(left);
        bar.add(middle);
        bar.add(right);
        return bar;
    }

    void loadTable(TableModel model) {
        model.setRows(new ArrayList<>(facade.branches().values()));
    }

    void showFilteredLaptops() {
        int row = branchTable.getSelectedRow();
        if (row < 0) return;
        Object selected = conditionFilter.getSelectedItem();
        LaptopCondition condition = selected instanceof LaptopCondition value ? value : null;
        laptopModel.setRows(facade.filter(branchModel.getRow(row), filterField.getText(), condition));
    }
}
