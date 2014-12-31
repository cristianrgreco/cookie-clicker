package com.cristianrgreco.model.builder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.cristianrgreco.model.entity.Product;

@RunWith(MockitoJUnitRunner.class)
public class ProductBuilderTest {
    @Test
    public void testAllSettersReturnCorrectValues() throws Exception {
        String expectedName = "Name";
        double expectedPrice = 10.0;
        double expectedCookiesPerSecond = 1.0;
        double expectedEfficiency = -0.5;

        Product product = new ProductBuilder().setName(expectedName).setPrice(expectedPrice)
                .setCookiesPerSecond(expectedCookiesPerSecond).setEfficiency(expectedEfficiency).build();
        String actualName = product.getName();
        double actualPrice = product.getPrice();
        double actualCookiesPerSecond = product.getCookiesPerSecond();
        double actualEfficiency = product.getEfficiency();

        assertEquals(expectedName, actualName);
        assertEquals(expectedPrice, actualPrice, 0);
        assertEquals(expectedCookiesPerSecond, actualCookiesPerSecond, 0);
        assertEquals(expectedEfficiency, actualEfficiency, 0);
    }
}
