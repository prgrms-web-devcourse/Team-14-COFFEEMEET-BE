package coffeemeet.server.common.execption.exception;

import lombok.Getter;

@Getter
public class DataLengthExceededException extends CoffeeMeetException {

  private final ErrorCode errorCode;

  public DataLengthExceededException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

}
