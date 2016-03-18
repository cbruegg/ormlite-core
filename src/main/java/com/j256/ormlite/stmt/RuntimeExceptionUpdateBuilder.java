package com.j256.ormlite.stmt;

import java.sql.SQLException;
import java.util.List;

public class RuntimeExceptionUpdateBuilder<T, ID> extends RuntimeExceptionStatementBuilder<T, ID> {

    private final UpdateBuilder<T, ID> internalBuilder;

    public RuntimeExceptionUpdateBuilder(UpdateBuilder<T, ID> internalBuilder) {
        super(internalBuilder);
        this.internalBuilder = internalBuilder;
    }

    /**
     * @see UpdateBuilder#prepare()
     */
    public PreparedUpdate<T> prepare()  {
        try {
            return internalBuilder.prepare();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see UpdateBuilder#updateColumnValue(String, Object)
     */
    public RuntimeExceptionUpdateBuilder<T, ID> updateColumnValue(String columnName, Object value)  {
        try {
            internalBuilder.updateColumnValue(columnName, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see UpdateBuilder#updateColumnExpression(String, String)
     */
    public RuntimeExceptionUpdateBuilder<T, ID> updateColumnExpression(String columnName, String expression)  {
        try {
            internalBuilder.updateColumnExpression(columnName, expression);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see UpdateBuilder#escapeColumnName(StringBuilder, String)
     */
    public void escapeColumnName(StringBuilder sb, String columnName) {
        internalBuilder.escapeColumnName(sb, columnName);
    }

    /**
     * @see UpdateBuilder#escapeColumnName(String)
     */
    public String escapeColumnName(String columnName) {
        return internalBuilder.escapeColumnName(columnName);
    }

    /**
     * @see UpdateBuilder#escapeValue(StringBuilder, String)
     */
    public void escapeValue(StringBuilder sb, String value) {
        internalBuilder.escapeValue(sb, value);
    }

    /**
     * @see UpdateBuilder#escapeValue(String)
     */
    public String escapeValue(String value) {
        return internalBuilder.escapeValue(value);
    }

    /**
     * @see UpdateBuilder#update()
     */
    public int update()  {
        try {
            return internalBuilder.update();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @deprecated Renamed to be {@link #reset()}.
     */
    @Deprecated
    @Override
    public void clear() {
        internalBuilder.clear();
    }

    @Override
    public void reset() {
        super.reset();
        internalBuilder.reset();
    }

    @Override
    protected void appendStatementStart(StringBuilder sb, List<ArgumentHolder> argList)  {
        try {
            internalBuilder.appendStatementStart(sb, argList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void appendStatementEnd(StringBuilder sb, List<ArgumentHolder> argList) {
        internalBuilder.appendStatementEnd(sb, argList);
    }
}
