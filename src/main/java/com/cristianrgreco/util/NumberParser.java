package com.cristianrgreco.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class NumberParser {
    private static final NumberFormat NUMBER_FORMATTER = DecimalFormat.getInstance(Locale.US);

    public double parseDoubleWithAppendedText(String numberString) throws ParseException {
        String[] parts = numberString.split(" ");
        String rawNumber = parts[0].trim();
        if (rawNumber.length() == 0) {
            throw new NumberFormatException();
        }
        double number = NUMBER_FORMATTER.parse(rawNumber).doubleValue();
        if (parts.length > 1) {
            String rawUnit = parts[1].trim();
            double unit = this.convertUnitTextToNumber(rawUnit);
            number *= unit;
        }
        return number;
    }

    private double convertUnitTextToNumber(String unitText) {
        double number = 1;
        switch (unitText) {
        case "million":
            number = 1000000.0;
            break;
        case "billion":
            number = 1000000000.0;
            break;
        case "trillion":
            number = 1000000000000.0;
            break;
        case "quadrillion":
            number = 1000000000000000.0;
            break;
        case "quintillion":
            number = 1000000000000000000.0;
            break;
        case "sextillion":
            number = 1000000000000000000000.0;
            break;
        case "septillion":
            number = 1000000000000000000000000.0;
            break;
        case "octillion":
            number = 1000000000000000000000000000.0;
            break;
        case "nonillion":
            number = 1000000000000000000000000000000.0;
            break;
        case "decillion":
            number = 1000000000000000000000000000000000.0;
            break;
        }
        return number;
    }
}
