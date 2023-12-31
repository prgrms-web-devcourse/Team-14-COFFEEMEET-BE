package coffeemeet.server.common.presentation.advice;

import coffeemeet.server.common.execption.BadRequestException;
import coffeemeet.server.common.execption.DataLengthExceededException;
import coffeemeet.server.common.execption.ForbiddenException;
import coffeemeet.server.common.execption.GlobalErrorCode;
import coffeemeet.server.common.execption.InvalidAuthException;
import coffeemeet.server.common.execption.InvalidInputException;
import coffeemeet.server.common.execption.MissMatchException;
import coffeemeet.server.common.execption.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleException(RuntimeException exception) {
    log.info(exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ErrorResponse.of(GlobalErrorCode.INTERNAL_SERVER_ERROR));
  }

  @ExceptionHandler(DataLengthExceededException.class)
  public ResponseEntity<ErrorResponse> handleException(DataLengthExceededException exception) {
    log.info(exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
        .body(ErrorResponse.of(exception.getErrorCode()));
  }

  @ExceptionHandler(InvalidAuthException.class)
  public ResponseEntity<ErrorResponse> handleException(InvalidAuthException exception) {
    log.info(exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(ErrorResponse.of(exception.getErrorCode()));
  }

  @ExceptionHandler(InvalidInputException.class)
  public ResponseEntity<ErrorResponse> handleException(InvalidInputException exception) {
    log.info(exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ErrorResponse.of(exception.getErrorCode()));
  }

  @ExceptionHandler(MissMatchException.class)
  public ResponseEntity<ErrorResponse> handleException(MissMatchException exception) {
    log.info(exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponse.of(exception.getErrorCode()));
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleException(BadRequestException exception) {
    log.info(exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponse.of(exception.getErrorCode()));
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleException(NotFoundException exception) {
    log.info(exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ErrorResponse.of(exception.getErrorCode()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException exception) {
    log.info(exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponse.of(GlobalErrorCode.VALIDATION_ERROR, exception.getBindingResult()));
  }

  @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleException(
      UnsatisfiedServletRequestParameterException exception) {
    log.info(exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponse.of(GlobalErrorCode.VALIDATION_ERROR));
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleException(
      MethodArgumentTypeMismatchException exception) {
    log.info(exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponse.of(exception));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException exception) {
    log.info(exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponse.of(GlobalErrorCode.VALIDATION_ERROR));
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleException(
      MissingServletRequestParameterException exception) {
    log.info(exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponse.of(GlobalErrorCode.PARAMETER_INPUT_MISSING));
  }

  @ExceptionHandler(MultipartException.class)
  public ResponseEntity<ErrorResponse> handleException(MultipartException exception) {
    log.info(exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
        .body(ErrorResponse.of(GlobalErrorCode.PAYLOAD_TOO_LARGE));
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ErrorResponse> handleException(ForbiddenException exception) {
    log.info(exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(ErrorResponse.of(exception.getErrorCode()));
  }

}
