package com.j256.ormlite.misc;

public class MultiplePrimaryKeyUtil {

    /**
     * From the values of multiple columns build
     * a String that merges them using the supplied
     * separator. This is useful for an ID column
     * that represents multiple columns in a custom getter.
     */
    public static String mergeIntoOneColumn(String separator, String... values) {
        StringBuilder resultBuilder = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            resultBuilder.append(values[i]);

            if (i + 1 != values.length) {
                resultBuilder.append(separator);
            }
        }

        return resultBuilder.toString();
    }

    /**
     * From the values of multiple columns build
     * a String that merges them using a semicolon as a separator.
     * This is useful for an ID column that represents multiple columns
     * in a custom getter.
     */
    public static String mergeIntoOneColumn(String... values) {
        return mergeIntoOneColumn(";", values);
    }

}
