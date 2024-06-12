package fi.hsl.jore.importer.config.jooq.converter.date_range;

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

public class DateRangeBinding implements Binding<Object, DateRange> {

    @Override
    public Converter<Object, DateRange> converter() {
        return DateRangeConverter.INSTANCE;
    }

    @Override
    public void register(final BindingRegisterContext<DateRange> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
    }

    @Override
    public void sql(final BindingSQLContext<DateRange> ctx) {
        ctx.render().visit(DSL.val(ctx.convert(converter()).value())).sql("::daterange");
    }

    @Override
    public void get(final BindingGetResultSetContext<DateRange> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.resultSet().getString(ctx.index()));
    }

    // Getting a String value from a JDBC CallableStatement and converting that to a String
    @Override
    public void get(final BindingGetStatementContext<DateRange> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.statement().getString(ctx.index()));
    }

    @Override
    public void set(final BindingSetStatementContext<DateRange> ctx) throws SQLException {
        ctx.statement()
                .setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null));
    }

    @Override
    public void set(final BindingSetSQLOutputContext<DateRange> ctx)
            throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void get(final BindingGetSQLInputContext<DateRange> ctx)
            throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
}
