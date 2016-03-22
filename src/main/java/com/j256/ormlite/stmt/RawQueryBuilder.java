package com.j256.ormlite.stmt;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.field.SqlType;
import com.sun.istack.internal.NotNull;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RawQueryBuilder {

    // ??  -> Table / column name
    // ?   -> escaped value

    private static final Pattern escapePattern = Pattern.compile("\\?\\?\\?|\\?\\?|\\?");

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

    private static int iterableInObjects(Object[] objects) {
        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i];
            if (object instanceof Iterable) {
                return i;
            }
        }
        return -1;
    }

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

    public static <T, ID> RuntimeExceptionWhere<T, ID> whereFromRaw(final RuntimeExceptionDao<T, ID> dao, String whereClause, Object... args) throws SQLException {
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
        List<String> tableNameArgs = new ArrayList<String>();
        Matcher matcher = escapePattern.matcher(iterableInlinedClause);
        int i = 0;
        while (matcher.find()) {
            if (matcher.group().equals("??")) {
                tableNameArgs.add((String) args[i]);
            } else {
                escapedArgs.add(args[i]);
            }
            i++;
        }

        if (escapedArgs.size() + tableNameArgs.size() != args.length) {
            throw new IllegalArgumentException("Arguments in clause must match the number of provided arguments!");
        }

        String inlinedClause = inlineTableNameArgs(iterableInlinedClause, tableNameArgs);

        // Determine types or remaining args, put into argumentholders for where.raw
        List<ArgumentHolder> argumentHolders = new ArrayList<ArgumentHolder>(escapedArgs.size());
        for (Object toBeEscaped : escapedArgs) {
            argumentHolders.add(toArgumentHolder(toBeEscaped));
        }

        // Replace " = ? " for null values with " IS NULL "
        StringBuilder nullClauseBuilder = new StringBuilder(inlinedClause);
        Matcher argumentMatcher = Pattern.compile("\\?").matcher(inlinedClause);
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

        // Remove null args from holders
        Iterator<ArgumentHolder> argumentHolderIterator = argumentHolders.iterator();
        while (argumentHolderIterator.hasNext()) {
            if (argumentHolderIterator.next() instanceof NullArgHolder) {
                argumentHolderIterator.remove();
            }
        }

        RuntimeExceptionWhere<T, ID> where = dao.queryBuilder().where();
        where.raw(nullClauseBuilder.toString(), argumentHolders.toArray(new ArgumentHolder[0]));

        return where;
    }

    private static List<MatchResult> toMatchResults(Matcher matcher) {
        List<MatchResult> result = new ArrayList<MatchResult>();
        while (matcher.find()) {
            result.add(matcher.toMatchResult());
        }
        return result;
    }

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

    private static String inlineTableNameArgs(String clause, List<String> tableNameArgs) {
        Queue<String> tableNameQueue = new LinkedList<String>(tableNameArgs);// TODO add prefix tablename

        for (int indexOfPlaceHolder = clause.indexOf("??"); indexOfPlaceHolder > -1; indexOfPlaceHolder = clause.indexOf("??")) {
            String tableName = tableNameQueue.poll();
            clause = clause.replaceFirst("\\?\\?", tableName);
        }

        return clause;
    }

    @NotNull
    private static String unescapedValue(Object arg) {
        if (arg == null) {
            return "null";
        }

        if (arg instanceof String) {
            return '\'' + (String) arg + '\'';
        }

        if (arg instanceof Number) {
            return arg.toString();
        }

        StringBuilder sb = new StringBuilder();
        if (arg instanceof byte[]) {
            byte[] argCast = (byte[]) arg;
            for (int i = 0; i < argCast.length; i++) {
                sb.append(argCast[i]);
                if (i + 1 != argCast.length) {
                    sb.append(", ");
                }
            }
        } else if (arg instanceof short[]) {
            short[] argCast = (short[]) arg;
            for (int i = 0; i < argCast.length; i++) {
                sb.append(argCast[i]);
                if (i + 1 != argCast.length) {
                    sb.append(", ");
                }
            }
        } else if (arg instanceof int[]) {
            int[] argCast = (int[]) arg;
            for (int i = 0; i < argCast.length; i++) {
                sb.append(argCast[i]);
                if (i + 1 != argCast.length) {
                    sb.append(", ");
                }
            }
        } else if (arg instanceof long[]) {
            long[] argCast = (long[]) arg;
            for (int i = 0; i < argCast.length; i++) {
                sb.append(argCast[i]);
                if (i + 1 != argCast.length) {
                    sb.append(", ");
                }
            }
        } else if (arg instanceof float[]) {
            float[] argCast = (float[]) arg;
            for (int i = 0; i < argCast.length; i++) {
                sb.append(argCast[i]);
                if (i + 1 != argCast.length) {
                    sb.append(", ");
                }
            }
        } else if (arg instanceof double[]) {
            double[] argCast = (double[]) arg;
            for (int i = 0; i < argCast.length; i++) {
                sb.append(argCast[i]);
                if (i + 1 != argCast.length) {
                    sb.append(", ");
                }
            }
        } else if (arg instanceof Object[]) {
            Object[] argCast = (Object[]) arg;
            for (int i = 0; i < argCast.length; i++) {
                sb.append(unescapedValue(argCast[i]));
                if (i + 1 != argCast.length) {
                    sb.append(", ");
                }
            }
        } else if (arg instanceof Iterable) {
            Iterable<?> argCast = (Iterable<?>) arg;
            Iterator<?> iterator = argCast.iterator();
            while (iterator.hasNext()) {
                sb.append(unescapedValue(iterator.next()));
                if (iterator.hasNext()) {
                    sb.append(", ");
                }
            }
        }

        return sb.toString();
    }


}
