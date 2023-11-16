package coffeemeet.server.user.implement;

import static coffeemeet.server.common.fixture.entity.UserFixture.token;
import static coffeemeet.server.common.fixture.entity.UserFixture.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.infrastructure.UserRepository;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserCommandTest {

  @InjectMocks
  private UserCommand userCommand;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserQuery userQuery;

  @Test
  @DisplayName("유저를 저장할 수 있다.")
  void saveUserTest() {
    // given
    User user = user();
    Long id = user.getId();

    given(userRepository.save(any(User.class))).willReturn(user);

    // when
    Long userId = userCommand.saveUser(user);

    // then
    assertThat(userId).isEqualTo(id);
  }

  @Test
  @DisplayName("유저 정보를 업데이트할 수 있다.")
  void updateUserTest() {
    // given
    User user = user();
    given(userRepository.save(any(User.class))).willReturn(user);

    // when, then
    assertThatCode(() -> userCommand.updateUser(user)).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("유저를 삭제할 수 있다.")
  void deleteUserTest() {
    // given
    Long userId = 1L;

    // when, then
    assertThatCode(() -> userCommand.deleteUser(userId)).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("유저의 닉네임을 업데이트할 수 있다.")
  void updateUserInfoTest() {
    // given
    User user = user();
    String newNickname = "newNickname";

    willDoNothing().given(userQuery).hasDuplicatedNickname(any());

    // when
    userCommand.updateUserInfo(user, newNickname);

    // then
    assertThat(user.getProfile().getNickname()).isEqualTo(newNickname);
  }

  @Test
  @DisplayName("채팅룸에 유저를 할당할 수 있다.")
  void assignUsersToChattingRoomTest() {
    // given
    Set<Long> userIds = Set.of(1L, 2L);
    ChattingRoom chattingRoom = new ChattingRoom();
    Set<User> users = Set.of(user(), user());
    given(userQuery.getUsersByIdSet(userIds)).willReturn(users);

    // when
    userCommand.assignUsersToChattingRoom(userIds, chattingRoom);

    // then
    for (User user : users) {
      assertThat(user.getChattingRoom()).isEqualTo(chattingRoom);
    }
  }

  @Test
  @DisplayName("알림 정보를 등록하거나 업데이트할 수 있다.")
  void registerOrUpdateNotificationToken() {
    // given
    User user = user();
    String token = token();
    given(userQuery.getUserById(user.getId())).willReturn(user);

    // when
    userCommand.registerOrUpdateNotificationToken(user.getId(), token);

    // then
    assertThat(user.getNotificationInfo().getToken()).isEqualTo(token);
  }

  @Test
  @DisplayName("푸시 알림을 거부할 수 있다.")
  void unsubscribeNotification() {
    User user = user();
    given(userQuery.getUserById(user.getId())).willReturn(user);

    // when
    userCommand.unsubscribeNotification(user.getId());

    // then
    assertThat(user.getNotificationInfo().isSubscribedToNotification()).isFalse();
  }

}
