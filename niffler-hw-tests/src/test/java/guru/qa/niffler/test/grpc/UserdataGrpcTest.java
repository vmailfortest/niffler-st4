package guru.qa.niffler.test.grpc;

import com.github.javafaker.Faker;
import com.google.protobuf.StringValue;
import guru.qa.grpc.niffler.grpc.NifflerUserdataServiceGrpc;
import guru.qa.grpc.niffler.grpc.User;
import guru.qa.grpc.niffler.grpc.UsersResponse;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.utils.GrpcConsoleInterceptor;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.Allure;
import io.qameta.allure.grpc.AllureGrpc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserdataGrpcTest {

    protected static final Config CFG = Config.getInstance();

    protected static Channel channel;
    protected static NifflerUserdataServiceGrpc.NifflerUserdataServiceBlockingStub blockingStub;

    static {
        channel = ManagedChannelBuilder.forAddress(CFG.userdataGrpcHost(), CFG.userdataGrpcPort())
                .intercept(new AllureGrpc(), new GrpcConsoleInterceptor())
                .usePlaintext()
                .build();

        blockingStub = NifflerUserdataServiceGrpc.newBlockingStub(channel);
    }

    @Test
    void getUserTest() {
        String expectedUser = "duck";

        var response = blockingStub.getUserByUsername(StringValue.of(expectedUser));

        Allure.step("Verify getUserByUsername", () -> {
            Assertions.assertEquals(
                    expectedUser,
                    response.getUsername()
            );
        });
    }

    @Test
    void getAllUsersTest() {
        String expectedUser = "tester1";
        String expectedUserFriend = "tester2";
        String expectedfriendState = "INVITE_SENT";

        UsersResponse response = blockingStub.getAllUsers(StringValue.of(expectedUser));

        Allure.step("Verify getAllUsers", () -> {
            Assertions.assertEquals(
                    expectedfriendState,
                    response.getAllUsersList().stream().filter(user -> user.getUsername().equals(expectedUserFriend))
                            .findFirst().get().getFriendState().name()
            );
        });
    }

    @Test
    void updateUserTest() {
        String expectedUser = "duck";
        String expectedFirstname = new Faker().numerify("Donald_######");

        final User response = blockingStub.updateUser(User.newBuilder()
                .setUsername(expectedUser)
                .setFirstname(expectedFirstname)
                .setSurname("Ducker")
                .setCurrency("EUR")
                .build()
        );

        Allure.step("Verify updateUser", () -> {
            Assertions.assertEquals(
                    expectedFirstname,
                    response.getFirstname()
            );
        });
    }

}
