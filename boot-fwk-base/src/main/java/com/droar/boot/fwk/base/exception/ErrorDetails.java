package com.droar.boot.fwk.base.exception;

public class ErrorDetails {

  private int codigo;
  private String mensaje;

  public ErrorDetails(int codigo, String mensaje) {
    this.codigo = codigo;
    this.mensaje = mensaje;
  }

  public int getCodigo() {
    return codigo;
  }

  public String getMensaje() {
    return mensaje;
  }

}
