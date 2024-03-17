package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.*;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.TestFriend;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.model.*;
import guru.qa.niffler.utils.DataUtils;

import java.io.IOException;
import java.util.Date;

public class RestCreateUserExtension extends CreateUserExtension {

    SpendApiClient spendApiClient = new SpendApiClient();
    CategoryApiClient categoryApiClient = new CategoryApiClient();
    AuthApiClient authApiClient = new AuthApiClient();
    UserApiClient userApiClient = new UserApiClient();
    FriendsApiClient friendsApiClient = new FriendsApiClient();

    @Override
    public UserJson createUser(TestUser user) {
        String username = user.username().isEmpty()
                ? DataUtils.generateRandomUsername()
                : user.username();
        String password = user.password().isEmpty()
                ? "12345"
                : user.password();

        return registerUser(username, password);
    }

    @Override
    public UserJson createCategory(TestUser user, UserJson createdUser) {

        for (GenerateCategory category : user.categories()) {

            CategoryJson categoryJson = new CategoryJson(null, category.category(), createdUser.username());

            try {
                categoryApiClient.addCategory(categoryJson);
            } catch (IOException e) {
                throw new RuntimeException("Error: Unable to create category " + category.category());
            }

        }

        return createdUser;
    }

    @Override
    public UserJson createSpend(TestUser user, UserJson createdUser) {

        for (GenerateSpend spend : user.spends()) {
            SpendJson spendJson = new SpendJson(
                    null, new Date(), spend.category(), spend.currency(), spend.amount(), spend.description(), createdUser.username()
            );

            try {
                spendApiClient.addSpend(spendJson);
            } catch (IOException e) {
                throw new RuntimeException("Error: Unable to create spend " + spend.description());
            }
        }

        return createdUser;
    }

    @Override
    public UserJson createFriend(TestUser user, UserJson createdUser) {
        for (TestFriend friend : user.friends()) {

            UserJson friendUser = registerUser(DataUtils.generateRandomUsername(), "12345");

            String user1 = createdUser.username();
            String user2 = friendUser.username();

            FriendJson friend1 = new FriendJson(user1);
            FriendJson friend2 = new FriendJson(user2);

            if (friend.friendState() == FriendState.FRIEND) {
                try {
                    friendsApiClient.addFriend(friend1.username(), friend2);
                    friendsApiClient.acceptInvitation(user2, friend1);
                } catch (IOException e) {
                    throw new RuntimeException("Unable to add friend: " + friendUser.username());
                }
            }

            if (friend.friendState() == FriendState.INVITE_SENT) {
                try {
                    friendsApiClient.addFriend(friend1.username(), friend2);
                } catch (IOException e) {
                    throw new RuntimeException("Unable to add friend: " + friendUser.username());
                }
            }

            if (friend.friendState() == FriendState.INVITE_RECEIVED) {
                try {
                    friendsApiClient.addFriend(friend2.username(), friend1);
                } catch (IOException e) {
                    throw new RuntimeException("Unable to add friend: " + friendUser.username());
                }
            }

        }

        return createdUser;
    }

    private UserJson registerUser(String username, String password) {
        try {
            authApiClient.doRegister(username, password);
        } catch (IOException e) {
            throw new RuntimeException("Unable to register user: " + username);
        }

        UserJson getCurrentUserResult;
        for (int i = 0; i < 10; i++) {
            try {
                getCurrentUserResult = userApiClient.getCurrentUser(username);
                if (getCurrentUserResult != null) {
                    return new UserJson(
                            getCurrentUserResult.id(), getCurrentUserResult.username(), getCurrentUserResult.firstname(), getCurrentUserResult.surname(), getCurrentUserResult.currency(), getCurrentUserResult.photo(), null,
                            new TestData(password, null)
                    );
                }
            } catch (IOException e) {
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        throw new RuntimeException("Unable to find user in userApi after registration: " + username);
    }

}
