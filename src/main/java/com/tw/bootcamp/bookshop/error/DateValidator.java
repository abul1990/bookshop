package com.tw.bootcamp.bookshop.error;

import java.sql.Date;

public class DateValidator {
    public static Date createDate(String date) throws InvalidDateException {
        validateDateFormat(date);
        try {
            return Date.valueOf(date);

        } catch (IllegalArgumentException exception) {
            throw new InvalidDateException("Date Should Be In Format As YYYY-MM-DD");
        }
    }

    private static void validateDateFormat(String date) throws InvalidDateException {
        if (!date.matches("^(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$")) {
            throw new InvalidDateException("Please Enter Date In Format As YYYY-MM-DD");
        }
    }

    public static void validateDateRange(Date startDate, Date endDate) throws InvalidDateException {
        if (startDate.after(new Date(System.currentTimeMillis())) || endDate.after(new Date(System.currentTimeMillis())))
            throw new InvalidDateException("Start Date and End Date should be equal or before current date");
        if (startDate.after(endDate)) throw new InvalidDateException("Start Date Should be before End Date");
    }
}
