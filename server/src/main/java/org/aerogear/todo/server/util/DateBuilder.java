package org.aerogear.todo.server.util;

import java.util.Calendar;

public class DateBuilder {

    private static DateBuilder dateBuilder = null;
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
        return calendar.get(Calendar.YEAR) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DATE);

    }
}
