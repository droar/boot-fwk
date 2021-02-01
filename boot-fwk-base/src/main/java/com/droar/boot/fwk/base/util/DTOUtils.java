package com.droar.boot.fwk.base.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

/**
 * The Class DTOUtils.
 * 
 * @author droar
 *
 */
public class DTOUtils {

  /**
   * Convert to dto.
   *
   * @param obj the obj
   * @param mapper the mapper
   * @return the list
   */
  public static <E, T> List<T> convertEntityToDtoList(List<E> lstEntity, Type mapper) {
    return new ModelMapper().map(lstEntity, mapper);
  }

  /**
   * Convert to entity.
   *
   * @param obj the obj
   * @param mapper the mapper
   * @return the list
   */
  public static <E, T> List<E> convertDtoToEntityList(List<T> lstDTO, Type mapper) {
    return new ModelMapper().map(lstDTO, mapper);
  }

  /**
   * Convert entity to dto.
   *
   * @param <E> the element type
   * @param <T> the generic type
   * @param entity the entity
   * @param mapper the mapper
   * @return the t
   */
  public static <E, T> T convertEntityToDto(E entity, Type mapper) {
    return new ModelMapper().map(entity, mapper);
  }


  /**
   * Convert dto to entity.
   *
   * @param <E> the element type
   * @param <T> the generic type
   * @param DTO the dto
   * @param mapper the mapper
   * @return the e
   */
  public static <E, T> E convertDtoToEntity(T DTO, Type mapper) {
    return new ModelMapper().map(DTO, mapper);
  }

  /**
   * Map entity list to dto custom.
   *
   * @param <E> the element type
   * @param lstEntity the lst entity
   * @param dtoClass the dto class
   * @param propertyMap the property map
   * @return the list
   */
  public static <E, T> List<T> mapEntityListToDtoCustom(List<E> lstEntity, Class<T> dtoClass,
      PropertyMap<?, ?> propertyMap) {
    List<T> lstDTO = new ArrayList<T>();

    if (propertyMap != null && dtoClass != null && CollectionUtils.isNotEmpty(lstEntity)) {
      ModelMapper modelMapper = new ModelMapper();

      // We ignore the ambiguous columns
      modelMapper.getConfiguration().setAmbiguityIgnored(Boolean.TRUE);
      modelMapper.addMappings(propertyMap);

      lstDTO = lstEntity.stream().map(item -> modelMapper.map(item, dtoClass))
          .collect(Collectors.toList());
    }

    return lstDTO;
  }

  /**
   * Map dto list to entity custom.
   *
   * @param <T> the generic type
   * @param lstDto the lst dto
   * @param entityClass the entity class
   * @param propertyMap the property map
   * @return the list
   */
  public static <T, E> List<?> mapDtoListToEntityCustom(List<T> lstDto, Class<E> entityClass,
      PropertyMap<?, ?> propertyMap) {
    List<E> lstEntity = new ArrayList<E>();

    if (propertyMap != null && entityClass != null && CollectionUtils.isNotEmpty(lstDto)) {
      ModelMapper modelMapper = new ModelMapper();

      // We ignore the ambiguous columns
      modelMapper.getConfiguration().setAmbiguityIgnored(Boolean.TRUE);

      modelMapper.addMappings(propertyMap);
      lstEntity = lstDto.stream().map(item -> modelMapper.map(item, entityClass))
          .collect(Collectors.toList());
    }

    return lstEntity;
  }

  /**
   * Map entity to dto custom.
   *
   * @param <E> the element type
   * @param <T> the generic type
   * @param entity the entity
   * @param dtoClass the dto class
   * @param propertyMap the property map
   * @return the t
   */
  public static <E, T> T mapEntityToDtoCustom(E entity, Class<T> dtoClass,
      PropertyMap<?, ?> propertyMap) {
    T entityDTO = null;

    if (propertyMap != null && dtoClass != null && entity != null) {
      ModelMapper modelMapper = new ModelMapper();

      // We ignore the ambiguous columns
      modelMapper.getConfiguration().setAmbiguityIgnored(Boolean.TRUE);

      modelMapper.addMappings(propertyMap);
      entityDTO = modelMapper.map(entity, dtoClass);
    }

    return entityDTO;
  }
}
