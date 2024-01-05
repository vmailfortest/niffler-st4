package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.jupiter.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.User.UserType.*;

@ExtendWith(UsersQueueExtension.class)
public class FriendsTest extends BaseWebTest{
    public WelcomePage welcomePage = new WelcomePage();
    public LoginPage loginPage = new LoginPage();
    public TopMenu topMenu = new TopMenu();
    public MainPage mainPage = new MainPage();
    public FriendsPage friendsPage = new FriendsPage();
    public PeoplePage peoplePage = new PeoplePage();

    @BeforeEach
    void doLogin(@User(WITH_FRIENDS) UserJson user) {
        Selenide.open("http://127.0.0.1:3000/main");
        welcomePage.clickLoginLink();
        loginPage.login(user.username(), user.testData().password());
    }

    @Test
    void friendsTableShouldNotBeEmpty0(@User(WITH_FRIENDS) UserJson user) throws Exception {
        Thread.sleep(3000);
    }

    @Test
    void friendsTableShouldNotBeEmpty1(@User(WITH_FRIENDS) UserJson user) throws Exception {
        Thread.sleep(3000);
    }

    @Test
    void friendsTableShouldNotBeEmpty2(@User(WITH_FRIENDS) UserJson user) throws Exception {
        Thread.sleep(3000);
    }

    @Test
    void friendsShouldBeDisplayed(@User(WITH_FRIENDS) UserJson user) {
        topMenu.clickFriendsTopMenu();
        friendsPage.friendShouldBeDisplayed();
    }

    @Test
    void requestSentShouldBeDisplayed(@User(REQUEST_SENT) UserJson user) {
        topMenu.clickPeopleTopMenu();
        peoplePage.requestSentShouldBeDisplayed();
    }

    @Test
    void requestReceivedShouldBeDisplayed0(@User(REQUEST_RECEIVED) UserJson user) {
        topMenu.clickFriendsTopMenu();
        friendsPage.requestReceivedShouldBeDisplayed();
    }

    @Test
    void requestReceivedShouldBeDisplayed1(@User(REQUEST_RECEIVED) UserJson user) {
        topMenu.clickFriendsTopMenu();
        friendsPage.requestReceivedShouldBeDisplayed();
    }

    @Test
    void testWithoutParam() throws Exception {
        Thread.sleep(3000);
    }
}
