package com.cristianrgreco.controller;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;

import com.cristianrgreco.model.entity.Product;
import com.cristianrgreco.ui.InformationFrame;

public class InformationFrameController {
    private InformationFrame informationFrame;

    public InformationFrameController(InformationFrame informationFrame) {
        this.informationFrame = informationFrame;
    }

    public void setProductLabel(String productLabelText) {
        JLabel productLabel = this.informationFrame.getProductLabel();
        productLabel.setText(productLabelText);
    }

    public void setProductProgressBar(int percentageComplete) {
        JProgressBar productProgressBar = this.informationFrame.getProductProgressBar();
        if (productProgressBar.isIndeterminate()) {
            productProgressBar.setIndeterminate(false);
        }
        productProgressBar.setValue(percentageComplete);
    }

    public void updateProductTable(int productId, Product product) {
        DefaultTableModel productTableModel = this.informationFrame.getProductTableModel();
        int currentNumberOfRows = productTableModel.getRowCount();
        if (productId >= currentNumberOfRows) {
            this.addNewProductToTable(product);
        } else {
            productTableModel.setValueAt(product.getName(), productId, 0);
            productTableModel.setValueAt(product.getPrice(), productId, 1);
            productTableModel.setValueAt(product.getCookiesPerSecond(), productId, 2);
            productTableModel.setValueAt((int) product.getEfficiency(), productId, 3);
        }
    }

    private void addNewProductToTable(Product product) {
        DefaultTableModel productTableModel = this.informationFrame.getProductTableModel();
        productTableModel.addRow(new Object[] { product.getName(), product.getPrice(),
                product.getCookiesPerSecond(), (int) product.getEfficiency() });
    }
}
