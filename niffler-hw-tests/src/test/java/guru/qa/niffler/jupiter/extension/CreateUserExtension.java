package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.annotation.TestUsers;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.*;

public abstract class CreateUserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(CreateUserExtension.class);

    public abstract UserJson createUser(TestUser user);

    public abstract UserJson createCategory(TestUser user, UserJson createdUser);

    public abstract UserJson createSpend(TestUser user, UserJson createdUser);

    public abstract UserJson createFriend(TestUser user, UserJson createdUser);

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {

        Map<User.Point, List<TestUser>> usersForTest = extractUsersForTest(extensionContext);

        Map<User.Point, List<UserJson>> createdUsers = new HashMap<>();
        for (Map.Entry<User.Point, List<TestUser>> userInfo : usersForTest.entrySet()) {
            List<UserJson> usersForPoint = new ArrayList<>();
            for (TestUser testUser : userInfo.getValue()) {
                UserJson createdUser = createUser(testUser);
                usersForPoint.add(createdUser);

                createCategory(testUser, createdUser);
                createSpend(testUser, createdUser);
                createFriend(testUser, createdUser);
            }
            createdUsers.put(userInfo.getKey(), usersForPoint);
        }

        extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), createdUsers);

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return AnnotationSupport.findAnnotation(parameterContext.getParameter(), User.class).isPresent() &&
                (parameterContext.getParameter().getType().isAssignableFrom(UserJson.class) ||
                        parameterContext.getParameter().getType().isAssignableFrom(UserJson[].class));
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        User user = AnnotationSupport.findAnnotation(parameterContext.getParameter(), User.class).get();
        Map<User.Point, List<UserJson>> createdUsers = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
        List<UserJson> userJsons = createdUsers.get(user.value());
        if (parameterContext.getParameter().getType().isAssignableFrom(UserJson[].class)) {
            return userJsons.stream().toList().toArray(new UserJson[0]);
        } else {
            return userJsons.get(0);
        }
    }

    private Map<User.Point, List<TestUser>> extractUsersForTest(ExtensionContext context) {

        Map<User.Point, List<TestUser>> result = new HashMap<>();
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), ApiLogin.class).ifPresent(
                apiLogin -> {
                    TestUser user = apiLogin.user();
                    if (user.handle()) {
                        result.put(User.Point.INNER, List.of(user));
                    }
                }
        );

        List<TestUser> outerUsers = new ArrayList<>();
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), TestUser.class).ifPresent(
                tu -> {
                    if (tu.handle()) outerUsers.add(tu);
                }
        );
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), TestUsers.class).ifPresent(
                testUsers -> Arrays.stream(testUsers.value())
                        .filter(tu -> tu.handle())
                        .forEach(outerUsers::add)
        );

        result.put(User.Point.OUTER, outerUsers);

        return result;
    }
}
