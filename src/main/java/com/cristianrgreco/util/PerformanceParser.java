package com.cristianrgreco.util;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.cristianrgreco.model.entity.PerformanceData;

public class PerformanceParser {
    private static final Pattern PERFORMANCE_STRING = Pattern.compile("(.*?)cookie.*?: (.*)", Pattern.DOTALL);
    private static final Logger LOGGER = Logger.getLogger(PerformanceParser.class);

    private NumberParser numberParser;

    public PerformanceParser(NumberParser numberParser) {
        this.numberParser = numberParser;
    }

    public PerformanceData parsePerformanceData(String performanceDataString) {
        Matcher matcher = PERFORMANCE_STRING.matcher(performanceDataString);
        matcher.find();
        String numberOfCookiesString = matcher.group(1);
        double numberOfCookies = this.parseNumberOfCookies(numberOfCookiesString);
        String cookiesPerSecondString = matcher.group(2);
        double cookiesPerSecond = this.parseCookiesPerSecond(cookiesPerSecondString);
        PerformanceData performanceData = new PerformanceData(numberOfCookies, cookiesPerSecond);
        LOGGER.debug("Retrieved the following performance data: " + performanceData);
        return performanceData;
    }

    private double parseNumberOfCookies(String numberOfCookiesString) {
        double numberOfCookies = -1;
        try {
            numberOfCookies = this.numberParser.parseDoubleWithAppendedText(numberOfCookiesString);
        } catch (ParseException e) {
            LOGGER.error(null, e);
        }
        return numberOfCookies;
    }

    private double parseCookiesPerSecond(String cookiesPerSecondString) {
        double cookiesPerSecond = -1;
        try {
            cookiesPerSecond = this.numberParser.parseDoubleWithAppendedText(cookiesPerSecondString);
            if (cookiesPerSecond == 0.0) {
                cookiesPerSecond = 0.1;
            }
        } catch (ParseException e) {
            LOGGER.error(null, e);
        }
        return cookiesPerSecond;
    }
}
