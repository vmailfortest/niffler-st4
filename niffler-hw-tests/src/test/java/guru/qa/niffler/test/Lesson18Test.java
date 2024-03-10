package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.annotation.TestUsers;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.MainPage;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static guru.qa.niffler.jupiter.annotation.User.Point.OUTER;

public class Lesson18Test extends BaseWebTest {

    @TestUsers({
            @TestUser,
            @TestUser
    })
    @ApiLogin(user = @TestUser)
    @Test
    void loginWithApi(@User() UserJson user,
                      @User(OUTER) UserJson[] outerUsers) {

        System.out.println("!!!: " + user);
        System.out.println("!!!-!!!: " + Arrays.toString(outerUsers));

        Selenide.open(MainPage.URL);
        mainPage.spendingsTableShouldHaveSize(0);

    }

}
