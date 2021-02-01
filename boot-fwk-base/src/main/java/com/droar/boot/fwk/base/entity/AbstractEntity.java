package com.droar.boot.fwk.base.entity;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.UUID;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Transient;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

import lombok.extern.slf4j.Slf4j;

/**
 * Abstract entity class
 * 
 * @author droar
 *
 */
@Slf4j
public abstract class AbstractEntity extends AbstractEntityEmpty {

  /**
   * 
   */
  private static final long serialVersionUID = 8806872693938755613L;

  /** The Constant ERROR_NO_ANOTACION_ID. */
  private static final String ERROR_NO_ANOTACION_ID =
      "Hemos invocado al metodo AbstractEntity.{0}, pero la clase {1} no tiene ningun atributo anotado con @Id.";

  /** The id attribute. */
  private transient Field idAttribute = null;

  @Transient
  private Integer hashCodeValue = null;

  /**
   * Metodo que devuelve el valor del objeto anotado con @Id.
   *
   * @return the id
   */
  public Object getId() {

    // http://opensource.atlassian.com/projects/hibernate/browse/HHH-3718
    if (this instanceof HibernateProxy) {
      LazyInitializer lazyInitializer = ((HibernateProxy) this).getHibernateLazyInitializer();
      if (lazyInitializer.isUninitialized()) {
        return (Integer) lazyInitializer.getIdentifier();
      }
    }

    Object idValue = null;

    Class<?> clasePk = getClassId();

    // Si no hay ClassId , entonces es que solo hay una PK
    if (clasePk == null) {

      // Ahora tenemos que recuperar el atributo con la @Id
      try {
        Field campoId = obtenerCampoId();

        // Si hemos encontrado un atributo marcado con la anotacion @Id,
        // entonces obtenemos su valor.
        if (campoId != null) {
          idValue = FieldUtils.readField(campoId, this, true);
        } else {
          log.warn(
              MessageFormat.format(ERROR_NO_ANOTACION_ID, new Object[] {"getId", this.getClass()}));
        }
      } catch (IllegalAccessException e) {
        // Dejamos constancia del error en el log.
        log.error("Error al obtener el id mediante el metodo generico getId de AbstractEntity.", e);

        // Ahora pintamos el error por la consola para ver el error si
        // estamos depurando.
        e.printStackTrace();
      }

    } else {
      // Tenemos que crear una pk del tipo que queremos
      try {

        idValue = clasePk.getDeclaredConstructor().newInstance();
        PropertyUtils.copyProperties(idValue, this);

      } catch (Exception e) {
        log.error(
            "Error al obtener el id compuesto mediante el metodo generico setId de AbstractEntity.",
            e);
        e.printStackTrace();
      }

      // Ahora copiamos las propiedades del padre a la PK
    }

    return idValue;
  }

  /**
   * Metodo que devuelve el valor del objeto anotado con @Id en formato String (Para aplicar al
   * resto de objetos.
   *
   * @return the id
   */
  public String getStringId() {
    return (this.getId() instanceof Integer ? ((Integer) this.getId()).toString() : "");
  }


  /**
   * Gets the class id.
   *
   * @return the class id
   */
  private Class<?> getClassId() {
    Class<? extends AbstractEntity> clase = this.getClass();
    Class<?> value = null;

    if (clase.isAnnotationPresent(IdClass.class)) {
      IdClass annotation = clase.getAnnotation(IdClass.class);
      value = annotation.value();
    }

    return value;
  }

  /**
   * Metodo que devolvera el campo que esta anotado con @Id.
   *
   * @return the field
   */
  @SuppressWarnings("rawtypes")
  private Field obtenerCampoId() {

    // Si ya hemos analizado la clase para saber el atributo que esta
    // anotado con @Id, no la volvemos a analizar
    Class<? extends AbstractEntity> estaClase = this.getClass();

    if (idAttribute == null) {
      int i = 0;

      Field[] declaredFields = estaClase.getDeclaredFields();
      while (i < declaredFields.length) {
        Field field = declaredFields[i];

        if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(EmbeddedId.class)) {
          idAttribute = field;
          break; // solo queremos el primero (y unico)
        }

        i++;
      }

      // En caso de no haber encontrado en la misma clase , intento buscar
      // hacia arriba
      if (idAttribute == null) {
        Class padre = estaClase.getSuperclass();
        while (padre != null) {
          i = 0;
          Field[] parentFields = padre.getDeclaredFields();
          while (i < parentFields.length) {
            Field field = parentFields[i];

            if (field.isAnnotationPresent(Id.class)
                || field.isAnnotationPresent(EmbeddedId.class)) {
              idAttribute = field;
              break; // solo queremos el primero (y unico)
            }
            i++;
          }

          if (idAttribute != null)
            break;
          padre = padre.getSuperclass();
        }

      }
    }

    // Para obtener el id debemos de recorrer todos los atributos de la
    // clase y quedarnos con aquel que tenga la anotacion @Id de JPA
    return idAttribute;
  }

  /**
   * Sets the id.
   *
   * @param id the new id
   */
  @SuppressWarnings("rawtypes")
  public void setId(Object id) {
    Class clasePk = getClassId();

    if (clasePk == null) {

      try {
        Field campoId = obtenerCampoId();

        // Si hemos encontrado un atributo marcado con la anotacion @Id,
        // entonces
        // obtenemos su valor.
        if (campoId != null) {
          if (id == null && campoId.getType().isPrimitive()) {
            FieldUtils.writeField(campoId, this, 0, true);
          } else {
            FieldUtils.writeField(campoId, this, id, true);
          }
        } else {
          log.warn(
              MessageFormat.format(ERROR_NO_ANOTACION_ID, new Object[] {"setId", this.getClass()}));
        }
      } catch (IllegalAccessException e) {
        // Dejamos constancia del error en el log.
        log.error("Error al establecer el id mediante el metodo generico setId de AbstractEntity.",
            e);

        // Ahora pintamos el error por la consola para ver el error si
        // estamos
        // depurando.
        e.printStackTrace();
      }
    } else {

      // La clave es compuesta, copiamos las propiedades
      try {
        PropertyUtils.copyProperties(id, this);
      } catch (Exception e) {
        log.error(
            "Error al establecer el id compuesto mediante el metodo generico setId de AbstractEntity.",
            e);
        e.printStackTrace();
      }

    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {

    if (!(obj instanceof AbstractEntity)) {
      return false;
    } else if (this.getId() == null && ((AbstractEntity) obj).getId() == null) {
      return obj == this;
    } else if (this.getClass().isAssignableFrom(obj.getClass())
        || obj.getClass().isAssignableFrom(this.getClass())
            && ((this.obtenerCampoId().equals(((AbstractEntity) obj).obtenerCampoId())))) {
      return (this.getId() != null && ((AbstractEntity) obj).getId() != null
          && this.getId().equals(((AbstractEntity) obj).getId()));
    } else {
      return false;
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    // If we dont have the hashCode we get it.
    if (hashCodeValue == null) {
      Object id = this.getId();
      Object objToCalculateHashCode = id;
      if (id == null) {
        objToCalculateHashCode = UUID.randomUUID();
      }

      hashCodeValue = new HashCodeBuilder().append(objToCalculateHashCode).toHashCode();
    }
    return hashCodeValue;
  }
}
