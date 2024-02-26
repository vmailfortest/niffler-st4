package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.DbUser;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.jupiter.extension.ContextHolderExtension;
import guru.qa.niffler.jupiter.extension.DbUserExtension;
import guru.qa.niffler.pages.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({ContextHolderExtension.class, DbUserExtension.class, ApiLoginExtension.class})
public class hw14Test extends BaseWebTest {

    @ApiLogin(username = "duck", password = "12345")
    @Test
    void loginWithApi() {

        Selenide.open(MainPage.URL);
        mainPage.spendingsTableShouldHaveRows();

    }

    @ApiLogin(user = @DbUser)
    @Test
    void loginDbUserWithApi() {

        Selenide.open(MainPage.URL);
        mainPage.spendingsTableShouldHaveSize(0);

    }

    @ApiLogin(user = @DbUser(username = "hw14tester", password = "12345"))
    @Test
    void loginDbUserWithCredsWithApi() {

        Selenide.open(MainPage.URL);
        mainPage.spendingsTableShouldHaveSize(0);

    }

    @DbUser(username = "testUser", password = "12345")
    @Test
    void dbUserWithCreds(UserAuthEntity userAuth) {
        Selenide.open(CFG.frontUrl());
        welcomePage.clickLoginLink();
        loginPage.login(userAuth.getUsername(), userAuth.getPassword());

        mainPage.spendingsTableShouldHaveSize(0);

    }

    @DbUser()
    @Test
    void dbUserWithGeneratedCreds(UserAuthEntity userAuth) {
        Selenide.open(CFG.frontUrl());
        welcomePage.clickLoginLink();
        loginPage.login(userAuth.getUsername(), userAuth.getPassword());

        mainPage.spendingsTableShouldHaveSize(0);

    }

}
