package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pages.message.SuccessMsg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Date;

import static com.codeborne.selenide.Selenide.open;

public class Lesson12Test extends BaseWebTest {

    @BeforeEach
    void doLogin() {
        open(CFG.frontUrl());
        welcomePage.clickLoginLink();
        loginPage.login("duck", "12345");
    }

//    @GenerateSpend(
//            username = "duck",
//            description = "",
//            amount = 72500.00,
//            category = "Обучение",
//            currency = CurrencyValues.RUB
//    )
    @Test
    void spendTableValuesAreCorrect() throws IOException {

        SpendJson spend1 = new SpendJson(
                null, new Date(), "apiCategory", CurrencyValues.USD,10d, "spend_1", "duck"
        );
        SpendJson spend2 = new SpendJson(
                null, new Date(), "apiCategory", CurrencyValues.EUR,20d, "spend_2", "duck"
        );
        SpendJson spend3 = new SpendJson(
                null, new Date(), "apiCategory", CurrencyValues.RUB,30d, "spend_3", "duck"
        );
        SpendJson spend4 = new SpendJson(
                null, new Date(), "apiCategory", CurrencyValues.KZT,40d, "spend_4", "duck"
        );

        SpendApiClient spendApiClient = new SpendApiClient();
        spendApiClient.addSpend(spend1);
        spendApiClient.addSpend(spend2);
        spendApiClient.addSpend(spend3);
        spendApiClient.addSpend(spend4);

//        spend2 = new SpendJson(
//                null, new Date(), "apiCategory", CurrencyValues.EUR,25d, "spend_2_upd2", "duck"
//        );
//        spend4 = new SpendJson(
//                null, new Date(), "apiCategory", CurrencyValues.KZT,45d, "spend_4_upd4", "duck"
//        );

        Selenide.refresh();

        mainPage.spendingsTableShouldHaveRows();
        mainPage.getSpendingTable().checkSpends(spend1, spend2, spend3, spend4);

    }

    @Test
    void avatarShouldBeDisplayedInHeader() {

        topMenu.clickProfileTopMenu();

        profilePage.addAvatar("images/duck.jpg");

        profilePage.checkMessage(SuccessMsg.PROFILE_MSG);

        mainPage.checkAvatar("images/duck.jpg");

    }
}
