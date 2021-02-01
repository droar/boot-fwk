package com.droar.boot.fwk.base.exception;

import java.util.Date;

public class ExceptionDetails {

  private int status;
  private Date timestamp;
  private String message;
  private String details;

  public ExceptionDetails(int status, Date timestamp, String message, String details) {
    super();
    this.status = status;
    this.timestamp = timestamp;
    this.message = message;
    this.details = details;
  }

  public ExceptionDetails(Date timestamp, String message, String details) {
    super();
    this.timestamp = timestamp;
    this.message = message;
    this.details = details;
  }

  public ExceptionDetails(int status, Date timestamp, String message) {
    super();
    this.status = status;
    this.timestamp = timestamp;
    this.message = message;
    this.details = "Detalles no disponibles";
  }

  public ExceptionDetails(Date timestamp, String message) {
    super();
    this.timestamp = timestamp;
    this.message = message;
    this.details = "Detalles no disponibles";
  }

  public int getStatus() {
    return status;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public String getMessage() {
    return message;
  }

  public String getDetails() {
    return details;
  }

}
