package com.droar.boot.fwk.base.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;


/**
 * The Class TypeUtil.
 *
 * @author droar
 *
 */
public abstract class TypeUtil {

  private TypeUtil() {}

  /**
   * Gets the generic class
   * 
   * @param <X> Tipo generico.
   * @param clazz Clase.
   * @param parameterIndex parameter index
   * @return Clase del tipo generico declarado.
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static <X> Class<X> getClassParameter(final Class clazz, final int parameterIndex) {
    Type[] theGenericClass = null;

    if (clazz.isInterface()) {
      theGenericClass = clazz.getGenericInterfaces();
    } else {
      theGenericClass = new Type[] {clazz.getGenericSuperclass()};
    }

    for (final Type t : theGenericClass) {
      if (t instanceof ParameterizedType) {
        final ParameterizedType thisType = (ParameterizedType) t;

        final Type[] actualTypeArguments = thisType.getActualTypeArguments();

        if (parameterIndex < actualTypeArguments.length) {
          final Type actualType = actualTypeArguments[parameterIndex];

          if (actualType instanceof Class) {
            return (Class<X>) actualType;
          } else if (actualType instanceof ParameterizedType) {
            return (Class<X>) ((ParameterizedType) actualType).getRawType();
          }
        }

        return null;
      }
    }

    return null;
  }

  /**
   * Gets the field value of a field.
   * 
   * @param <E> Tipo del campo.
   * @param clazz Clase donde está definido el campo.
   * @param instance Instancia de la que se quiere recuperar el valor del campo.
   * @param field Nombre del campo.
   * @return Valor del campo.
   * @see {@link Field#get(Object)}
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static <E> E getField(final Class clazz, final Object instance, final String field) {
    try {
      final Field translatorField = clazz.getDeclaredField(field);
      translatorField.setAccessible(true);
      return (E) translatorField.get(instance);
    } catch (final RuntimeException e) {
      throw e;
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Gets .class of a class
   * 
   * @param <X> Tipo de la clase.
   * @param clazz Clase.
   * @return Fichero .class de la clase.
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static <X> File getFileFromClass(final Class<X> clazz) throws IOException {

    final String filePattern =
        String.format("classpath:%s.class", clazz.getName().replace('.', '/'));

    final Resource[] resources =
        new PathMatchingResourcePatternResolver().getResources(filePattern);

    if (resources == null || resources.length == 0) {
      throw new FileNotFoundException(filePattern);
    }

    final File f = resources[0].getFile();

    return f;

  }

}
