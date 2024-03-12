package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.jupiter.extension.ContextHolderExtension;
import guru.qa.niffler.jupiter.extension.DatabaseCreateUserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({ContextHolderExtension.class, DatabaseCreateUserExtension.class, ApiLoginExtension.class})
public class hw14Test extends BaseWebTest {

    @ApiLogin(username = "duck", password = "12345")
    @Test
    void loginWithApi() {

        Selenide.open(MainPage.URL);
        mainPage.spendingsTableShouldHaveRows();

    }

    @ApiLogin(user = @TestUser)
    @Test
    void loginDbUserWithApi() {

        Selenide.open(MainPage.URL);
        mainPage.spendingsTableShouldHaveSize(0);

    }

    @ApiLogin(user = @TestUser(username = "hw14tester", password = "12345"))
    @Test
    void loginDbUserWithCredsWithApi() {

        Selenide.open(MainPage.URL);
        mainPage.spendingsTableShouldHaveSize(0);

    }

    @TestUser(username = "testUser", password = "12345")
    @Test
    void dbUserWithCreds(UserAuthEntity userAuth) {
        Selenide.open(CFG.frontUrl());
        welcomePage.clickLoginLink();
        loginPage.login(userAuth.getUsername(), userAuth.getPassword());

        mainPage.spendingsTableShouldHaveSize(0);

    }

    @TestUser
    @Test
    void dbUserWithGeneratedCreds(@User(User.Point.OUTER) UserJson user) {
        Selenide.open(CFG.frontUrl());
        welcomePage.clickLoginLink();
        loginPage.login(user.username(), user.testData().password());

        mainPage.spendingsTableShouldHaveSize(0);

    }

}
