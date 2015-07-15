package com.lge.lai.parser;

import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ParserPropertiesTest {
    ParserProperties properties;

    @Before
    public void setUp() {
        properties = new ParserProperties("LGAppIF.parser");
    }

    @Test
    public void checkInstanceNotNull() {
        assertNotNull(properties);
    }

    @Test
    public void getReportAsProperty() {
        assertThat(properties.getProperty("report", true), either(is("console")).or(is("excel")));
    }

    @Test
    public void getWriteDatabaseAsProperty() {
        assertThat(Boolean.valueOf(properties.getProperty("write.db", true)),
                either(is(true)).or(is(false)));
    }
}
