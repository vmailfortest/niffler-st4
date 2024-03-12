package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.pages.message.ErrorMsg;
import guru.qa.niffler.pages.message.SuccessMsg;
import guru.qa.niffler.test.web.BaseWebTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class hw11ProfileTest extends BaseWebTest {

    private final static String tester1 = "tester1";
    private final static String tester2 = "tester2";

    @TestUser()
    @BeforeEach
    public void beforeEach(UserAuthEntity userAuth){
        Selenide.open("http://127.0.0.1:3000/main");
        welcomePage.clickLoginLink();
//        loginPage.login(userAuth.getUsername(), "12345");
    }

    @TestUser()
    @Test
    void createCategory(UserAuthEntity userAuth) {

        loginPage.login(userAuth.getUsername(), "12345");

        topMenu.clickProfileTopMenu();

        profilePage.createCategory(new Faker().numerify("cat###"));

        profilePage.checkMessage(SuccessMsg.CATEGORY_MSG);
    }

    @TestUser()
    @Test
    void createExistingCategory(UserAuthEntity userAuth) {

        loginPage.login(userAuth.getUsername(), "12345");

        topMenu.clickProfileTopMenu();

        profilePage.createCategory("testCategory1");

        Selenide.refresh();

        profilePage.createCategory("testCategory1");

        profilePage.checkMessage(ErrorMsg.CATEGORY_MSG);
    }

    @TestUser()
    @Test
    void setProfileData(UserAuthEntity userAuth) throws IOException {

        loginPage.login(userAuth.getUsername(), "12345");

        topMenu.clickProfileTopMenu();

        profilePage.setProfileInfo("Donald", "Ducker", CurrencyValues.USD);

        profilePage.checkMessage(SuccessMsg.PROFILE_MSG);
    }

}
