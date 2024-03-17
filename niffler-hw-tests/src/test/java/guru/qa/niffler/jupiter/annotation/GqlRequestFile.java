package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.converter.GqlRequestConverter;
import guru.qa.niffler.jupiter.extension.GqlRequestResolver;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.converter.ConvertWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ExtendWith(GqlRequestResolver.class)
@ConvertWith(GqlRequestConverter.class)
public @interface GqlRequestFile {
    String value() default "";
}
