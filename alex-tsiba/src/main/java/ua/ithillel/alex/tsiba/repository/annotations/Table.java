package ua.ithillel.alex.tsiba.repository.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Table {
    String table();
}
