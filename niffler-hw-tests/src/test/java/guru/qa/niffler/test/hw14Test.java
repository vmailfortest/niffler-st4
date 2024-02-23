package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.DbUser;
import guru.qa.niffler.pages.MainPage;
import org.junit.jupiter.api.Test;

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

}
