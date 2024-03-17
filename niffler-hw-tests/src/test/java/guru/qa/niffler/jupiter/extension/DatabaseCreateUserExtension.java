package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.model.*;
import guru.qa.niffler.db.repository.SpendingRepository;
import guru.qa.niffler.db.repository.SpendingRepositorySJdbc;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.db.repository.UserRepositorySJdbc;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.utils.DataUtils;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class DatabaseCreateUserExtension extends CreateUserExtension {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(DatabaseCreateUserExtension.class);

    static String userAuthKey = "userAuth";
    static String userdataKey = "userdata";

    private UserRepository userRepository = new UserRepositorySJdbc();
    private SpendingRepository spendRepository = new SpendingRepositorySJdbc();

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

        for (GenerateCategory category : user.categories()) {
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setCategory(category.category());
            categoryEntity.setUsername(createdUser.username());

            spendRepository.createCategory(categoryEntity);
        }

        return createdUser;
    }

    @Override
    public UserJson createSpend(TestUser user, UserJson createdUser) {
        for (GenerateSpend spend : user.spends()) {

            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setCategory(spend.category());
            categoryEntity.setUsername(spend.username());
            String categoryId = spendRepository.getCategoryId(spend.category(), spend.username());
            categoryEntity.setId(UUID.fromString(categoryId));

            SpendEntity spendEntity = new SpendEntity();
            spendEntity.setUsername(createdUser.username());
            spendEntity.setCategory(categoryEntity);
            spendEntity.setCurrency(spend.currency());
            spendEntity.setSpendDate(new Date());
            spendEntity.setAmount(spend.amount());
            spendEntity.setDescription(spend.description());
            spendEntity.setCategory(categoryEntity);

            spendRepository.createSpending(spendEntity);
        }

        return createdUser;
    }

    @Override
    public UserJson createFriend(TestUser user, UserJson createdUser) {
        throw new RuntimeException("DB createFriend is not implemented.");
    }

}
