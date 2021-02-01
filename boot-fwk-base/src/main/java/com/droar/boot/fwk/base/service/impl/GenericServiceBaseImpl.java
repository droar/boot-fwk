package com.droar.boot.fwk.base.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.droar.boot.fwk.base.entity.AbstractEntity;
import com.droar.boot.fwk.base.service.GenericRepository;
import com.droar.boot.fwk.base.service.GenericServiceBase;
import lombok.extern.slf4j.Slf4j;

/**
 * The GenericServiceBaseImpl
 *
 * @author droar
 * @param <E> the element type
 */

/**
 * Instantiates a new generic service base.
 */
@Slf4j
public class GenericServiceBaseImpl<E extends AbstractEntity, ID extends Serializable, REPO extends GenericRepository<E, ID>>
    implements GenericServiceBase<E, ID, REPO> {

  /** The Constant VALIDATION_NOT_NULL. */
  private static final String VALIDATION_NOT_NULL =
      "Null entity not valid: ";

  /** The injection to the generic repository */
  @Autowired
  private GenericRepository<E, ID> repo;

  @Override
  @Transactional
  public E createOrUpdate(E entity) {
    Validate.notNull(entity, VALIDATION_NOT_NULL + this.repo.getClass().getName());

    // Save and return the entity
    return this.getRepo().saveAndFlush(entity);
  }

  @Override
  @Transactional
  public List<E> createOrUpdateAll(Iterable<E> entities) {
    Validate.notNull(entities, VALIDATION_NOT_NULL + this.repo.getClass().getName());

    // Save and return the entities
    return (List<E>) this.getRepo().saveAndFlush(entities);
  }

  @Override
  @Transactional(readOnly = true)
  public E findById(ID id) {
    Validate.notNull(id, VALIDATION_NOT_NULL + this.repo.getClass().getName());
    E result = null;

    // We search the object
    Optional<E> optResult = this.getRepo().findById(id);

    try {
      // We recover it
      result = optResult.get();
    } catch (NoSuchElementException e) {
      log.error("No existe el objeto con id " + id);
      result = null;
    }

    return result;
  }

  @Override
  @Transactional(readOnly = true)
  public List<E> findAll() {
    return (List<E>) this.getRepo().findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public List<E> findAll(List<ID> lstIds) {
    return (List<E>) this.getRepo().findAllById(lstIds);
  }

  @Override
  @Transactional
  public void delete(E entity) {
    Validate.notNull(entity, VALIDATION_NOT_NULL + this.repo.getClass().getName());

    this.getRepo().delete(entity);
  }

  @Override
  @Transactional
  public void deleteAll(Iterable<E> entities) {
    Validate.notNull(entities, VALIDATION_NOT_NULL + this.repo.getClass().getName());

    this.getRepo().deleteAll(entities);
  }

  @Override
  @Transactional
  public void deleteById(ID id) {
    Validate.notNull(id, VALIDATION_NOT_NULL + this.repo.getClass().getName());

    this.getRepo().deleteById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public long count() {
    return this.getRepo().count();
  }

  /**
   * @return the this.repo
   */
  @SuppressWarnings("unchecked")
  public REPO getRepo() {
    return (REPO) this.repo;
  }

  /**
   * @param this.repo the this.repo to set
   */
  public void setRepo(REPO repo) {
    this.repo = (GenericRepository<E, ID>) repo;
  }
}
