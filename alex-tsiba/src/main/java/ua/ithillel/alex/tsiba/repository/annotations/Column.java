package ua.ithillel.alex.tsiba.repository.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface Column {
    enum ColumnProperty {AUTO_INCREMENT, STRING, INTEGER}

    String name();

    boolean isId() default false;

    ColumnProperty property() default ColumnProperty.STRING;
}
