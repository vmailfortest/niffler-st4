package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.*;
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
    void severalUsers(@User() UserJson user,
                      @User(OUTER) UserJson[] outerUsers) {

        System.out.println("USER: " + user);
        System.out.println("USERS-OUTER: " + Arrays.toString(outerUsers));

        Selenide.open(MainPage.URL);
        mainPage.spendingsTableShouldHaveSize(0);

    }

    @ApiLogin(user = @TestUser(
            categories = {
                    @GenerateCategory(category = "testCat-1"),
                    @GenerateCategory(category = "testCat-2")
            },
            spends = {
                    @GenerateSpend(category = "testCat-1", amount = 11d, description = "testSpend-1"),
                    @GenerateSpend(category = "testCat-2", amount = 12d, description = "testSpend-2"),
            }
    ))
    @Test
    void userWithCategoriesAndSpends(@User() UserJson user) {

        System.out.println("USER: " + user);

        Selenide.open(MainPage.URL);
        mainPage.spendingsTableShouldHaveSize(2);

    }

}
