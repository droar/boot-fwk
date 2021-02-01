package com.droar.boot.fwk.base.service;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import com.droar.boot.fwk.base.entity.AbstractEntity;

/**
 * The Interface Generic repository.
 *
 * @author droar
 * @param <E> the element type
 * @param <ID> the generic type
 */
@NoRepositoryBean
public interface GenericRepository<E extends AbstractEntity, ID extends Serializable>
    extends CrudRepository<E, ID> {

  /**
   * Save.
   *
   * @param <S> the generic type
   * @param entity the entity
   * @return the s
   */
  public <S extends E> S save(S entity);

  /**
   * Save.
   *
   * @param <S> the generic type
   * @param entities the entities
   * @return the list
   */
  public <S extends E> List<S> save(Iterable<S> entities);

  /**
   * Save and flush.
   *
   * @param <S> the generic type
   * @param entity the entity
   * @return the s
   */
  public <S extends E> S saveAndFlush(S entity);

  /**
   * Save and flush.
   *
   * @param <S> the generic type
   * @param entities the entities
   * @return the list
   */
  public <S extends E> List<S> saveAndFlush(Iterable<S> entities);

  /**
   * Refresh.
   *
   * @param e the e
   */
  public <S extends E> void refresh(S entity);

  /**
   * Refresh.
   *
   * @param e the entities
   */
  public <S extends E> void refresh(Iterable<S> entities);

  /**
   * Clear.
   */
  public void clear();
}
