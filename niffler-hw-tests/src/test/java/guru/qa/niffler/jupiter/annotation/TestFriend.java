package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.model.FriendState;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestFriend {

    FriendState friendState();

}