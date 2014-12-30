package com.cristianrgreco.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class InformationFrame {
    private static final String FRAME_TITLE = "Cookie Clicker";
    private static final String INITIALIZATION_TEXT = "Starting up";
    private static final String[] PRODUCT_TABLE_COLUMN_NAMES = { "Name", "Price", "CPS", "Efficiency" };
    private static final int PRODUCT_TABLE_ROW_COUNT = 10;
    private static final int PRODUCT_TABLE_COLUMN_COUNT = PRODUCT_TABLE_COLUMN_NAMES.length;

    private JFrame frame;
    private JPanel panel;
    private JLabel productLabel;
    private JProgressBar productProgressBar;
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private JScrollPane productTableScrollPane;

    public InformationFrame() {
        this.createAndShowGui();
    }

    private void createAndShowGui() {
        this.initialiseComponents();
        this.addAndPackComponents();
    }

    private void initialiseComponents() {
        this.frame = new JFrame(FRAME_TITLE);
        this.panel = new JPanel(new GridBagLayout());
        this.panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.productLabel = new JLabel();
        this.productLabel.setText(INITIALIZATION_TEXT);
        this.productProgressBar = new JProgressBar(0, 100);
        this.productProgressBar.setIndeterminate(true);
        this.initialiseProductTable();
    }

    private void initialiseProductTable() {
        this.productTable = new JTable() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.productTable.setFocusable(false);
        this.productTable.setRowSelectionAllowed(false);
        this.productTableScrollPane = new JScrollPane(this.productTable);
        this.productTableModel = new DefaultTableModel(PRODUCT_TABLE_ROW_COUNT, PRODUCT_TABLE_COLUMN_COUNT);
        this.productTableModel.setColumnIdentifiers(PRODUCT_TABLE_COLUMN_NAMES);
        this.productTable.setModel(this.productTableModel);
        Dimension productTableDimension = this.productTable.getPreferredSize();
        this.productTableScrollPane.setPreferredSize(new Dimension(productTableDimension.width, this.productTable
                .getRowHeight() * (PRODUCT_TABLE_ROW_COUNT + 2)));
    }

    private void addAndPackComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridy = 0;
        this.panel.add(this.productLabel, gbc);
        gbc.gridy = 1;
        this.panel.add(this.productProgressBar, gbc);
        gbc.gridy = 2;
        this.panel.add(this.productTableScrollPane, gbc);
        this.frame.getContentPane().add(this.panel);
        this.frame.pack();
        this.frame.setResizable(false);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }

    public JLabel getProductLabel() {
        return this.productLabel;
    }

    public JProgressBar getProductProgressBar() {
        return this.productProgressBar;
    }

    public DefaultTableModel getProductTableModel() {
        return this.productTableModel;
    }
}