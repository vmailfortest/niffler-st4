package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.jupiter.annotation.DbUser;
import org.junit.jupiter.api.Test;

public class hw14dbUserOnlyTest extends BaseWebTest {

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
