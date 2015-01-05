package com.cristianrgreco.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import com.cristianrgreco.adapter.WebDriverAdapter;
import com.cristianrgreco.model.builder.ProductBuilder;
import com.cristianrgreco.model.entity.PerformanceData;
import com.cristianrgreco.model.entity.Product;
import com.cristianrgreco.util.NumberParser;
import com.cristianrgreco.util.PerformanceParser;

@RunWith(MockitoJUnitRunner.class)
public class WebDriverControllerTest {
    private WebDriverController target;

    @Mock
    private WebDriverAdapter webDriverAdapter;
    @Spy
    private NumberParser numberParser;
    @Spy
    private PerformanceParser performanceParser = new PerformanceParser(this.numberParser);

    @Before
    public void setup() {
        this.target = new WebDriverController(this.webDriverAdapter, this.numberParser, this.performanceParser);
    }

    @Test
    public void testConnectToTargetUrl() throws Exception {
        this.target.connectToTargetUrl();
        verify(this.webDriverAdapter, times(1)).connectToTargetUrl();
    }

    @Test
    public void testClickOnBigCookie() throws Exception {
        this.target.clickOnBigCookie();
        verify(this.webDriverAdapter, times(1)).clickOnBigCookie();
    }

    @Test
    public void testGetListOfUnlockedProducts() throws Exception {
        List<WebElement> expected = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            expected.add(null);
        }
        doReturn(expected).when(this.webDriverAdapter).getListOfUnlockedProducts();

        List<WebElement> actual = this.target.getListOfUnlockedProducts();

        assertEquals(expected, actual);
    }

    @Test
    public void testPurchaseUnlockedProduct() throws Exception {
        int expectedProductId = 10;

        this.target.purchaseUnlockedProduct(expectedProductId);

        verify(this.webDriverAdapter, times(1)).purchaseUnlockedProduct(expectedProductId);
    }

    @Test
    public void testConvertProductWebElementToProductObjectNoException() throws Exception {
        Product expected = new ProductBuilder().setName("Product").setPrice(5.0).setCookiesPerSecond(0.5).build();

        doReturn("Product").when(this.webDriverAdapter).getNameOfUnlockedProduct(any(Integer.class),
                any(WebElement.class));
        doReturn("5.0").when(this.webDriverAdapter).getPriceOfUnlockedProduct(any(Integer.class),
                any(WebElement.class));
        doReturn("0.5").when(this.webDriverAdapter).getCookiesPerSecondOfUnlockedProduct(any(Integer.class),
                any(WebElement.class));
        doReturn(5.0).when(this.numberParser).parseDoubleWithAppendedText("5.0");
        doReturn(0.5).when(this.numberParser).parseDoubleWithAppendedText("0.5");

        Product actual = this.target.createProductObjectFromWebPage(0, null);

        assertEquals(expected, actual);
    }

    @Test
    public void testConvertProductWebElementToProductObjectEstimatesSpecIfNoProductInfoExists() throws Exception {
        double estimatedCookiesPerSecond = 0.025;
        Product expected = new ProductBuilder().setName("Product").setPrice(5.0)
                .setCookiesPerSecond(estimatedCookiesPerSecond).build();
        doReturn("Product").when(this.webDriverAdapter).getNameOfUnlockedProduct(any(Integer.class),
                any(WebElement.class));
        doReturn("5.0").when(this.webDriverAdapter).getPriceOfUnlockedProduct(any(Integer.class),
                any(WebElement.class));
        doThrow(NoSuchElementException.class).when(this.webDriverAdapter).getCookiesPerSecondOfUnlockedProduct(
                any(Integer.class), any(WebElement.class));

        Product actual = this.target.createProductObjectFromEstimate(0, null);

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testConvertProductWebElementToProductObjectThrowsExceptionIfNonRecoverableExceptionRaised()
            throws Exception {
        double estimatedCookiesPerSecond = 0.025;
        Product expected = new ProductBuilder().setName("Product").setPrice(5.0)
                .setCookiesPerSecond(estimatedCookiesPerSecond).build();
        doReturn("Product").when(this.webDriverAdapter).getNameOfUnlockedProduct(any(Integer.class),
                any(WebElement.class));
        doReturn("5.0").when(this.webDriverAdapter).getPriceOfUnlockedProduct(any(Integer.class),
                any(WebElement.class));
        doThrow(NumberFormatException.class).when(this.webDriverAdapter).getCookiesPerSecondOfUnlockedProduct(
                any(Integer.class), any(WebElement.class));

        Product actual = this.target.createProductObjectFromWebPage(0, null);

        assertEquals(expected, actual);
    }

    @Test
    public void testConvertProductWebElementToProductObjectRetriesIfStaleElementRaised() throws Exception {
        when(this.webDriverAdapter.getNameOfUnlockedProduct(any(Integer.class), any(WebElement.class))).thenThrow(
                new StaleElementReferenceException(null)).thenReturn("Product");
        doReturn("5.0").when(this.webDriverAdapter).getPriceOfUnlockedProduct(any(Integer.class),
                any(WebElement.class));
        doReturn("0.5").when(this.webDriverAdapter).getCookiesPerSecondOfUnlockedProduct(any(Integer.class),
                any(WebElement.class));
        doReturn(5.0).when(this.numberParser).parseDoubleWithAppendedText("5.0");
        doReturn(0.5).when(this.numberParser).parseDoubleWithAppendedText("0.5");

        this.target.createProductObjectFromWebPage(0, null);

        verify(this.webDriverAdapter, times(2)).getNameOfUnlockedProduct(0, null);
    }

    @Test
    public void testGetPerformanceData() throws Exception {
        PerformanceData expected = new PerformanceData(1.0, 0.1);
        String performanceString = "1 cookie\nper second : 0.1";
        doReturn(performanceString).when(this.webDriverAdapter).getPerformanceString();
        doReturn(expected).when(this.performanceParser).parsePerformanceData(performanceString);

        PerformanceData actual = this.target.getPerformanceData();

        assertEquals(expected, actual);
    }
}
