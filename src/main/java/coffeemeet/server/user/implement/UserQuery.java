package coffeemeet.server.user.implement;

import static coffeemeet.server.user.exception.UserErrorCode.ALREADY_EXIST_NICKNAME;
import static coffeemeet.server.user.exception.UserErrorCode.ALREADY_REGISTERED_USER;
import static coffeemeet.server.user.exception.UserErrorCode.NOT_EXIST_USER;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.common.execption.DuplicatedDataException;
import coffeemeet.server.common.execption.NotFoundException;
import coffeemeet.server.user.domain.NotificationInfo;
import coffeemeet.server.user.domain.OAuthInfo;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.infrastructure.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQuery {

  private static final String NOT_EXIST_USER_MESSAGE = "해당 아이디(%s)에 일치하는 사용자는 존재하지 않습니다.";
  private static final String NOT_REGISTERED_USER_MESSAGE = "해당 로그인 타입(%s)에 대한 아이디(%s)와 일치하는 사용자는 존재하지 않습니다.";
  private static final String ALREADY_EXIST_NICKNAME_MESSAGE = "해당 닉네임(%s)은 이미 존재하는 닉네임입니다.";
  private static final String ALREADY_REGISTERED_USER_MESSAGE = "해당 사용자(%s)는 이미 회원가입 되었습니다.";

  private final UserRepository userRepository;

  public User getUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException(
            NOT_EXIST_USER,
            String.format(NOT_EXIST_USER_MESSAGE, userId))
        );
  }

  public boolean isRegistered(OAuthInfo oAuthInfo) {
    if (userRepository.existsUserByOauthInfo(oAuthInfo)) {
      getUserByOAuthInfo(oAuthInfo);
      return true;
    }
    return false;
  }

  public User getNonRegisteredUserById(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException(
            NOT_EXIST_USER,
            String.format(NOT_EXIST_USER_MESSAGE, userId))
        );
    if (user.isRegistered()) {
      throw new NotFoundException(
          ALREADY_REGISTERED_USER,
          String.format(ALREADY_REGISTERED_USER_MESSAGE, userId)
      );
    }
    return user;
  }

  public Set<User> getUsersByIdSet(Set<Long> userIds) {
    return new HashSet<>(userRepository.findByIdIn(userIds));
  }

  public Set<NotificationInfo> getNotificationInfosByIdSet(Set<Long> userIds) {
    return userRepository.findByIdIn(userIds).stream()
        .map(User::getNotificationInfo).collect(Collectors.toSet());
  }

  public User getUserByOAuthInfo(OAuthInfo oAuthInfo) {
    return userRepository.findByOauthInfo(oAuthInfo)
        .orElseThrow(
            () -> new NotFoundException(
                NOT_EXIST_USER,
                String.format(NOT_REGISTERED_USER_MESSAGE, oAuthInfo.getOauthProvider(),
                    oAuthInfo.getOauthProviderId()))
        );
  }

  public void hasDuplicatedNickname(String nickname) {
    if (userRepository.existsUserByProfile_Nickname(nickname)) {
      throw new DuplicatedDataException(
          ALREADY_EXIST_NICKNAME,
          String.format(ALREADY_EXIST_NICKNAME_MESSAGE, nickname)
      );
    }
  }

  public NotificationInfo getNotificationInfoByUserId(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException(
            NOT_EXIST_USER,
            String.format(NOT_EXIST_USER_MESSAGE, userId))
        ).getNotificationInfo();
  }

  public List<User> getUsersByRoom(ChattingRoom room) {
    return userRepository.findAllByChattingRoom(room);
  }

}
