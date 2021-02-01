package com.droar.boot.fwk.base.exception;

public class ErrorDetailList {

  private ErrorDetails[] listaErrores;

  public ErrorDetailList(ErrorDetails[] listaErrores) {
    this.listaErrores = listaErrores;
  }

  public ErrorDetails[] getListaErrores() {
    return listaErrores;
  }


}
