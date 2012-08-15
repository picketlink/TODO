package org.aerogear.todo.server.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateBuilder {

    private static DateBuilder dateBuilder = null;
    public static final String PATTERN = "yyyy-MM-dd";
    private Calendar calendar;

    private DateBuilder() {
    }

    public static DateBuilder newDateBuilder() {
        if (dateBuilder == null) {
            dateBuilder = new DateBuilder();
        }
        return dateBuilder;
    }

    public DateBuilder withCalendar(Calendar calendar) {
        this.calendar = calendar;
        return this;
    }

    /**
     * Workaround for date build issue
     *
     * @TODO: Refactor it to use consolidated solutions like Joda time
     */
    public String build() {
        calendar.add(Calendar.DATE, 1);

        return new SimpleDateFormat(PATTERN).format(calendar.getTime());
    }
}
