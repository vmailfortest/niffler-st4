package guru.qa.niffler.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.pages.component.SpendingTable;
import io.qameta.allure.Step;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static guru.qa.niffler.condition.PhotoCondition.photoFromClasspath;

public class MainPage extends BasePage {
    private ElementsCollection spendings = $(".spendings-table tbody").$$("tr");

    private SelenideElement categoryField = $x("//input[contains(@id, 'react-select')]");
    private ElementsCollection categoryValues = $$x("//div[@aria-disabled='false' and contains(@id, 'react-select')]");

    private SelenideElement amountField = $("input[name='amount']");
    private SelenideElement dateField = $(".calendar-wrapper input");
    private SelenideElement descriptionField = $("input[name='description']");

    private SelenideElement addSpendingButton = $("button[type='submit']");

    private SelenideElement todayFilterBtn = $(byText("Today"));
    private SelenideElement lastWeekFilterBtn = $(byText("Last week"));
    private SelenideElement lastMonthFilterBtn = $(byText("Last month"));
    private SelenideElement allTimeFilterBtn = $(byText("All time"));

    private SelenideElement currencyInput = $x("//div[@class='spendings__buttons']/following-sibling::div");
    private ElementsCollection currencies = $$x("//div[@aria-disabled='false' and contains(@id, 'react-select')]");
    private SelenideElement resetFiltersBtn = $(".spendings__filters .button-icon_type_close");
    private SelenideElement deleteSelectedButton = $(byText("Delete selected"));

    DateTimeFormatter spendingDateformatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final SelenideElement avatar = $(".header__avatar");
    private final SpendingTable spendingTable = new SpendingTable();

    @Step("Add spending {category} / {amount} / {description}")
    public void addSpending(String category, String amount, LocalDate date, String description) {
        categoryField.click();
        categoryField.setValue(category);
        categoryValues.findBy(Condition.text(category)).click();

        amountField.setValue(amount);

        dateField.clear();
        dateField.setValue(spendingDateformatter.format(date));

        descriptionField.setValue(description);

        addSpendingButton.click();
    }

    @Step("Select spending by description '{description}'")
    public void selectSpendingByDescription(String description) {
        spendings.find(text(description))
                .$("td")
                .scrollTo()
                .click();
    }

    @Step("Spendings table should have size '{expectedSize}'")
    public void spendingsTableShouldHaveSize(int expectedSize) {
        spendings.shouldHave(size(expectedSize));
    }

    @Step("Spendings table should have rows")
    public void spendingsTableShouldHaveRows() {
        spendings.shouldHave(CollectionCondition.sizeGreaterThan(0));
    }

    @Step("check avatar")
    public void checkAvatar(String imageName) {
        avatar.shouldHave(photoFromClasspath(imageName));
    }

    public SpendingTable getSpendingTable() {
        return spendingTable;
    }

    @Step("Click Today filter btn")
    public void clickTodayFilterBtn() {
        todayFilterBtn.click();
    }

    @Step("Click Last Week filter btn")
    public void clickLastWeekFilterBtn() {
        lastWeekFilterBtn.click();
    }

    @Step("Click Last Month filter btn")
    public void clickLastMonthFilterBtn() {
        lastMonthFilterBtn.click();
    }

    @Step("Click All Time filter btn")
    public void clickAllTimeFilterBtn() {
        allTimeFilterBtn.click();
    }

    @Step("Select Currency filter '{currency}'")
    public void selectSpendCurrencyFilter(CurrencyValues currency) {
        currencyInput.click();
        currencies.findBy(text(currency.name())).click();
    }

    @Step("Click Reset filters btn")
    public void clickResetFiltersBtn() {
        resetFiltersBtn.click();
    }

    @Step("Click Delete Selected button")
    public void clickDeleteSelectedButton() {
        deleteSelectedButton.scrollTo();
        deleteSelectedButton.click();
    }

}
