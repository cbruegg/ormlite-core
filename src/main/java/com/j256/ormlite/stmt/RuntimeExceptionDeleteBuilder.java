package com.j256.ormlite.stmt;

import java.sql.SQLException;
import java.util.List;

public class RuntimeExceptionDeleteBuilder<T, ID> extends RuntimeExceptionStatementBuilder<T, ID> {

    private final DeleteBuilder<T, ID> internalBuilder;

    public RuntimeExceptionDeleteBuilder(DeleteBuilder<T, ID> internalBuilder) {
        super(internalBuilder);
        this.internalBuilder = internalBuilder;
    }

    /**
     * @see DeleteBuilder#prepare()
     */
    public PreparedDelete<T> prepare() {
        try {
            return internalBuilder.prepare();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see DeleteBuilder#delete()
     */
    public int delete() {
        try {
            return internalBuilder.delete();
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
        // NOTE: this is here because it is in the other sub-classes
        super.reset();
        internalBuilder.reset();
    }

    @Override
    protected void appendStatementStart(StringBuilder sb, List<ArgumentHolder> argList) {
        internalBuilder.appendStatementEnd(sb, argList);
    }

    @Override
    protected void appendStatementEnd(StringBuilder sb, List<ArgumentHolder> argList) {
        internalBuilder.appendStatementEnd(sb, argList);
    }
}
