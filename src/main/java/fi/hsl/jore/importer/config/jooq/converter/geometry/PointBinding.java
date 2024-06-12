package fi.hsl.jore.importer.config.jooq.converter.geometry;

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
import org.locationtech.jts.geom.Point;

public class PointBinding implements Binding<Object, Point> {

    @Override
    public Converter<Object, Point> converter() {
        return PointConverter.INSTANCE;
    }

    @Override
    public void sql(final BindingSQLContext<Point> ctx) throws SQLException {
        ctx.render().visit(DSL.sql("?::geometry"));
    }

    @Override
    public void register(final BindingRegisterContext<Point> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
    }

    @Override
    public void set(final BindingSetStatementContext<Point> ctx) throws SQLException {
        ctx.statement()
                .setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null));
    }

    @Override
    public void get(final BindingGetResultSetContext<Point> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.resultSet().getString(ctx.index()));
    }

    @Override
    public void get(final BindingGetStatementContext<Point> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.statement().getString(ctx.index()));
    }

    @Override
    public void set(final BindingSetSQLOutputContext<Point> ctx) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void get(final BindingGetSQLInputContext<Point> ctx) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
}
