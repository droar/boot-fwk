package com.droar.boot.fwk.base.exception;

import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * The Class CustomizedResponseEntityExceptionHandler.
 * 
 * @author droar
 *
 */
@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler {

  /** The Constant BAD_REQUEST. */
  public static final int BAD_REQUEST = 1;

  /**
   * Handle not found exception.
   *
   * @param ex the ex
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public final ResponseEntity<ExceptionList> handleNotFoundException(Exception ex,
      WebRequest request) {

    ExceptionResponse exceptionResponse = new ExceptionResponse(BAD_REQUEST, ex.getMessage());

    List<ExceptionResponse> elr = new ArrayList<ExceptionResponse>();
    elr.add(exceptionResponse);
    ExceptionList el = new ExceptionList();
    el.setListaErrores(elr);
    return new ResponseEntity<ExceptionList>(el, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidDataAccessApiUsageException.class)
  public final ResponseEntity<ExceptionList> handleInvalidDataException(Exception ex,
      WebRequest request) {

    ExceptionResponse exceptionResponse = new ExceptionResponse(BAD_REQUEST, ex.getMessage());

    List<ExceptionResponse> elr = new ArrayList<ExceptionResponse>();
    elr.add(exceptionResponse);
    ExceptionList el = new ExceptionList();
    el.setListaErrores(elr);
    return new ResponseEntity<ExceptionList>(el, HttpStatus.BAD_REQUEST);
  }

}
