package fi.hsl.jore.importer.config.migration;


import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class BlockMigrationsCondition implements Condition {

    @Override
    public boolean matches(final ConditionContext context,
                           final AnnotatedTypeMetadata metadata) {
        //noinspection BooleanVariableAlwaysNegated
        final boolean shouldMigrate = context.getEnvironment()
                                             .getProperty("jore.importer.migrate",
                                                          Boolean.class,
                                                          false);
        return !shouldMigrate;
    }
}
