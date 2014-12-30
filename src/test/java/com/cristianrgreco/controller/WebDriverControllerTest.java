package com.cristianrgreco.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.cristianrgreco.adapter.WebDriverAdapter;
import com.cristianrgreco.controller.WebDriverController;
import com.cristianrgreco.model.entity.PerformanceData;
import com.cristianrgreco.util.NumberFormatter;

@RunWith(MockitoJUnitRunner.class)
public class WebDriverControllerTest {
    private WebDriverController target;

    @Mock
    private WebDriverAdapter webDriverAdapter;
    @Spy
    private NumberFormatter numberFormatter;

    @Before
    public void setup() {
        this.target = new WebDriverController(this.webDriverAdapter, this.numberFormatter);
    }

    @Test
    public void testParseStartingPerformanceData() throws Exception {
        String data = "0 cookies\nper second : 0.0";
        doReturn(data).when(this.webDriverAdapter).getPerformanceString();

        PerformanceData expected = new PerformanceData(0.0, 0.1);
        PerformanceData actual = this.target.getPerformanceData();

        assertEquals(expected, actual);
    }

    @Test
    public void testParsePerformanceDataPlurality() throws Exception {
        String data = "1 cookie\nper second : 0.0";
        doReturn(data).when(this.webDriverAdapter).getPerformanceString();

        PerformanceData expected = new PerformanceData(1.0, 0.1);
        PerformanceData actual = this.target.getPerformanceData();

        assertEquals(expected, actual);
    }

    @Test
    public void testParseStandardPerformanceData() throws Exception {
        String data = "567 cookies\nper second : 5.8";
        doReturn(data).when(this.webDriverAdapter).getPerformanceString();

        PerformanceData expected = new PerformanceData(567.0, 5.8);
        PerformanceData actual = this.target.getPerformanceData();

        assertEquals(expected, actual);
    }

    @Test
    public void testParseLargePerformanceData() throws Exception {
        String data = "103.922 million\ncookies\nper second : 1.4 billion";
        doReturn(data).when(this.webDriverAdapter).getPerformanceString();

        PerformanceData expected = new PerformanceData(103922000.0, 1400000000.0);
        PerformanceData actual = this.target.getPerformanceData();

        assertEquals(expected, actual);
    }
}
