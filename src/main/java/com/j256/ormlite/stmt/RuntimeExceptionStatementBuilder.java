package com.j256.ormlite.stmt;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.stmt.mapped.MappedPreparedStmt;

import java.sql.SQLException;
import java.util.List;

public abstract class RuntimeExceptionStatementBuilder<T, ID> {

    private final StatementBuilder<T, ID> internalBuilder;

    public RuntimeExceptionStatementBuilder(StatementBuilder<T, ID> internalBuilder) {
        this.internalBuilder = internalBuilder;
    }

    /**
     * @see StatementBuilder#where()
     */
    public Where<T, ID> where() {
        return internalBuilder.where();
    }

    /**
     * @see StatementBuilder#setWhere(Where)
     */
    public void setWhere(Where<T, ID> where) {
        internalBuilder.setWhere(where);
    }

    /**
     * @see StatementBuilder#prepareStatement(Long)
     */
    protected MappedPreparedStmt<T, ID> prepareStatement(Long limit) {
        try {
            return internalBuilder.prepareStatement(limit);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see StatementBuilder#prepareStatementString()
     */
    public String prepareStatementString() {
        try {
            return internalBuilder.prepareStatementString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see StatementBuilder#prepareStatementInfo()
     */
    public StatementBuilder.StatementInfo prepareStatementInfo() {
        try {
            return internalBuilder.prepareStatementInfo();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see StatementBuilder#clear()
     */
    @Deprecated
    public void clear() {
        internalBuilder.clear();
    }

    /**
     * @see StatementBuilder#reset()
     */
    public void reset() {
        internalBuilder.reset();
    }

    /**
     * @see StatementBuilder#buildStatementString(List)
     */
    protected String buildStatementString(List<ArgumentHolder> argList) {
        try {
            return internalBuilder.buildStatementString(argList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see StatementBuilder#appendStatementString(StringBuilder, List)
     */
    protected void appendStatementString(StringBuilder sb, List<ArgumentHolder> argList) {
        try {
            internalBuilder.appendStatementString(sb, argList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see StatementBuilder#appendStatementStart(StringBuilder, List)
     */
    protected abstract void appendStatementStart(StringBuilder sb, List<ArgumentHolder> argList);

    /**
     * @see StatementBuilder#appendWhereStatement(StringBuilder, List, StatementBuilder.WhereOperation)
     */
    protected boolean appendWhereStatement(StringBuilder sb, List<ArgumentHolder> argList, StatementBuilder.WhereOperation operation) {
        try {
            return internalBuilder.appendWhereStatement(sb, argList, operation);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see StatementBuilder#appendStatementEnd(StringBuilder, List)
     */
    protected abstract void appendStatementEnd(StringBuilder sb, List<ArgumentHolder> argList);

    /**
     * @see StatementBuilder#shouldPrependTableNameToColumns()
     */
    protected boolean shouldPrependTableNameToColumns() {
        return internalBuilder.shouldPrependTableNameToColumns();
    }

    /**
     * @see StatementBuilder#getResultFieldTypes()
     */
    protected FieldType[] getResultFieldTypes() {
        return internalBuilder.getResultFieldTypes();
    }

    /**
     * @see StatementBuilder#verifyColumnName(String)
     */
    protected FieldType verifyColumnName(String columnName) {
        return internalBuilder.verifyColumnName(columnName);
    }

    /**
     * @see StatementBuilder#getType()
     */
    StatementBuilder.StatementType getType() {
        return internalBuilder.getType();
    }

}
