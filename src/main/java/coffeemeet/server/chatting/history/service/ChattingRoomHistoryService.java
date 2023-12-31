package coffeemeet.server.chatting.history.service;

import coffeemeet.server.chatting.history.domain.ChattingRoomHistory;
import coffeemeet.server.chatting.history.domain.UserChattingHistory;
import coffeemeet.server.chatting.history.implement.ChattingMessageHistoryQuery;
import coffeemeet.server.chatting.history.implement.ChattingRoomHistoryQuery;
import coffeemeet.server.chatting.history.implement.UserChattingHistoryQuery;
import coffeemeet.server.chatting.history.service.dto.ChattingMessageHistoryDto;
import coffeemeet.server.chatting.history.service.dto.ChattingRoomHistoryDto;
import coffeemeet.server.user.domain.User;
import coffeemeet.server.user.implement.UserQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingRoomHistoryService {

  private final UserChattingHistoryQuery userChattingHistoryQuery;
  private final ChattingRoomHistoryQuery chattingRoomHistoryQuery;
  private final ChattingMessageHistoryQuery chattingMessageHistoryQuery;
  private final UserQuery userQuery;

  // TODO: 11/20/23 쿼리 로직 수정
  public List<ChattingRoomHistoryDto.Response> searchChattingRoomHistories(Long userId) {
    User user = userQuery.getUserById(userId);
    List<UserChattingHistory> userChattingHistories = userChattingHistoryQuery.getUserChattingHistoriesBy(
        user);
    List<ChattingRoomHistory> chattingRoomHistories = userChattingHistories.stream()
        .map(UserChattingHistory::getChattingRoomHistory)
        .toList();
    return chattingRoomHistories.stream()
        .map(chattingRoomHistory -> {
          List<User> users = userChattingHistoryQuery.getUserChattingHistoriesBy(
                  chattingRoomHistory)
              .stream()
              .map(UserChattingHistory::getUser)
              .toList();
          return ChattingRoomHistoryDto.Response.of(users, chattingRoomHistory);
        })
        .toList();
  }

  // TODO: 11/20/23 캐쉬 로직 적용
  public List<ChattingMessageHistoryDto.Response> searchChattingMessageHistories(Long roomHistoryId,
      Long firstMessageId, int pageSize) {
    ChattingRoomHistory chattingRoomHistory = chattingRoomHistoryQuery.getChattingRoomHistoryBy(
        roomHistoryId);
    return chattingMessageHistoryQuery.getMessageHistories(chattingRoomHistory, firstMessageId,
            pageSize)
        .stream()
        .map(chattingMessageHistory -> ChattingMessageHistoryDto.Response.of(
            chattingMessageHistory.getUser(), chattingMessageHistory))
        .toList();
  }

}
