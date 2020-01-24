package ua.ithillel.alex.tsiba.repository.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface Column {
    enum ColumnProperty {AUTO_INCREMENT, STRING, INTEGER}

    String name();

    boolean isId() default false;

    ColumnProperty property() default ColumnProperty.STRING;
}
