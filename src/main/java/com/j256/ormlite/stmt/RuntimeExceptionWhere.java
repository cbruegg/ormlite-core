package com.j256.ormlite.stmt;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;

import java.sql.SQLException;
import java.util.List;

public class RuntimeExceptionWhere<T, ID> {

    private final Where<T, ID> internalWhere;

    public RuntimeExceptionWhere(Where<T, ID> internalWhere) {
        this.internalWhere = internalWhere;
    }

    /**
     * @see Where#and()
     */
    public RuntimeExceptionWhere<T, ID> and() {
        internalWhere.and();
        return this;
    }

    /**
     * @see Where#and(Where, Where, Where[])
     */
    public RuntimeExceptionWhere<T, ID> and(Where<T, ID> first, Where<T, ID> second, Where<T, ID>... others) {
        internalWhere.and(first, second, others);
        return this;
    }

    /**
     * @see Where#and(int)
     */
    public RuntimeExceptionWhere<T, ID> and(int numClauses) {
        internalWhere.and(numClauses);
        return this;
    }

    /**
     * @see Where#between(String, Object, Object)
     */
    public RuntimeExceptionWhere<T, ID> between(String columnName, Object low, Object high) {
        try {
            internalWhere.between(columnName, low, high);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see Where#eq(String, Object)
     */
    public RuntimeExceptionWhere<T, ID> eq(String columnName, Object value) {
        try {
            internalWhere.eq(columnName, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see Where#ge(String, Object)
     */
    public RuntimeExceptionWhere<T, ID> ge(String columnName, Object value) {
        try {
            internalWhere.ge(columnName, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see Where#gt(String, Object)
     */
    public RuntimeExceptionWhere<T, ID> gt(String columnName, Object value) {
        try {
            internalWhere.gt(columnName, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see Where#in(String, Iterable)
     */
    public RuntimeExceptionWhere<T, ID> in(String columnName, Iterable<?> objects) {
        try {
            internalWhere.in(columnName, objects);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see Where#notIn(String, Iterable)
     */
    public RuntimeExceptionWhere<T, ID> notIn(String columnName, Iterable<?> objects) {
        try {
            internalWhere.notIn(columnName, objects);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see Where#(String, Object...)
     */
    public RuntimeExceptionWhere<T, ID> in(String columnName, Object... objects) {
        try {
            internalWhere.in(columnName, objects);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see Where#notIn(String, Object...)
     */
    public RuntimeExceptionWhere<T, ID> notIn(String columnName, Object... objects) {
        try {
            internalWhere.notIn(columnName, objects);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see Where#in(String, QueryBuilder)
     */
    public RuntimeExceptionWhere<T, ID> in(String columnName, QueryBuilder<?, ?> subQueryBuilder) {
        try {
            internalWhere.in(columnName, subQueryBuilder);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see Where#notIn(String, QueryBuilder)
     */
    public RuntimeExceptionWhere<T, ID> notIn(String columnName, QueryBuilder<?, ?> subQueryBuilder) {
        try {
            internalWhere.notIn(columnName, subQueryBuilder);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see Where#exists(QueryBuilder)
     */
    public RuntimeExceptionWhere<T, ID> exists(QueryBuilder<?, ?> subQueryBuilder) {
        internalWhere.exists(subQueryBuilder);
        return this;
    }

    /**
     * @see Where#isNull(String)
     */
    public RuntimeExceptionWhere<T, ID> isNull(String columnName) {
        try {
            internalWhere.isNull(columnName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see Where#isNotNull(String)
     */
    public RuntimeExceptionWhere<T, ID> isNotNull(String columnName) {
        try {
            internalWhere.isNotNull(columnName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see Where#le(String, Object)
     */
    public RuntimeExceptionWhere<T, ID> le(String columnName, Object value) {
        try {
            internalWhere.le(columnName, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see Where#lt(String, Object)
     */
    public RuntimeExceptionWhere<T, ID> lt(String columnName, Object value) {
        try {
            internalWhere.lt(columnName, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see Where#like(String, Object)
     */
    public RuntimeExceptionWhere<T, ID> like(String columnName, Object value) {
        try {
            internalWhere.like(columnName, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see Where#ne(String, Object)
     */
    public RuntimeExceptionWhere<T, ID> ne(String columnName, Object value) {
        try {
            internalWhere.ne(columnName, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see Where#not()
     */
    public RuntimeExceptionWhere<T, ID> not() {
        internalWhere.not();
        return this;
    }

    /**
     * @see Where#not(Where)
     */
    public RuntimeExceptionWhere<T, ID> not(Where<T, ID> comparison) {
        internalWhere.not(comparison);
        return this;
    }

    /**
     * @see Where#or()
     */
    public RuntimeExceptionWhere<T, ID> or() {
        internalWhere.or();
        return this;
    }

    /**
     * @see Where#or(Where, Where, Where[])
     */
    public RuntimeExceptionWhere<T, ID> or(Where<T, ID> left, Where<T, ID> right, Where<T, ID>... others) {
        internalWhere.or(left, right, others);
        return this;
    }

    /**
     * @see Where#or(int)
     */
    public RuntimeExceptionWhere<T, ID> or(int numClauses) {
        internalWhere.or(numClauses);
        return this;
    }

    /**
     * @see Where#idEq(Object)
     */
    public RuntimeExceptionWhere<T, ID> idEq(ID id) {
        try {
            internalWhere.idEq(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see Where#idEq(Dao, Object)
     */
    public <OD> RuntimeExceptionWhere<T, ID> idEq(Dao<OD, ?> dataDao, OD data) {
        try {
            internalWhere.idEq(dataDao, data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see Where#raw(String, ArgumentHolder...)
     */
    public RuntimeExceptionWhere<T, ID> raw(String rawStatement, ArgumentHolder... args) {
        internalWhere.raw(rawStatement, args);
        return this;
    }

    /**
     * @see Where#rawComparison(String, String, Object)
     */
    public RuntimeExceptionWhere<T, ID> rawComparison(String columnName, String rawOperator, Object value) {
        try {
            internalWhere.rawComparison(columnName, rawOperator, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * @see Where#prepare()
     */
    public PreparedQuery<T> prepare() {
        try {
            return internalWhere.prepare();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see Where#query()
     */
    public List<T> query() {
        try {
            return internalWhere.query();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see Where#queryRaw()
     */
    public GenericRawResults<String[]> queryRaw() {
        try {
            return internalWhere.queryRaw();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see Where#queryForFirst()
     */
    public T queryForFirst() {
        try {
            return internalWhere.queryForFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see Where#queryRawFirst()
     */
    public String[] queryRawFirst() {
        try {
            return internalWhere.queryRawFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see Where#countOf()
     */
    public long countOf() {
        try {
            return internalWhere.countOf();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see Where#iterator()
     */
    public CloseableIterator<T> iterator() {
        try {
            return internalWhere.iterator();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see Where#clear()
     */
    @Deprecated
    public RuntimeExceptionWhere<T, ID> clear() {
        internalWhere.clear();
        return this;
    }

    /**
     * @see Where#reset()
     */
    public RuntimeExceptionWhere<T, ID> reset() {
        internalWhere.reset();
        return this;
    }

    /**
     * @see Where#getStatement()
     */
    public String getStatement() {
        try {
            return internalWhere.getStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see Where#appendSql(String, StringBuilder, List)
     */
    void appendSql(String tableName, StringBuilder sb, List<ArgumentHolder> columnArgList) {
        try {
            internalWhere.appendSql(tableName, sb, columnArgList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return internalWhere.toString();
    }
}
