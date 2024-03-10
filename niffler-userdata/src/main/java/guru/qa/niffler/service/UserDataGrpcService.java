package guru.qa.niffler.service;

import com.google.protobuf.StringValue;
import guru.qa.grpc.niffler.grpc.NifflerUserdataServiceGrpc;
import guru.qa.grpc.niffler.grpc.User;
import guru.qa.grpc.niffler.grpc.UsersResponse;
import guru.qa.niffler.data.CurrencyValues;
import guru.qa.niffler.data.FriendsEntity;
import guru.qa.niffler.data.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.ex.NotFoundException;
import guru.qa.niffler.model.FriendState;
import guru.qa.niffler.model.UserJson;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.Nonnull;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@GrpcService
public class UserDataGrpcService extends NifflerUserdataServiceGrpc.NifflerUserdataServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(UserDataGrpcService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserDataGrpcService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void getUserByUsername(StringValue user, StreamObserver<User> responseObserver) {

        UserEntity userEntity = getRequiredUser(user.getValue());

        User response = User.newBuilder()
                .setId(userEntity.getId().toString())
                .setUsername(userEntity.getUsername())
                .setFirstname(userEntity.getFirstname() != null ? userEntity.getFirstname() : "")
                .setSurname(userEntity.getSurname() != null ? userEntity.getSurname() : "")
                .setCurrency(userEntity.getCurrency().toString())
                .build();

        responseObserver.onNext(response);

        responseObserver.onCompleted();

    }


    @Override
    public void getAllUsers(StringValue user, StreamObserver<UsersResponse> responseObserver) {

        List<UserJson> all = allUsers(user.getValue());

        UsersResponse response = UsersResponse.newBuilder()
                .addAllAllUsers(all.stream().map(userJson -> User.newBuilder()
                                .setId(userJson.id().toString())
                                .setUsername(userJson.username())
                                .setFirstname(userJson.firstname() != null ? userJson.firstname() : "")
                                .setSurname(userJson.surname() != null ? userJson.surname() : "")
                                .setCurrency(userJson.currency().toString())
                                .setFriendState(guru.qa.grpc.niffler.grpc.FriendState.valueOf(
                                                userJson.friendState() != null ?
                                                        userJson.friendState().name() :
                                                        guru.qa.grpc.niffler.grpc.FriendState.EMPTY.name()
                                        )
                                )
                                .build())
                        .toList())
                .build();

        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }


    @Override
    public void updateUser(User user, StreamObserver<User> responseObserver) {

        UserEntity userEntity = getRequiredUser(user.getUsername());
        userEntity.setFirstname(user.getFirstname());
        userEntity.setSurname(user.getSurname());
        userEntity.setCurrency(CurrencyValues.valueOf(user.getCurrency()));

        UserEntity saved = userRepository.save(userEntity);

        User response = User.newBuilder()
                .setId(saved.getId().toString())
                .setUsername(saved.getUsername())
                .setFirstname(saved.getFirstname())
                .setSurname(saved.getSurname())
                .setCurrency(saved.getCurrency().toString())
                .build();

        responseObserver.onNext(response);

        responseObserver.onCompleted();

    }

    private UserEntity getRequiredUser(@Nonnull String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new NotFoundException("Can`t find user by username: " + username);
        }
        return user;
    }

    private List<UserJson> allUsers(@Nonnull String username) {
        Set<UserJson> result = new HashSet<>();
        for (UserEntity user : userRepository.findByUsernameNot(username)) {
            List<FriendsEntity> sendInvites = user.getFriends();
            List<FriendsEntity> receivedInvites = user.getInvites();

            if (!sendInvites.isEmpty() || !receivedInvites.isEmpty()) {
                Optional<FriendsEntity> inviteToMe = sendInvites.stream()
                        .filter(i -> i.getFriend().getUsername().equals(username))
                        .findFirst();

                Optional<FriendsEntity> inviteFromMe = receivedInvites.stream()
                        .filter(i -> i.getUser().getUsername().equals(username))
                        .findFirst();

                if (inviteToMe.isPresent()) {
                    FriendsEntity invite = inviteToMe.get();
                    result.add(UserJson.fromEntity(user, invite.isPending()
                            ? FriendState.INVITE_RECEIVED
                            : FriendState.FRIEND));
                }
                if (inviteFromMe.isPresent()) {
                    FriendsEntity invite = inviteFromMe.get();
                    result.add(UserJson.fromEntity(user, invite.isPending()
                            ? FriendState.INVITE_SENT
                            : FriendState.FRIEND));
                }
            } else {
                result.add(UserJson.fromEntity(user));
            }
        }
        return new ArrayList<>(result);
    }

}
