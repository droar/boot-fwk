package com.droar.boot.fwk.base.entity;

import java.util.Date;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import com.droar.boot.fwk.base.config.AbstractRevisionListener;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * Instantiates a new rev info (if needed auditory data)
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "revinfo")
@Entity
@RevisionEntity(AbstractRevisionListener.class)
public class RevInfo {

  /** The id. */
  @Id
  @Access(value = AccessType.PROPERTY)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, unique = true, name = "id")
  @RevisionNumber
  private int id;

  /** The descripcion. */
  @RevisionTimestamp
  private Date revtstmp;

}


