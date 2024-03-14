package guru.qa.niffler.test.grphql;

import guru.qa.niffler.jupiter.annotation.*;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.FriendState;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.gql.GqlUpdateUser;
import guru.qa.niffler.model.gql.GqlUser;
import guru.qa.niffler.model.gql.GqlUsers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class GqlUsersTest extends BaseGraphQLTest {

    @Test
    @ApiLogin(
            user = @TestUser
    )
    void currentUserShouldBeReturned(@User UserJson testUser,
                                     @Token String bearerToken,
                                     @GqlRequestFile("gql/currentUserQuery.json") guru.qa.niffler.model.gql.GqlRequest request) throws Exception {

        final GqlUser response = gatewayGqlApiClient.currentUser(bearerToken, request);
        Assertions.assertEquals(
                testUser.username(),
                response.getData().getUser().getUsername()
        );
    }

    @ApiLogin(user = @TestUser(
            friends = {
                    @TestFriend(friendState = FriendState.FRIEND),
                    @TestFriend(friendState = FriendState.FRIEND)
            }
    ))
    @Test
    void userShouldHaveFriends(@User UserJson testUser,
                               @Token String bearerToken,
                               @GqlRequestFile("gql/getFriendsQuery.json") guru.qa.niffler.model.gql.GqlRequest request) throws Exception {

        final GqlUser response = gatewayGqlApiClient.getFriends(bearerToken, request);

        Assertions.assertEquals(
                2,
                response.getData().getUser().getFriends().size()
        );
    }

    @ApiLogin(user = @TestUser(
            friends = {
                    @TestFriend(friendState = FriendState.FRIEND)
            }
    ))
    @Test
    void twoFriendsSubqueryReturnsError(@User UserJson testUser,
                                        @Token String bearerToken,
                                        @GqlRequestFile("gql/getFriends2FriendsSubQuery.json") guru.qa.niffler.model.gql.GqlRequest request) throws Exception {

        final GqlUser response = gatewayGqlApiClient.getUsersWithError(bearerToken, request);

        Assertions.assertEquals(
                "Can`t fetch over 2 friends sub-queries",
                response.getErrors().get(0).message()
        );
    }

    @ApiLogin(user = @TestUser(
            friends = {
                    @TestFriend(friendState = FriendState.INVITE_RECEIVED)
            }
    ))
    @Test
    void twoInvitationsSubqueryReturnsError(@User UserJson testUser,
                                            @Token String bearerToken,
                                            @GqlRequestFile("gql/getFriends2InvitationsSubQuery.json") guru.qa.niffler.model.gql.GqlRequest request) throws Exception {

        final GqlUser response = gatewayGqlApiClient.getUsersWithError(bearerToken, request);

        Assertions.assertEquals(
                "Can`t fetch over 2 invitations sub-queries",
                response.getErrors().get(0).message()
        );
    }

    @ApiLogin(user = @TestUser)
    @Test
    void usersShouldBeReturned(@User UserJson testUser,
                               @Token String bearerToken,
                               @GqlRequestFile("gql/usersQuery.json") guru.qa.niffler.model.gql.GqlRequest request) throws Exception {

        final GqlUsers response = gatewayGqlApiClient.getUsers(bearerToken, request);

        Assertions.assertTrue(
                response.getData().getUsers().size() > 0
        );
    }

    @ApiLogin(user = @TestUser)
    @Test
    void userShouldBeUpdated(@Token String bearerToken,
                             @GqlRequestFile("gql/updateUserMutation.json") guru.qa.niffler.model.gql.GqlRequest request) throws Exception {

        final GqlUpdateUser response = gatewayGqlApiClient.updateUser(bearerToken, request);

        Assertions.assertEquals(
                "Pizzly",
                response.getData().getUpdateUser().getFirstname()
        );
        Assertions.assertEquals(
                "Pizzlyvich",
                response.getData().getUpdateUser().getSurname()
        );
        Assertions.assertEquals(
                CurrencyValues.EUR,
                response.getData().getUpdateUser().getCurrency()
        );
    }

    @CsvSource({
            "gql/getFriends2FriendsSubQuery.json",
            "gql/getFriends2InvitationsSubQuery.json"
    }
    )
    @ApiLogin(user = @TestUser(
            friends = {
                    @TestFriend(friendState = FriendState.FRIEND),
                    @TestFriend(friendState = FriendState.INVITE_RECEIVED)
            }
    ))
    @ParameterizedTest
    void parametrizedTestShouldWork(
            @GqlRequestFile guru.qa.niffler.model.gql.GqlRequest request,
            @Token String bearerToken) throws Exception {

        final GqlUser response = gatewayGqlApiClient.getUsersWithError(bearerToken, request);

        Assertions.assertEquals(
                1,
                response.getErrors().size()
        );
        Assertions.assertTrue(
                response.getErrors().get(0).message().contains("Can`t fetch over 2")
        );
    }

}
