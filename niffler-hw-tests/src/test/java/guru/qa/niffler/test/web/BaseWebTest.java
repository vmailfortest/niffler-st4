package guru.qa.niffler.test.web;

import guru.qa.niffler.api.*;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.pages.*;

@WebTest
public abstract class BaseWebTest {

    protected static final Config CFG = Config.getInstance();

    protected RegisterPage registerPage = new RegisterPage();
    protected WelcomePage welcomePage = new WelcomePage();
    protected LoginPage loginPage = new LoginPage();
    protected TopMenu topMenu = new TopMenu();
    protected MainPage mainPage = new MainPage();
    protected FriendsPage friendsPage = new FriendsPage();
    protected PeoplePage peoplePage = new PeoplePage();
    protected ProfilePage profilePage = new ProfilePage();

    protected SpendApiClient spendApiClient = new SpendApiClient();
    protected CategoryApiClient categoryApiClient = new CategoryApiClient();
    protected CurrencyApiClient currencyApiClient = new CurrencyApiClient();
    protected UserApiClient userApiClient = new UserApiClient();
    protected FriendsApiClient friendsApiClient = new FriendsApiClient();

}
