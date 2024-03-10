package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.UserQueue;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.annotation.UserQueue.UserType.REQUEST_SENT;

@ExtendWith(UsersQueueExtension.class)
public class FriendsSentTest extends BaseWebTest {

    @BeforeEach
    void doLogin(@UserQueue(REQUEST_SENT) UserJson user) {
        Selenide.open("http://127.0.0.1:3000/main");
        welcomePage.clickLoginLink();
        loginPage.login(user.username(), user.testData().password());
    }

    @Test
    void requestSentShouldBeDisplayed(@UserQueue(REQUEST_SENT) UserJson user) {
        topMenu.clickPeopleTopMenu();
        peoplePage.requestSentShouldBeDisplayed();
    }
}
