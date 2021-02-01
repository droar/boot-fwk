package com.droar.boot.fwk.base.entity;

import java.io.Serializable;
import javax.persistence.MappedSuperclass;

/**
 * 
 * Super clase sin definir ningÃºn atributo
 *
 * @author everis
 *
 */
@MappedSuperclass
public abstract class AbstractEntityEmpty implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1535560031849741652L;

}
