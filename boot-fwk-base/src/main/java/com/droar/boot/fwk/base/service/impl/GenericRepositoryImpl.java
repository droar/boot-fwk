package com.droar.boot.fwk.base.service.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import com.droar.boot.fwk.base.entity.AbstractAudit;
import com.droar.boot.fwk.base.entity.AbstractEntity;
import com.droar.boot.fwk.base.service.GenericRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * The Interface Generic repository impl.
 *
 * @author droar
 * @param <E> the element type
 * @param <ID> the generic type
 */
@Slf4j
public class GenericRepositoryImpl<E extends AbstractEntity, ID extends Serializable>
    extends SimpleJpaRepository<E, ID> implements GenericRepository<E, ID> {

  /** The Constant VALIDATION_NOT_NULL. */
  private static final String VALIDATION_NOT_NULL =
      "Null entity not valid: ";

  /** The entity manager. */
  private final EntityManager entityManager;

  /** The entity information. */
  private final JpaEntityInformation<E, ID> entityInformation;

  /**
   * Instantiates a new generic repository impl.
   *
   * @param entityInformation the entity information
   * @param entityManager the entity manager
   */
  public GenericRepositoryImpl(JpaEntityInformation<E, ID> entityInformation,
      EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityManager = entityManager;
    this.entityInformation = entityInformation;
  }

  @Override
  @Transactional
  public <S extends E> S save(S entity) {
    Validate.notNull(entity, VALIDATION_NOT_NULL + this.getClass().getName());

    if (this.entityInformation.isNew(entity)) {
      // We generate the audit entity if its new entity
      this.createAuditEntity(entity);

      this.entityManager.persist(entity);

      return entity;
    } else {
      // We update the audit entity
      this.updateAuditEntity(entity);

      // Update entity
      S updatedEntity = this.entityManager.merge(entity);

      return updatedEntity;
    }
  }

  @Override
  @Transactional
  public <S extends E> List<S> save(Iterable<S> entities) {
    Validate.notNull(entities, VALIDATION_NOT_NULL + this.getClass().getName());
    List<S> result = new ArrayList<S>();

    for (S entity : entities) {
      result.add(this.save(entity));
    }

    return result;
  }

  @Override
  @Transactional
  public <S extends E> S saveAndFlush(S entity) {

    S result = this.save(entity);

    // Flushing
    this.entityManager.flush();

    // We refresh the entity
    this.refresh(result);

    return result;
  }

  @Override
  @Transactional
  public <S extends E> List<S> saveAndFlush(Iterable<S> entities) {

    Iterable<S> result = this.save(entities);

    // Flushing
    this.entityManager.flush();

    // We refresh the entity
    this.refresh(result);

    return (List<S>) result;
  }

  /**
   * Refresh.
   *
   * @param t the t
   */
  @Override
  @Transactional
  public <S extends E> void refresh(S entity) {
    this.entityManager.refresh(entity);
  }

  /**
   * Refresh.
   *
   * @param t the t
   */
  @Override
  @Transactional
  public <S extends E> void refresh(Iterable<S> entities) {
    if (IterableUtils.isEmpty(entities)) {
      for (E entity : entities) {
        this.entityManager.refresh(entity);
      }
    }
  }

  /**
   * Refresh.
   *
   * @param t the t
   */
  @Override
  @Transactional
  public void clear() {
    this.entityManager.clear();
  }

  /**
   * Creates the core audit entity.
   *
   * @param entity the entity
   * @return the audit entity
   */
  private void createAuditEntity(E entity) {
    try {
      if (entity != null) {
        Field auditField = this.getAuditField(entity);

        if (auditField != null) {
          AbstractAudit auditEntity = (AbstractAudit) auditField.get(entity);

          // We init the audit entity if we need to.
          if (auditEntity == null) {
            auditField.set(entity, new AbstractAudit());
          }
        }
      }
    } catch (Exception e) {
      log.error("Error when trying to obtain entity" + e.getMessage());
    }
  }

  /**
   * Updates the audit entity.
   *
   * @param entity the entity
   * @return the audit entity
   */
  private <S extends E> void updateAuditEntity(E entity) {
    try {
      if (entity != null) {
        Field auditField = this.getAuditField(entity);

        if (auditField != null) {
          AbstractAudit auditEntity = (AbstractAudit) auditField.get(entity);

          // We init the audit entity if we need to.
          if (auditEntity != null) {
            auditEntity.setUpdateDate(new Date());
            auditEntity.setVersion(
                auditEntity.getVersion() != null ? auditEntity.getVersion() + 1 : auditEntity.getVersion());
          }
        }
      }
    } catch (Exception e) {
      log.error("Error when trying to obtain entity" + e.getMessage());
    }
  }

  /**
   * Gets the audit field.
   *
   * @param entity the entity
   * @return the audit field
   */
  private Field getAuditField(E entity) {
    Field auditField = null;

    if (entity != null) {
      // If we have the audit field on our entity, we'll return it so we can work on it.
      if (ArrayUtils.isNotEmpty(entity.getClass().getDeclaredFields())) {
        for (Field field : entity.getClass().getDeclaredFields()) {
          if (field.getType().equals(AbstractAudit.class)) {
            field.setAccessible(Boolean.TRUE);
            auditField = field;
          }
        }
      }
    }

    return auditField;
  }
}
