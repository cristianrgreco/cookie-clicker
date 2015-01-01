package com.cristianrgreco.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.cristianrgreco.controller.InformationFrameController;
import com.cristianrgreco.model.builder.ProductBuilder;
import com.cristianrgreco.model.entity.Product;
import com.cristianrgreco.ui.InformationFrame;

@RunWith(MockitoJUnitRunner.class)
public class InformationFrameControllerTest {
    private InformationFrameController target;

    @Spy
    private InformationFrame informationFrame;

    @Before
    public void setup() {
        this.target = new InformationFrameController(this.informationFrame);
    }

    @Test
    public void testSetProductLabel() throws Exception {
        String expected = "Test Label";

        this.target.setProductLabel(expected);
        String actual = this.informationFrame.getProductLabel().getText();

        assertEquals(expected, actual);
    }

    @Test
    public void testSetProgressBarValue() throws Exception {
        int expected = 50;

        this.target.setProductProgressBar(expected);
        int actual = this.informationFrame.getProductProgressBar().getValue();

        assertEquals(expected, actual);
    }

    @Test
    public void testSetProgressBarValueRemovesIndeterminateStateIfSet() throws Exception {
        boolean expected = false;
        this.informationFrame.getProductProgressBar().setIndeterminate(true);

        this.target.setProductProgressBar(50);
        boolean actual = this.informationFrame.getProductProgressBar().isIndeterminate();

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateTableCorrectlyAddsProductInfoToTable() throws Exception {
        String expectedName = "Product 1";
        double expectedPrice = 5.0;
        double expectedCookiesPerSecond = 0.5;
        int expectedEfficiency = -5;
        int productId = 0;
        Product product = new ProductBuilder().setName(expectedName).setPrice(expectedPrice)
                .setCookiesPerSecond(expectedCookiesPerSecond).setEfficiency(expectedEfficiency).build();

        this.target.updateProductTable(productId, product);
        String actualName = (String) this.informationFrame.getProductTableModel().getValueAt(productId, 0);
        double actualPrice = (double) this.informationFrame.getProductTableModel().getValueAt(productId, 1);
        double actualCookiesPerSecond = (double) this.informationFrame.getProductTableModel().getValueAt(
                productId, 2);
        int actualEfficiency = (int) this.informationFrame.getProductTableModel().getValueAt(productId, 3);

        assertEquals(expectedName, actualName);
        assertEquals(expectedPrice, actualPrice, 0);
        assertEquals(expectedCookiesPerSecond, actualCookiesPerSecond, 0);
        assertEquals(expectedEfficiency, actualEfficiency);
    }

    @Test
    public void testUpdateTableUpdatesExistingRowIfProductAlreadyExists() throws Exception {
        int productId = 0;
        Product product1 = new ProductBuilder().setName("Product 1").setPrice(5.0).setCookiesPerSecond(0.5)
                .setEfficiency(-5).build();
        Product product2 = new ProductBuilder().setName("Product 2").setPrice(5.0).setCookiesPerSecond(0.5)
                .setEfficiency(-5).build();

        this.target.updateProductTable(productId, product1);
        assertEquals("Product 1", this.informationFrame.getProductTableModel().getValueAt(productId, 0));
        this.target.updateProductTable(productId, product2);
        assertEquals("Product 2", this.informationFrame.getProductTableModel().getValueAt(productId, 0));
    }

    @Test
    public void testCreatesNewRowIfProductIdIsGreaterThanCurrentNumberOfRows() throws Exception {
        int productId = 11;
        Product product = new ProductBuilder().setName("Product").setPrice(5.0).setCookiesPerSecond(0.5)
                .setEfficiency(-5).build();

        assertEquals(10, this.informationFrame.getProductTableModel().getRowCount());
        this.target.updateProductTable(productId, product);
        assertEquals(11, this.informationFrame.getProductTableModel().getRowCount());
    }
}
