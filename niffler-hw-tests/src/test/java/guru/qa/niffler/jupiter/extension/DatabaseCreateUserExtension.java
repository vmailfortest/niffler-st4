package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.logging.JsonAttachment;
import guru.qa.niffler.db.model.*;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.db.repository.UserRepositoryJdbc;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.utils.DataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DatabaseCreateUserExtension extends CreateUserExtension {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(DatabaseCreateUserExtension.class);

    Faker faker = new Faker();

    static String userAuthKey = "userAuth";
    static String userdataKey = "userdata";

    private UserRepository userRepository = new UserRepositoryJdbc();

    @Override
    public UserJson createUser(TestUser user) {
        String username = user.username().isEmpty()
                ? DataUtils.generateRandomUsername()
                : user.username();
        String password = user.password().isEmpty()
                ? "12345"
                : user.password();

        UserAuthEntity userAuth = new UserAuthEntity();
        userAuth.setUsername(username);
        userAuth.setPassword(password);
        userAuth.setEnabled(true);
        userAuth.setAccountNonExpired(true);
        userAuth.setAccountNonLocked(true);
        userAuth.setCredentialsNonExpired(true);
        AuthorityEntity[] authorities = Arrays.stream(Authority.values()).map(
                a -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setAuthority(a);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);

        userAuth.addAuthorities(authorities);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setCurrency(CurrencyValues.RUB);

        userRepository.createInAuth(userAuth);
        userRepository.createInUserdata(userEntity);
        return new UserJson(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getFirstname(),
                userEntity.getSurname(),
                guru.qa.niffler.model.CurrencyValues.valueOf(userEntity.getCurrency().name()),
                userEntity.getPhoto() == null ? "" : new String(userEntity.getPhoto()),
                null,
                new TestData(
                        password,
                        null
                )
        );
    }

    @Override
    public UserJson createCategory(TestUser user, UserJson createdUser) {
        return null;
    }

    @Override
    public UserJson createSpend(TestUser user, UserJson createdUser) {
        return null;
    }

//    @Override
//    public void beforeEach(ExtensionContext extensionContext) throws Exception {
//
//        UserAuthEntity userAuth = new UserAuthEntity();
//        UserEntity user = new UserEntity();
//
//        Optional<ApiLogin> apiLoginAnnotation = AnnotationSupport.findAnnotation(
//                extensionContext.getRequiredTestMethod(),
//                ApiLogin.class
//        );
//
//        Optional<TestUser> dbUserAnnotation = AnnotationSupport.findAnnotation(
//                extensionContext.getRequiredTestMethod(),
//                TestUser.class
//        );
//
//        if (apiLoginAnnotation.isPresent() && !apiLoginAnnotation.get().user().handle()) {
//            return;
//        }
//
//        String username = "";
//        String password = "";
//
//        if (dbUserAnnotation.isPresent() && !dbUserAnnotation.get().username().isEmpty()) {
//            username = dbUserAnnotation.get().username();
//            password = dbUserAnnotation.get().password();
//        }
//
//        if (apiLoginAnnotation.isPresent() && !apiLoginAnnotation.get().user().username().isEmpty()) {
//            username = apiLoginAnnotation.get().user().username();
//            password = apiLoginAnnotation.get().user().password();
//        }
//
//        String randomUsername = faker.name().username();
//        if (username.isEmpty()) {
//            userAuth.setUsername(randomUsername);
//            userAuth.setPassword("12345");
//
//            user.setUsername(randomUsername);
//        } else {
//            userAuth.setUsername(username);
//            userAuth.setPassword(password);
//
//            user.setUsername(username);
//        }
//
//        userAuth.setEnabled(true);
//        userAuth.setAccountNonExpired(true);
//        userAuth.setAccountNonLocked(true);
//        userAuth.setCredentialsNonExpired(true);
//        userAuth.setAuthorities(Arrays.stream(Authority.values())
//                .map(e -> {
//                    AuthorityEntity ae = new AuthorityEntity();
//                    ae.setAuthority(e);
//                    return ae;
//                }).toList()
//        );
//
//        user.setCurrency(CurrencyValues.USD);
//        user.setFirstname(faker.name().firstName());
//        user.setSurname(faker.name().lastName());
//
//        userRepository.createInAuth(userAuth);
//        userRepository.createInUserdata(user);
//
//        Map<String, Object> userEntities = new HashMap<>();
//        userEntities.put(userAuthKey, userAuth);
//        userEntities.put(userdataKey, user);
//
//        extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), userEntities);
//
//        JsonAttachment.attachJson("JSON " + user.getUsername(), user.toFormattedJson());
//    }
//
//    @Override
//    public void afterEach(ExtensionContext extensionContext) throws Exception {
//
//        Map userEntities = extensionContext.getStore(NAMESPACE)
//                .get(extensionContext.getUniqueId(), Map.class);
//
//        if (userEntities == null) {
//            return;
//        }
//
//        UserAuthEntity userAuth = (UserAuthEntity) userEntities.get(userAuthKey);
//        UserEntity user = (UserEntity) userEntities.get(userdataKey);
//
//        userRepository.deleteInAuthById(userAuth.getId());
//        userRepository.deleteInUserdataById(user.getId());
//    }
//
//    @Override
//    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
//        return parameterContext.getParameter().getType().isAssignableFrom(UserAuthEntity.class);
//    }
//
//    @Override
//    public UserAuthEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
//        Map userEntities = extensionContext.getStore(NAMESPACE)
//                .get(extensionContext.getUniqueId(), Map.class);
//
//        return (UserAuthEntity) userEntities.get(userAuthKey);
//    }

}
