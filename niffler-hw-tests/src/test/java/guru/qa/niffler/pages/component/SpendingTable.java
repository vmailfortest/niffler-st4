package guru.qa.niffler.pages.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.SpendCollectionCondition.spends;

public class SpendingTable extends BaseComponent<SpendingTable> {

    private SelenideElement descriptionInput = $(".editable__input[name='description']");
    private SelenideElement saveRowBtn = $(".spendings__button-group .button-icon_type_submit");

    public SpendingTable() {
        super($(".spendings-table tbody"));
    }

    public SpendingTable checkSpends(SpendJson... expectedSpends) {
        getSelf().$$("tr").should(spends(expectedSpends));
        return this;
    }

    @Step("Select spend by index '{index}'")
    public void selectSpendByIndex(int index) {
        getSelf().$$("tr")
                .get(index)
                .$("td")
                .scrollTo()
                .click();
    }

    @Step("Select spend by description '{description}'")
    public void selectSpendByDescription(String description) {
        getSelf().$$("tr")
                .find(text(description))
                .$("td")
                .scrollTo()
                .click();
    }

    @Step("Click update spend btn by description '{description}'")
    public void clickUpdateSpendBtnByDescription(String oldDesc, String newDesc) {
        getSelf().$$("tr")
                .find(text(oldDesc))
                .$(".button-icon_type_edit")
                .scrollTo()
                .click();

        descriptionInput.setValue(newDesc);
        saveRowBtn.scrollTo();
        saveRowBtn.click();
    }

}
