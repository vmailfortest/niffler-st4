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
public class FriendsTest extends BaseWebTest {

    @BeforeEach
    void doLogin(@UserQueue(WITH_FRIENDS) UserJson user) {
        System.out.println("user0: " + user.username());
        Selenide.open("http://127.0.0.1:3000/main");
        welcomePage.clickLoginLink();
        loginPage.login(user.username(), user.testData().password());
    }

    @Test
    void friendsTableShouldNotBeEmpty0(@UserQueue(WITH_FRIENDS) UserJson user) throws Exception {
        Thread.sleep(3000);
    }

    @Test
    void friendsTableShouldNotBeEmpty1(@UserQueue(WITH_FRIENDS) UserJson user) throws Exception {
        Thread.sleep(3000);
    }

    @Test
    void friendsTableShouldNotBeEmpty2(@UserQueue(WITH_FRIENDS) UserJson user) throws Exception {
        Thread.sleep(3000);
    }

    @Test
    void friendsShouldBeDisplayed(@UserQueue(WITH_FRIENDS) UserJson user) {
        topMenu.clickFriendsTopMenu();
        friendsPage.friendShouldBeDisplayed();
    }

    @Test
    void testWithoutParam() throws Exception {

    }

    @Test
    void testWithSeveralParam(@UserQueue(REQUEST_SENT) UserJson userSent, @UserQueue(REQUEST_RECEIVED) UserJson userReceived) {
        System.out.println("user1: " + userSent.username());
        System.out.println("user2: " + userReceived.username());
    }
}
