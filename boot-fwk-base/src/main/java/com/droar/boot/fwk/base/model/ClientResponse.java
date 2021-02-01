package com.droar.boot.fwk.base.model;

import com.droar.boot.fwk.base.exception.ResponseStatusException;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author droar
 *
 * @param <T>
 */
@EqualsAndHashCode
@Data
public class ClientResponse<T> {

  /** The exception. */
  private ResponseStatusException responseException;

  /** The response object. */
  private T responseObject;

}
