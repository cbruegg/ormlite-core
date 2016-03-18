package com.j256.ormlite.stmt;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.field.FieldType;

import java.sql.SQLException;
import java.util.List;

public class RuntimeExceptionQueryBuilder<T, ID> extends RuntimeExceptionStatementBuilder<T, ID> {

    private final QueryBuilder<T, ID> internalQueryBuilder;

    public RuntimeExceptionQueryBuilder(QueryBuilder<T, ID> internalQueryBuilder) {
        super(internalQueryBuilder);
        this.internalQueryBuilder = internalQueryBuilder;
    }

    /**
     * @see QueryBuilder#enableInnerQuery()
     */
    void enableInnerQuery() {
        internalQueryBuilder.enableInnerQuery();
    }

    /**
     * @see QueryBuilder#getSelectColumnCount()
     */
    int getSelectColumnCount() {
        return internalQueryBuilder.getSelectColumnCount();
    }

    /**
     * @see QueryBuilder#getSelectColumnsAsString()
     */
    String getSelectColumnsAsString() {
        return internalQueryBuilder.getSelectColumnsAsString();
    }

    /**
     * @see QueryBuilder#prepare()
     */
    public PreparedQuery<T> prepare() {
        try {
            return internalQueryBuilder.prepare();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see QueryBuilder#selectColumns(String...)
     */
    public RuntimeExceptionQueryBuilder<T, ID> selectColumns(String... columns) {
        internalQueryBuilder.selectColumns(columns);
        return this;
    }

    /**
     * @see QueryBuilder#selectColumns(Iterable)
     */
    public RuntimeExceptionQueryBuilder<T, ID> selectColumns(Iterable<String> columns) {
        internalQueryBuilder.selectColumns(columns);
        return this;
    }

    /**
     * @see QueryBuilder#selectRaw(String...)
     */
    public RuntimeExceptionQueryBuilder<T, ID> selectRaw(String... columns) {
        internalQueryBuilder.selectRaw(columns);
        return this;
    }

    /**
     * @see QueryBuilder#groupBy(String)
     */
    public RuntimeExceptionQueryBuilder<T, ID> groupBy(String columnName) {
        internalQueryBuilder.groupBy(columnName);
        return this;
    }

    /**
     * @see QueryBuilder#groupByRaw(String)
     */
    public RuntimeExceptionQueryBuilder<T, ID> groupByRaw(String rawSql) {
        internalQueryBuilder.groupByRaw(rawSql);
        return this;
    }

    /**
     * @see QueryBuilder#orderBy(String, boolean)
     */
    public RuntimeExceptionQueryBuilder<T, ID> orderBy(String columnName, boolean ascending) {
        internalQueryBuilder.orderBy(columnName, ascending);
        return this;
    }

    /**
     * @see QueryBuilder#orderByRaw(String)
     */
    public RuntimeExceptionQueryBuilder<T, ID> orderByRaw(String rawSql) {
        internalQueryBuilder.orderByRaw(rawSql);
        return this;
    }

    /**
     * @see QueryBuilder#orderByRaw(String, ArgumentHolder...)
     */
    public RuntimeExceptionQueryBuilder<T, ID> orderByRaw(String rawSql, ArgumentHolder... args) {
        internalQueryBuilder.orderByRaw(rawSql, args);
        return this;
    }

    /**
     * @see QueryBuilder#distinct()
     */
    public RuntimeExceptionQueryBuilder<T, ID> distinct() {
        internalQueryBuilder.distinct();
        return this;
    }

    /**
     * @see QueryBuilder#limit(int)
     */
    @Deprecated
    public RuntimeExceptionQueryBuilder<T, ID> limit(int maxRows) {
        internalQueryBuilder.limit(maxRows);
        return this;
    }

    /**
     * @see QueryBuilder#limit(Long)
     */
    public RuntimeExceptionQueryBuilder<T, ID> limit(Long maxRows) {
        internalQueryBuilder.limit(maxRows);
        return this;
    }

    /**
     * @see QueryBuilder#offset(int)
     */
    @Deprecated
    public RuntimeExceptionQueryBuilder<T, ID> offset(int startRow) {
        try {
            internalQueryBuilder.offset(startRow);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see QueryBuilder#offset(Long)
     */
    public RuntimeExceptionQueryBuilder<T, ID> offset(Long startRow) {
        try {
            internalQueryBuilder.offset(startRow);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see QueryBuilder#setCountOf(boolean)
     */
    public RuntimeExceptionQueryBuilder<T, ID> setCountOf(boolean countOf) {
        internalQueryBuilder.setCountOf(countOf);
        return this;
    }

    /**
     * @see QueryBuilder#setCountOf(String)
     */
    public RuntimeExceptionQueryBuilder<T, ID> setCountOf(String countOfQuery) {
        internalQueryBuilder.setCountOf(countOfQuery);
        return this;
    }

    /**
     * @see QueryBuilder#having(String)
     */
    public RuntimeExceptionQueryBuilder<T, ID> having(String having) {
        internalQueryBuilder.having(having);
        return this;
    }

    /**
     * @see QueryBuilder#join(QueryBuilder)
     */
    public RuntimeExceptionQueryBuilder<T, ID> join(QueryBuilder<?, ?> joinedQueryBuilder) {
        try {
            internalQueryBuilder.join(joinedQueryBuilder);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see QueryBuilder#join(QueryBuilder, QueryBuilder.JoinType, QueryBuilder.JoinWhereOperation)
     */
    public RuntimeExceptionQueryBuilder<T, ID> join(QueryBuilder<?, ?> joinedQueryBuilder, QueryBuilder.JoinType type, QueryBuilder.JoinWhereOperation operation) {
        try {
            internalQueryBuilder.join(joinedQueryBuilder, type, operation);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see QueryBuilder#joinOr(QueryBuilder)
     */
    public RuntimeExceptionQueryBuilder<T, ID> joinOr(QueryBuilder<?, ?> joinedQueryBuilder) {
        try {
            internalQueryBuilder.joinOr(joinedQueryBuilder);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see QueryBuilder#leftJoin(QueryBuilder)
     */
    public RuntimeExceptionQueryBuilder<T, ID> leftJoin(QueryBuilder<?, ?> joinedQueryBuilder) {
        try {
            internalQueryBuilder.leftJoin(joinedQueryBuilder);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see QueryBuilder#leftJoinOr(QueryBuilder)
     */
    public RuntimeExceptionQueryBuilder<T, ID> leftJoinOr(QueryBuilder<?, ?> joinedQueryBuilder) {
        try {
            internalQueryBuilder.leftJoinOr(joinedQueryBuilder);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see QueryBuilder#join(String, String, QueryBuilder)
     */
    public RuntimeExceptionQueryBuilder<T, ID> join(String localColumnName, String joinedColumnName,
                                                    QueryBuilder<?, ?> joinedQueryBuilder) {
        try {
            internalQueryBuilder.join(localColumnName, joinedColumnName, joinedQueryBuilder);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see QueryBuilder#join(String, String, QueryBuilder, QueryBuilder.JoinType, QueryBuilder.JoinWhereOperation)
     */
    public RuntimeExceptionQueryBuilder<T, ID> join(String localColumnName, String joinedColumnName,
                                                    QueryBuilder<?, ?> joinedQueryBuilder, QueryBuilder.JoinType type, QueryBuilder.JoinWhereOperation operation) {
        try {
            internalQueryBuilder.join(localColumnName, joinedColumnName, joinedQueryBuilder);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see QueryBuilder#query()
     */
    public List<T> query() {
        try {
            return internalQueryBuilder.query();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see QueryBuilder#queryRaw()
     */
    public GenericRawResults<String[]> queryRaw() {
        try {
            return internalQueryBuilder.queryRaw();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see QueryBuilder#queryForFirst()
     */
    public T queryForFirst() {
        try {
            return internalQueryBuilder.queryForFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see QueryBuilder#queryRawFirst()
     */
    public String[] queryRawFirst() {
        try {
            return internalQueryBuilder.queryRawFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see QueryBuilder#iterator()
     */
    public CloseableIterator<T> iterator() {
        try {
            return internalQueryBuilder.iterator();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see QueryBuilder#countOf()
     */
    public long countOf() {
        try {
            return internalQueryBuilder.countOf();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see QueryBuilder#countOf(String)
     */
    public long countOf(String countOfQuery) {
        try {
            return internalQueryBuilder.countOf(countOfQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see QueryBuilder#clear()
     */
    @Deprecated
    @Override
    public void clear() {
        internalQueryBuilder.clear();
    }

    @Override
    public void reset() {
        super.reset();
        internalQueryBuilder.reset();
    }

    @Override
    protected void appendStatementStart(StringBuilder sb, List<ArgumentHolder> argList) {
        internalQueryBuilder.appendStatementStart(sb, argList);
    }

    @Override
    protected FieldType[] getResultFieldTypes() {
        return internalQueryBuilder.getResultFieldTypes();
    }

    @Override
    protected boolean appendWhereStatement(StringBuilder sb, List<ArgumentHolder> argList, StatementBuilder.WhereOperation operation) {
        try {
            return internalQueryBuilder.appendWhereStatement(sb, argList, operation);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void appendStatementEnd(StringBuilder sb, List<ArgumentHolder> argList) {
        try {
            internalQueryBuilder.appendStatementEnd(sb, argList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean shouldPrependTableNameToColumns() {
        return internalQueryBuilder.shouldPrependTableNameToColumns();
    }

}
