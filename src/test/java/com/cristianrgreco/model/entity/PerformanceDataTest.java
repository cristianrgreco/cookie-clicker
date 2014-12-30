package com.cristianrgreco.model.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PerformanceDataTest {
    @Test
    public void testPerformanceDataEqualityEqual() throws Exception {
        PerformanceData data1 = new PerformanceData(1.0, 5.5);
        PerformanceData data2 = new PerformanceData(1.0, 5.5);

        assertEquals(data1, data2);
        assertEquals(data1.hashCode(), data2.hashCode());
    }

    @Test
    public void testPerformanceDataEqualityNotEqual() throws Exception {
        PerformanceData data1 = new PerformanceData(1.0, 5.5);
        PerformanceData data2 = new PerformanceData(1.1, 5.5);

        assertNotEquals(data1, data2);
        assertNotEquals(data1.hashCode(), data2.hashCode());
    }
}
