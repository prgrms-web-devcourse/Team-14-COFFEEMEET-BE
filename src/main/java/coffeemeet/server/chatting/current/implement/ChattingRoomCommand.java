package coffeemeet.server.chatting.current.implement;

import coffeemeet.server.chatting.current.domain.ChattingRoom;
import coffeemeet.server.chatting.current.infrastructure.ChattingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ChattingRoomCommand {

  private final ChattingRoomRepository chattingRoomRepository;

  public ChattingRoom createChattingRoom() {
    return chattingRoomRepository.save(new ChattingRoom());
  }

  public void removeChattingRoom(Long roomId) {
    chattingRoomRepository.deleteById(roomId);
  }

}
