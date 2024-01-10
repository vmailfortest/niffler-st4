package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.jupiter.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.User.UserType.REQUEST_SENT;

@ExtendWith(UsersQueueExtension.class)
public class FriendsSentTest extends BaseWebTest {
    public WelcomePage welcomePage = new WelcomePage();
    public LoginPage loginPage = new LoginPage();
    public TopMenu topMenu = new TopMenu();
    public MainPage mainPage = new MainPage();
    public FriendsPage friendsPage = new FriendsPage();
    public PeoplePage peoplePage = new PeoplePage();

    @BeforeEach
    void doLogin(@User(REQUEST_SENT) UserJson user) {
        Selenide.open("http://127.0.0.1:3000/main");
        welcomePage.clickLoginLink();
        loginPage.login(user.username(), user.testData().password());
    }

    @Test
    void requestSentShouldBeDisplayed(@User(REQUEST_SENT) UserJson user) {
        topMenu.clickPeopleTopMenu();
        peoplePage.requestSentShouldBeDisplayed();
    }
}