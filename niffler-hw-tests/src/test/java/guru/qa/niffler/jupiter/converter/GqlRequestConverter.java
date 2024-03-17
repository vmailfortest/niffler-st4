package guru.qa.niffler.jupiter.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.jupiter.annotation.GqlRequestFile;
import guru.qa.niffler.jupiter.extension.GqlRequestResolver;
import guru.qa.niffler.model.gql.GqlRequest;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.platform.commons.support.AnnotationSupport;

import java.io.IOException;
import java.io.InputStream;

public class GqlRequestConverter implements ArgumentConverter {

    private static final ObjectMapper om = new ObjectMapper();
    private final ClassLoader cl = GqlRequestResolver.class.getClassLoader();

    @Override
    public Object convert(Object o, ParameterContext parameterContext) throws ArgumentConversionException {
        if (o instanceof String path
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), GqlRequestFile.class)
                && parameterContext.getParameter().getType().isAssignableFrom(GqlRequest.class)) {
            try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
                return om.readValue(is.readAllBytes(), GqlRequest.class);
            } catch (IOException e) {
                throw new ParameterResolutionException(e.getMessage());
            }
        }
        throw new RuntimeException("Cannot convert parametrized test value to GqlRequest.");
    }

}
