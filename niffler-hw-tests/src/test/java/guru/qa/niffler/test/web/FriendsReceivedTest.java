package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.UserQueue;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.annotation.UserQueue.UserType.REQUEST_RECEIVED;

@ExtendWith(UsersQueueExtension.class)
public class FriendsReceivedTest extends BaseWebTest {

    @BeforeEach
    void doLogin(@UserQueue(REQUEST_RECEIVED) UserJson user) {
        Selenide.open("http://127.0.0.1:3000/main");
        welcomePage.clickLoginLink();
        loginPage.login(user.username(), user.testData().password());
    }

    @Test
    void requestReceivedShouldBeDisplayed0(@UserQueue(REQUEST_RECEIVED) UserJson user) {
        topMenu.clickFriendsTopMenu();
        friendsPage.requestReceivedShouldBeDisplayed();
    }

    @Test
    void requestReceivedShouldBeDisplayed1(@UserQueue(REQUEST_RECEIVED) UserJson user) {
        topMenu.clickFriendsTopMenu();
        friendsPage.requestReceivedShouldBeDisplayed();
    }
}
