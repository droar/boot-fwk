package com.droar.boot.fwk.base.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

/**
 * If we want to use the audit fields (common ones, feel free to change).
 * 
 * @author droar
 */

@Embeddable
@Data
public class AbstractAudit implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1535560031849741652L;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "insert_date")
  private Date insertDate = new Date();

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "update_date")
  private Date updateDate;

  @Column(name = "version")
  private Integer version = 0;

  @Column(name = "deleted")
  private Integer deleted = 0;

  @Column(name = "owner")
  private String owner = "owner_of_the_tables";

}
