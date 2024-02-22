package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.jupiter.extension.ContextHolderExtension;
import guru.qa.niffler.pages.FriendsPage;
import guru.qa.niffler.pages.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({ContextHolderExtension.class, ApiLoginExtension.class})
public class hw14Test extends BaseWebTest {

    @ApiLogin(username = "duck", password = "12345")
    @Test
    void loginWithApi() {

        Selenide.open(MainPage.URL);
        mainPage.spendingsTableShouldHaveRows();

    }

}
