package com.cristianrgreco.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.cristianrgreco.model.entity.PerformanceData;

@RunWith(MockitoJUnitRunner.class)
public class PerformanceParserTest {
    private PerformanceParser target;

    @Spy
    private NumberFormatter numberFormatter;

    @Before
    public void setup() {
        this.target = new PerformanceParser(this.numberFormatter);
    }

    @Test
    public void testParseStartingPerformanceData() throws Exception {
        String data = "0 cookies\nper second : 0.0";

        PerformanceData expected = new PerformanceData(0.0, 0.1);
        PerformanceData actual = this.target.parsePerformanceData(data);

        assertEquals(expected, actual);
    }

    @Test
    public void testParsePerformanceDataPlurality() throws Exception {
        String data = "1 cookie\nper second : 0.0";

        PerformanceData expected = new PerformanceData(1.0, 0.1);
        PerformanceData actual = this.target.parsePerformanceData(data);

        assertEquals(expected, actual);
    }

    @Test
    public void testParseStandardPerformanceData() throws Exception {
        String data = "567 cookies\nper second : 5.8";

        PerformanceData expected = new PerformanceData(567.0, 5.8);
        PerformanceData actual = this.target.parsePerformanceData(data);

        assertEquals(expected, actual);
    }

    @Test
    public void testParseLargePerformanceData() throws Exception {
        String data = "103.922 million\ncookies\nper second : 1.4 billion";

        PerformanceData expected = new PerformanceData(103922000.0, 1400000000.0);
        PerformanceData actual = this.target.parsePerformanceData(data);

        assertEquals(expected, actual);
    }
}
