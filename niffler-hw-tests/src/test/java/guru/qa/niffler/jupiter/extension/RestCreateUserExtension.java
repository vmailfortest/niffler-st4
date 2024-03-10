package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.api.CategoryApiClient;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.api.UserApiClient;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.model.*;
import guru.qa.niffler.utils.DataUtils;

import java.io.IOException;
import java.util.Date;

public class RestCreateUserExtension extends CreateUserExtension {

    SpendApiClient spendApiClient = new SpendApiClient();
    CategoryApiClient categoryApiClient = new CategoryApiClient();
    AuthApiClient authApiClient = new AuthApiClient();
    UserApiClient userApiClient = new UserApiClient();

    @Override
    public UserJson createUser(TestUser user) {
        String username = user.username().isEmpty()
                ? DataUtils.generateRandomUsername()
                : user.username();
        String password = user.password().isEmpty()
                ? "12345"
                : user.password();

        try {
            authApiClient.doRegister(username, password);
        } catch (IOException e) {
            throw new RuntimeException("Unable to register user: " + user.username());
        }

        UserJson getCurrentUserResult;
        for (int i = 0; i < 10; i++) {
            try {
                getCurrentUserResult = userApiClient.getCurrentUser(username);
                System.out.println("QQQ: " + getCurrentUserResult);
                if (getCurrentUserResult != null) {
                    return new UserJson(
                            getCurrentUserResult.id(), getCurrentUserResult.username(), getCurrentUserResult.firstname(), getCurrentUserResult.surname(), getCurrentUserResult.currency(), getCurrentUserResult.photo(), null,
                            new TestData(password, null)
                    );
                }
            } catch (IOException e) {
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        throw new RuntimeException("Unable to find user in userApi after registration: " + user.username());
    }

    @Override
    public UserJson createCategory(TestUser user, UserJson createdUser) {

        for (GenerateCategory category : user.categories()) {

            CategoryJson categoryJson = new CategoryJson(null, category.category(), createdUser.username());

            try {
                categoryApiClient.addCategory(categoryJson);
            } catch (IOException e) {
                throw new RuntimeException("Error: Unable to create category " + category.category());
            }

        }

        return createdUser;
    }

    @Override
    public UserJson createSpend(TestUser user, UserJson createdUser) {

        for (GenerateSpend spend : user.spends()) {
            SpendJson spendJson = new SpendJson(
                    null, new Date(), spend.category(), spend.currency(), spend.amount(), spend.description(), createdUser.username()
            );

            try {
                spendApiClient.addSpend(spendJson);
            } catch (IOException e) {
                throw new RuntimeException("Error: Unable to create spend " + spend.description());
            }
        }

        return createdUser;
    }

}
