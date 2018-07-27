package com.obss.javasummerschool.bookportal.util;

import com.obss.javasummerschool.bookportal.exception.BadRequestException;

public class PageValidator {

    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "30";

    public static final int MAX_PAGE_SIZE = 50;

    private PageValidator() {

    }

    public static void validate(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > PageValidator.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + PageValidator.MAX_PAGE_SIZE);
        }
    }
}
