package com.droar.boot.fwk.base.exception;

import lombok.Data;


/**
 * The Class ExceptionResponse.
 */
@Data
public class ExceptionResponse {

  /** The codigo. */
  private int codigo;

  /** The mensaje. */
  private String mensaje;

  /**
   * Instantiates a new exception response.
   *
   * @param codigo the codigo
   * @param mensaje the mensaje
   */
  public ExceptionResponse(int codigo, String mensaje) {
    super();
    this.codigo = codigo;
    this.mensaje = mensaje;
  }

  /**
   * Instantiates a new exception response.
   */
  public ExceptionResponse() {}

}
