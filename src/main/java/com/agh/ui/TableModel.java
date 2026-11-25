package com.agh.ui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TableModel<T> extends AbstractTableModel {
    private final String[] columns;
    private final List<T> rows = new ArrayList<>();
    private final List<Function<T, Object>> values;

    public TableModel(String[] columns, List<Function<T, Object>> values) {
        this.columns = columns;
        this.values = values;
    }
    @Override
    public int getRowCount() {
        return rows.size();
    }
    @Override
    public int getColumnCount() {
        return columns.length;
    }
    @Override
    public String getColumnName(int column) {
        return columns[column];
    }
    @Override
    public Object getValueAt(int row, int column) {
        return values.get(column).apply(rows.get(row));
    }
    public T getRow(int row) {
        return rows.get(row);
    }
    public void setRows(List<T> next){
        rows.clear();
        rows.addAll(next);
        fireTableDataChanged();
    }

}
