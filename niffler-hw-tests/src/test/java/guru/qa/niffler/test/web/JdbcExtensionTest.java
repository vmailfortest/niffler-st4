package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.extension.UserRepositoryExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ExtendWith(UserRepositoryExtension.class)
public class JdbcExtensionTest extends BaseWebTest {

    @TestUser(username = "valentin_1a", password = "12345")
    @Test
    void statisticShouldBeVisibleAfterLogin(UserAuthEntity userAuth) {
        System.out.println(userAuth.toString());
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userAuth.getUsername());
        $("input[name='password']").setValue(userAuth.getPassword());
        $("button[type='submit']").click();
        $(".main-content__section-stats").should(visible);
    }

    @TestUser()
    @Test
    void statisticShouldBeVisibleAfterLoginEmptyDbUser(UserAuthEntity userAuth) {
        System.out.println(userAuth.toString());
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userAuth.getUsername());
        $("input[name='password']").setValue(userAuth.getPassword());
        $("button[type='submit']").click();
        $(".main-content__section-stats").should(visible);
    }
}
