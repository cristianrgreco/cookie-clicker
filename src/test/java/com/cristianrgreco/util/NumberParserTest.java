package com.cristianrgreco.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NumberParserTest {
    private NumberParser target;

    @Before
    public void setup() {
        this.target = new NumberParser();
    }

    @Test
    public void testNumberWithSpace() throws Exception {
        String number = "5 ";

        double expected = 5;
        double actual = this.target.parseDoubleWithAppendedText(number);

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testNumberWithComma() throws Exception {
        String number = "10,000";

        double expected = 10000;
        double actual = this.target.parseDoubleWithAppendedText(number);

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testNumberWithDot() throws Exception {
        String number = "10000.0";

        double expected = 10000;
        double actual = this.target.parseDoubleWithAppendedText(number);

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testNumberWithText() throws Exception {
        String number = "10 million";

        double expected = 10000000.0;
        double actual = this.target.parseDoubleWithAppendedText(number);

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testComplicatedNumberWithText() throws Exception {
        String number = "1.4 sextillion";

        double expected = 1400000000000000000000.0;
        double actual = this.target.parseDoubleWithAppendedText(number);

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testNumberWithTextAndSpaces() throws Exception {
        String number = "10 million ";

        double expected = 10000000.0;
        double actual = this.target.parseDoubleWithAppendedText(number);

        assertEquals(expected, actual, 0);
    }

    @Test(expected = NumberFormatException.class)
    public void testEmptyStringThrowsNumberFormatException() throws Exception {
        String number = "";

        this.target.parseDoubleWithAppendedText(number);
    }
}
