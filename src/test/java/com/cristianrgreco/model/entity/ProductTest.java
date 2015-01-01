package com.cristianrgreco.model.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.cristianrgreco.model.builder.ProductBuilder;

@RunWith(MockitoJUnitRunner.class)
public class ProductTest {
    @Test
    public void testProductEqualityTrue() {
        Product product1 = new ProductBuilder().setName("Product").setPrice(5.0).setCookiesPerSecond(0.5)
                .setEfficiency(-5).build();
        Product product2 = new ProductBuilder().setName("Product").setPrice(5.0).setCookiesPerSecond(0.5)
                .setEfficiency(-5).build();

        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());
    }

    @Test
    public void testProductEqualityFalse() {
        Product product1 = new ProductBuilder().setName("Product 1").setPrice(5.0).setCookiesPerSecond(0.5)
                .setEfficiency(-5).build();
        Product product2 = new ProductBuilder().setName("Product 2").setPrice(5.0).setCookiesPerSecond(0.5)
                .setEfficiency(-5).build();

        assertNotEquals(product1, product2);
        assertNotEquals(product1.hashCode(), product2.hashCode());
    }
}
