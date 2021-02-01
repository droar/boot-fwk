package com.droar.boot.fwk.base.service;

import java.io.Serializable;
import java.util.List;
import com.droar.boot.fwk.base.entity.AbstractEntity;

/**
 * The GenericServiceBase.
 *
 * @author droar
 * @param <E> the generic type
 * @param <ID> the generic type
 * @param <REPO> the generic type
 */
public interface GenericServiceBase<E extends AbstractEntity, ID extends Serializable, REPO extends GenericRepository<E, ID>> {

  /**
   * Creates the or update.
   *
   * @param entity the entity
   * @return the e
   */
  public E createOrUpdate(E entity);

  /**
   * Creates the or update all.
   *
   * @param entities the entities
   * @return the list
   */
  public List<E> createOrUpdateAll(Iterable<E> entities);

  /**
   * Find by id.
   *
   * @param id the id
   * @return the e
   */
  public E findById(ID id);

  /**
   * Find all.
   *
   * @param pageNumber the page number
   * @param pageSize the page size
   * @return the list
   */
  public List<E> findAll(List<ID> lstIds);

  /**
   * Find all.
   *
   * @return the list
   */
  public List<E> findAll();

  /**
   * Delete.
   *
   * @param entity the entity
   */
  public void delete(E entity);

  /**
   * Delete by id.
   *
   * @param id the id
   */
  public void deleteById(ID id);

  /**
   * Delete all.
   *
   * @param entities the entities
   */
  public void deleteAll(Iterable<E> entities);

  /**
   * Count.
   *
   * @return the long
   */
  public long count();

}
