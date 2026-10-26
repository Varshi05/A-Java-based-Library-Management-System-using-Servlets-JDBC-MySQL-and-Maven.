package com.library.util;

import java.util.ArrayList;
import java.util.List;

public final class BookValidator {

    public static final int MAX_LEN = 20;

    private BookValidator() { }

    /** Null-safe trim. */
    public static String clean(String s) {
        return s == null ? "" : s.trim();
    }

    public static List<String> validate(String bname, String author, String btype, String price) {
        List<String> errors = new ArrayList<>();
        checkText("Book name", bname, errors);
        checkText("Author", author, errors);
        checkText("Book type", btype, errors);

        if (price == null || price.trim().isEmpty()) {
            errors.add("Price is required.");
        } else if (!price.trim().matches("\\d{1,7}(\\.\\d{1,2})?")) {
            errors.add("Price must be a positive number with up to 2 decimals (for example 250.50).");
        }
        return errors;
    }

    private static void checkText(String label, String value, List<String> errors) {
        if (value == null || value.trim().isEmpty()) {
            errors.add(label + " is required.");
        } else if (value.trim().length() > MAX_LEN) {
            errors.add(label + " must be at most " + MAX_LEN + " characters.");
        }
    }

    /** Returns the id, or null if the text is not a positive whole number. */
    public static Integer parseId(String text) {
        if (text == null) {
            return null;
        }
        try {
            int id = Integer.parseInt(text.trim());
            return id > 0 ? id : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}