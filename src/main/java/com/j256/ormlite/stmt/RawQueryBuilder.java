package com.j256.ormlite.stmt;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @see #whereFromRaw(RuntimeExceptionDao, String, Object...)
 * @see #whereFromRaw(Dao, String, Object...)
 */
public class RawQueryBuilder {

    // ??  -> Table / column name
    // ?   -> escaped value

    private static final Pattern escapePattern = Pattern.compile("\\?\\?\\?|\\?\\?|\\?");
    public static final String SQL_NAME_PLACEHOLDER = "??";

    /**
     * Iterate over the array and replace all arrays inside the
     * array with lists.
     */
    private static void replaceArraysWithLists(Object[] arr) {
        for (int i = 0; i < arr.length; i++) {
            Object arg = arr[i];
            if (arg instanceof byte[]) {
                byte[] argCast = (byte[]) arg;
                arr[i] = Arrays.asList(argCast);
            } else if (arg instanceof short[]) {
                short[] argCast = (short[]) arg;
                arr[i] = Arrays.asList(argCast);
            } else if (arg instanceof int[]) {
                int[] argCast = (int[]) arg;
                arr[i] = Arrays.asList(argCast);
            } else if (arg instanceof long[]) {
                long[] argCast = (long[]) arg;
                arr[i] = Arrays.asList(argCast);
            } else if (arg instanceof float[]) {
                float[] argCast = (float[]) arg;
                arr[i] = Arrays.asList(argCast);
            } else if (arg instanceof double[]) {
                double[] argCast = (double[]) arg;
                arr[i] = Arrays.asList(argCast);
            } else if (arg instanceof Object[]) {
                Object[] argCast = (Object[]) arg;
                arr[i] = Arrays.asList(argCast);
            }
        }
    }

    /**
     * Get the index of the first iterable in the the array.
     */
    private static int iterableInObjects(Object[] objects) {
        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i];
            if (object instanceof Iterable) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Inline the what array into the 'into' array at the position
     * 'at'. The into array is not modified.
     */
    private static Object[] insert(Object[] into, Object[] what, int at) {
        Object[] newArr = new Object[into.length - 1 + what.length];
        int newPointer = 0;
        int intoPointer = 0;
        for (; newPointer < at; ) {
            newArr[newPointer++] = into[intoPointer++];
        }
        for (int whatPointer = 0; whatPointer < what.length; whatPointer++) {
            newArr[newPointer++] = what[whatPointer];
        }
        intoPointer++; // Skip the inlined element
        for (; intoPointer < into.length; ) {
            newArr[newPointer++] = into[intoPointer++];
        }

        return newArr;
    }

    /**
     * Build a {@link Where} object using a raw where clause without the leading WHERE.
     * <p>
     * Example: <code>whereFromRaw(fooDao, "?? = ? OR ?? = ?? OR ?? = ?", Foo.A, "a", Foo.A, Foo.B, Foo.A, null)</code>.
     * Null arguments are automatically replaced with "IS NULL" in the clause. "??" denotes an SQL name
     * (for example a table or column name), "?" denotes an escaped SQL argument.
     *
     * @see SqlType SqlType for supported object types
     */
    public static <T, ID> RuntimeExceptionWhere<T, ID> whereFromRaw(final RuntimeExceptionDao<T, ID> dao, String whereClause, Object... args) {
        return new RuntimeExceptionWhere<T, ID>(whereFromRaw(dao.getWrappedDao(), whereClause, args));
    }


    /**
     * Build a {@link Where} object using a raw where clause without the leading WHERE.
     * <p>
     * Example: <code>whereFromRaw(fooDao, "?? = ? OR ?? = ?? OR ?? = ?", Foo.A, "a", Foo.A, Foo.B, Foo.A, null)</code>.
     * Null arguments are automatically replaced with "IS NULL" in the clause. "??" denotes an SQL name
     * (for example a table or column name), "?" denotes an escaped SQL argument.
     *
     * @see SqlType SqlType for supported object types
     */
    public static <T, ID> Where<T, ID> whereFromRaw(final Dao<T, ID> dao, String whereClause, Object... args) {
        replaceArraysWithLists(args);
        StringBuilder whereClauseBuilder = new StringBuilder(whereClause);

        int iterableIndex;
        while ((iterableIndex = iterableInObjects(args)) != -1) {
            // Copy iterable contents into a list
            Iterable<?> iterable = (Iterable<?>) args[iterableIndex];
            List<Object> objects = new ArrayList<Object>();
            for (Object o : iterable) {
                objects.add(o);
            }

            // Build (?, ?, ..., ?) for the number of objects in the iterable
            StringBuilder sb = new StringBuilder("(");
            for (int i = 0; i < objects.size(); i++) {
                sb.append("?");
                if (i + 1 < objects.size()) {
                    sb.append(", ");
                }
            }
            sb.append(")");

            // Find the corresponding ? in the clause
            // and replace it with the n-tuple
            Matcher matcher = escapePattern.matcher(whereClause);
            int i = 0;
            while (matcher.find()) {
                if (i == iterableIndex) {
                    whereClauseBuilder.replace(matcher.start(), matcher.end(), sb.toString());
                    break;
                }
                i++;
            }

            // Inline the iterable into the args
            args = insert(args, objects.toArray(), iterableIndex);
        }

        String iterableInlinedClause = whereClauseBuilder.toString();

        // Split args into escaped args and table / column names
        List<Object> escapedArgs = new ArrayList<Object>();
        List<String> sqlNameArgs = new ArrayList<String>();
        Matcher matcher = escapePattern.matcher(iterableInlinedClause);
        int i = 0;
        while (matcher.find()) {
            if (matcher.group().equals(SQL_NAME_PLACEHOLDER)) {
                sqlNameArgs.add((String) args[i]);
            } else {
                escapedArgs.add(args[i]);
            }
            i++;
        }

        if (escapedArgs.size() + sqlNameArgs.size() != args.length) {
            throw new IllegalArgumentException("Arguments in clause must match the number of provided arguments!");
        }

        String tableName = DatabaseTableConfig.extractTableName(dao.getDataClass());
        String inlinedNamesClause = inlineTableNameArgs(iterableInlinedClause, sqlNameArgs, tableName);

        // Determine types or remaining args, put into argumentholders for where.raw
        List<ArgumentHolder> argumentHolders = toArgumentHolders(escapedArgs);

        String withIsNull = replaceEqNullWithIsNull(inlinedNamesClause, argumentHolders);
        filterNonNullArgumentHolder(argumentHolders);

        return dao.queryBuilder().where().raw(withIsNull, argumentHolders.toArray(new ArgumentHolder[0]));
    }

    /**
     * Map the argument objects that are associated with "?"
     * in the SQL statement to argument holders by identifying their types.
     */
    private static List<ArgumentHolder> toArgumentHolders(List<Object> toBeEscapedArgs) {
        List<ArgumentHolder> argumentHolders = new ArrayList<ArgumentHolder>(toBeEscapedArgs.size());
        for (Object toBeEscaped : toBeEscapedArgs) {
            argumentHolders.add(toArgumentHolder(toBeEscaped));
        }
        return argumentHolders;
    }

    /**
     * Replace occurrences of "XX = ?" where ? is a {@link NullArgHolder}
     * with "XX IS NULL" and return the modified clause. The list of
     * argumentHolders is not modified.
     */
    private static String replaceEqNullWithIsNull(String statement, List<ArgumentHolder> argumentHolders) {
        StringBuilder nullClauseBuilder = new StringBuilder(statement);
        Matcher argumentMatcher = Pattern.compile("\\?").matcher(statement);
        List<MatchResult> matches = toMatchResults(argumentMatcher);
        for (int match = matches.size() - 1; match > -1; match--) {
            int argumentEndIndex = matches.get(match).end();
            ArgumentHolder argumentHolder = argumentHolders.get(match);
            if (argumentHolder instanceof NullArgHolder) {
                MatchResult matchResult = matches.get(match);
                int groupIndex = matchResult.start();
                Pattern equalsPattern = Pattern.compile("(\\s)+=(\\s)+");
                // Find last pattern occurrence ending before groupIndex
                String part = nullClauseBuilder.substring(0, groupIndex);
                Matcher equalsMatcher = equalsPattern.matcher(part);
                List<MatchResult> equalsMatches = toMatchResults(equalsMatcher);
                MatchResult lastMatch = equalsMatches.get(equalsMatches.size() - 1);
                int start = lastMatch.start();
                String replacement = " IS NULL ";
                nullClauseBuilder.replace(start, argumentEndIndex, replacement);
            }
        }
        return nullClauseBuilder.toString();
    }

    /**
     * Remove {@link NullArgHolder}s from the list.
     */
    private static void filterNonNullArgumentHolder(List<ArgumentHolder> argumentHolders) {
        Iterator<ArgumentHolder> argumentHolderIterator = argumentHolders.iterator();
        while (argumentHolderIterator.hasNext()) {
            if (argumentHolderIterator.next() instanceof NullArgHolder) {
                argumentHolderIterator.remove();
            }
        }
    }

    /**
     * Returns a list of all matches.
     */
    private static List<MatchResult> toMatchResults(Matcher matcher) {
        List<MatchResult> result = new ArrayList<MatchResult>();
        while (matcher.find()) {
            result.add(matcher.toMatchResult());
        }
        return result;
    }

    /**
     * Determine the type of the object and return an
     * appropriate {@link ArgumentHolder}.
     */
    private static ArgumentHolder toArgumentHolder(Object arg) {
        if (arg == null) {
            return new NullArgHolder();
        }

        if (arg instanceof String) {
            return new SelectArg(SqlType.STRING, arg);
        }
        if (arg instanceof Byte) {
            return new SelectArg(SqlType.BYTE, arg);
        }
        if (arg instanceof Short) {
            return new SelectArg(SqlType.SHORT, arg);
        }
        if (arg instanceof Integer) {
            return new SelectArg(SqlType.INTEGER, arg);
        }
        if (arg instanceof Long) {
            return new SelectArg(SqlType.LONG, arg);
        }
        if (arg instanceof Float) {
            return new SelectArg(SqlType.FLOAT, arg);
        }
        if (arg instanceof Double) {
            return new SelectArg(SqlType.DOUBLE, arg);
        }

        throw new IllegalArgumentException("Unsupported arg type");
    }

    /**
     * Replace '??' in the clause with the elements from the list.
     */
    private static String inlineTableNameArgs(String clause, List<String> tableNameArgs, String tableName) {
        String quotedSqlNamePlaceHolder = Pattern.quote(SQL_NAME_PLACEHOLDER);
        Queue<String> tableNameQueue = new LinkedList<String>(tableNameArgs);

        for (int indexOfPlaceHolder = clause.indexOf(SQL_NAME_PLACEHOLDER);
             indexOfPlaceHolder > -1;
             indexOfPlaceHolder = clause.indexOf(SQL_NAME_PLACEHOLDER)) {
            String sqlName = tableNameQueue.poll();
            clause = clause.replaceFirst(quotedSqlNamePlaceHolder, String.format("`%s`.`%s`", tableName, sqlName));
        }

        return clause;
    }


}
