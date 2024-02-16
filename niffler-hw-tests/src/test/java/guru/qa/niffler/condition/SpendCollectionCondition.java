package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.SpendJson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class SpendCollectionCondition {

    public static CollectionCondition spends(SpendJson... expectedSpends) {
        return new CollectionCondition() {

            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy", Locale.ENGLISH);

            @Override
            public void fail(CollectionSource collection, CheckResult lastCheckResult, Exception cause, long timeoutMs) {

                throw new UnsupportedOperationException(lastCheckResult.getActualValue().toString());

            }

            @Nonnull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {

                StringBuilder failedValues = new StringBuilder();

                if (elements.size() != expectedSpends.length) {

                    failedValues.append("Actual rows").append("\n");
                    for (WebElement element : elements) {
                        failedValues.append(element.getText().replace("\n", " / ")).append("\n");
                    }

                    return CheckResult.rejected("Incorrect table size.", failedValues);
                }

                for (WebElement element : elements) {

                    failedValues.append("Actual / Expected").append("\n");

                    List<WebElement> tds = element.findElements(By.cssSelector("td"));

                    boolean isPassed = true;

                    for (SpendJson expectedSpend : expectedSpends) {

                        String text = tds.get(1).getText();
                        if (!text.equals(formatter.format(expectedSpend.spendDate()))) {
                            isPassed = false;
                            failedValues.append(text).append(" / ").append(expectedSpend.spendDate()).append("\n");
                        }

                        text = tds.get(2).getText();
                        if (!text.equals(String.format("%.0f", expectedSpend.amount()))) {
                            isPassed = false;
                            failedValues.append(text).append(" / ").append(expectedSpend.amount()).append("\n");
                        }

                        text = tds.get(3).getText();
                        if (!text.equals(expectedSpend.currency().toString())) {
                            isPassed = false;
                            failedValues.append(text).append(" / ").append(expectedSpend.currency()).append("\n");
                        }

                        text = tds.get(4).getText();
                        if (!text.equals(expectedSpend.category())) {
                            isPassed = false;
                            failedValues.append(text).append(" / ").append(expectedSpend.category()).append("\n");
                        }

                        text = tds.get(5).getText();
                        if (!text.equals(expectedSpend.description())) {
                            isPassed = false;
                            failedValues.append(text).append(" / ").append(expectedSpend.description()).append("\n");
                        }

                    }

                    if (isPassed) {
                        return CheckResult.accepted();
                    } else {
                        return CheckResult.rejected("Incorrect spends content.", failedValues.toString());
                    }
                }

                return super.check(driver, elements);
            }

            @Override
            public boolean missingElementSatisfiesCondition() {
                return false;
            }
        };
    }
}
