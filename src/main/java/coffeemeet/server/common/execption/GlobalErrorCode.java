package coffeemeet.server.common.execption;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {

  VALIDATION_ERROR("G000", "유효하지 않은 입력입니다."),
  BAD_REQUEST_ERROR("G000", "잘못된 요청입니다."),
  PARAMETER_INPUT_MISSING("G000", "입력 파라미터가 없습니다."),
  PAYLOAD_TOO_LARGE("G000", "요청 데이터가 6MB를 초과했습니다."),
  INVALID_FCM_TOKEN("G004", "유효하지 않은 FCM토큰입니다."),
  STOMP_ACCESSOR_NOT_FOUND("G005", "웹소켓 연결을 할 수 없습니다."),
  INTERNAL_SERVER_ERROR("G050", "예상치 못한 오류입니다."),
  PUSH_NOTIFICATION_SEND_FAILURE("G050", "푸시 알림 전송에 실패했습니다."),
  ;

  private final String code;
  private final String message;

  @Override
  public String code() {
    return this.code;
  }

  @Override
  public String message() {
    return this.message;
  }

}
