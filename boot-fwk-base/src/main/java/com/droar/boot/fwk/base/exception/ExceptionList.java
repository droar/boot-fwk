package com.droar.boot.fwk.base.exception;

import java.util.List;
import lombok.Data;

@Data
public class ExceptionList {

  /** The lista errores. */
  List<ExceptionResponse> listaErrores;
}
