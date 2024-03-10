package guru.qa.niffler.jupiter.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface User {

    Point value() default Point.INNER;

    enum Point {
        INNER, OUTER
    }

}
