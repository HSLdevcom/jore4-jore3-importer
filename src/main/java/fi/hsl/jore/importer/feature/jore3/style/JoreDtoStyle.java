package fi.hsl.jore.importer.feature.jore3.style;

import fi.hsl.jore.importer.feature.jore3.mapping.JoreColumn;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreForeignKey;
import fi.hsl.jore.importer.feature.jore3.mapping.JoreTable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.immutables.value.Value;

@Target({ElementType.PACKAGE, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
@Value.Style(
        // Don't require our abstract classes to start with the "Abstract"-prefix
        typeAbstract = "*",
        // Minimize generated code by:
        // - we don't need from methods and we construct the DTOs in one go
        strictBuilder = true,
        passAnnotations = {JoreTable.class, JoreColumn.class, JoreForeignKey.class})
public @interface JoreDtoStyle {}
