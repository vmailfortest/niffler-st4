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

                    failedValues.append("Incorrect table size.").append("\n");
                    failedValues.append("   Actual rows:").append("\n");
                    for (WebElement element : elements) {
                        failedValues.append(element.getText().replace("\n", " / ")).append("\n");
                    }

                    failedValues.append("   Expected rows:").append("\n");
                    for (SpendJson spend : expectedSpends) {
                        failedValues
                                .append(formatter.format(spend.spendDate())).append(" / ")
                                .append(String.format("%.0f", spend.amount())).append(" / ")
                                .append(spend.currency().toString()).append(" / ")
                                .append(spend.category()).append(" / ")
                                .append(spend.description())
                                .append("\n");
                    }

                    return CheckResult.rejected("Incorrect table size.", failedValues);
                }

                boolean isPassed = true;

                failedValues.append("Actual / Expected").append("\n");

                for (int i = 0; i < elements.size(); i++) {

                    SpendJson expectedSpend = expectedSpends[i];

                    failedValues.append("   Row " + i).append("\n");

                    List<WebElement> tds = elements.get(i).findElements(By.cssSelector("td"));

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

            @Override
            public boolean missingElementSatisfiesCondition() {
                return false;
            }
        };
    }
}
