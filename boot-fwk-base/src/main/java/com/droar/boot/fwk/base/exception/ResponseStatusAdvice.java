package com.droar.boot.fwk.base.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ResponseStatusAdvice {

  @ResponseBody
  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<?> responseStatusHandler(ResponseStatusException ex, WebRequest request) {

    // Standard REST exception response
    // ExceptionDetails exceptionDetails = new ExceptionDetails(ex.getStatus().value(), new Date(),
    // ex.getReason(), request.getDescription(false));
    // return new ResponseEntity<>(exceptionDetails, ex.getStatus());

    ErrorDetailList errorDetailList =
        new ErrorDetailList(new ErrorDetails[] {new ErrorDetails(ex.getCode(), ex.getReason())});
    return new ResponseEntity<>(errorDetailList, ex.getStatus());
  }

  // TODO: Uncomment this if you want customized server errors TODO

  // @ResponseBody
  // @ExceptionHandler(Exception.class)
  // public ResponseEntity<?> internalServerErrorHandler(Exception ex, WebRequest request) {
  //
  // 
  // if (ex instanceof NullPointerException) {
  // return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  // }
  // ErrorDetailList errorDetailList = new ErrorDetailList(new ErrorDetails[] {new ErrorDetails(4,
  // ex.getMessage())});
  // return new ResponseEntity<>(errorDetailList, HttpStatus.INTERNAL_SERVER_ERROR);
  // }

}
