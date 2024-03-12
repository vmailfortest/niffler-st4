package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.UserQueue;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.annotation.UserQueue.UserType.*;

@ExtendWith(UsersQueueExtension.class)
public class FriendsWithoutBeforeEachParamTest extends BaseWebTest{

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000/main");
        welcomePage.clickLoginLink();
    }

    @Test
    void friendsShouldBeDisplayed(@UserQueue(WITH_FRIENDS) UserJson user) throws Exception {
        loginPage.login(user.username(), user.testData().password());
        topMenu.clickFriendsTopMenu();
        friendsPage.friendShouldBeDisplayed();
    }

    @Test
    void requestSentShouldBeDisplayed(@UserQueue(REQUEST_SENT) UserJson user) throws Exception {
        loginPage.login(user.username(), user.testData().password());
        topMenu.clickPeopleTopMenu();
        peoplePage.requestSentShouldBeDisplayed();
    }

    @Test
    void requestReceivedShouldBeDisplayed(@UserQueue(REQUEST_RECEIVED) UserJson user) throws Exception {
        loginPage.login(user.username(), user.testData().password());
        topMenu.clickFriendsTopMenu();
        friendsPage.requestReceivedShouldBeDisplayed();
    }
}
