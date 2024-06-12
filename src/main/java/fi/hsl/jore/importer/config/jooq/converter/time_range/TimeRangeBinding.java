package fi.hsl.jore.importer.config.jooq.converter.time_range;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;
import java.util.Objects;
import org.jooq.Binding;
import org.jooq.BindingGetResultSetContext;
import org.jooq.BindingGetSQLInputContext;
import org.jooq.BindingGetStatementContext;
import org.jooq.BindingRegisterContext;
import org.jooq.BindingSQLContext;
import org.jooq.BindingSetSQLOutputContext;
import org.jooq.BindingSetStatementContext;
import org.jooq.Converter;
import org.jooq.impl.DSL;

public class TimeRangeBinding implements Binding<Object, TimeRange> {

    @Override
    public Converter<Object, TimeRange> converter() {
        return TimeRangeConverter.INSTANCE;
    }

    @Override
    public void register(final BindingRegisterContext<TimeRange> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
    }

    @Override
    public void sql(final BindingSQLContext<TimeRange> ctx) {
        ctx.render().visit(DSL.val(ctx.convert(converter()).value())).sql("::tstzrange");
    }

    @Override
    public void get(final BindingGetResultSetContext<TimeRange> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.resultSet().getString(ctx.index()));
    }

    // Getting a String value from a JDBC CallableStatement and converting that to a String
    @Override
    public void get(final BindingGetStatementContext<TimeRange> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.statement().getString(ctx.index()));
    }

    @Override
    public void set(final BindingSetStatementContext<TimeRange> ctx) throws SQLException {
        ctx.statement()
                .setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null));
    }

    @Override
    public void set(final BindingSetSQLOutputContext<TimeRange> ctx)
            throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void get(final BindingGetSQLInputContext<TimeRange> ctx)
            throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
}
