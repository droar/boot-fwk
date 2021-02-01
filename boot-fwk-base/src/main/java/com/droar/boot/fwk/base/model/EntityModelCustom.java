/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.droar.boot.fwk.base.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.util.Assert;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * General helper to easily create a wrapper for a collection of entities.
 * 
 * @author Oliver Gierke
 */
@XmlRootElement(name = "entities")
public class EntityModelCustom<T> implements Iterable<T> {

  private final Collection<T> items;

  /**
   * Creates an empty {@link Resources} instance.
   */
  protected EntityModelCustom() {
    this(new ArrayList<T>());
  }

  /**
   * Creates a {@link Resources} instance with the given content and {@link Link}s (optional).
   * 
   * @param content must not be {@literal null}.
   * @param links the links to be added to the {@link Resources}.
   */
  public EntityModelCustom(Iterable<T> content, Link... links) {
    this(content, Arrays.asList(links));
  }

  /**
   * Creates a {@link Resources} instance with the given content and {@link Link}s.
   * 
   * @param content must not be {@literal null}.
   * @param links the links to be added to the {@link Resources}.
   */
  public EntityModelCustom(Iterable<T> content, Iterable<Link> links) {
    Assert.notNull(content, "Contenido nulo");

    this.items = new ArrayList<T>();

    for (T element : content) {
      this.items.add(element);
    }
  }

  /**
   * Creates a new {@link Resources} instance by wrapping the given domain class instances into a
   * {@link Resource}.
   * 
   * @param content must not be {@literal null}.
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T extends EntityModel<S>, S> EntityModelCustom<T> wrap(Iterable<S> content) {
    Assert.notNull(content, "Contenido nulo");

    ArrayList<T> resources = new ArrayList<T>();

    for (S element : content) {
      resources.add((T) EntityModel.of(element));
    }

    return new EntityModelCustom<T>(resources);
  }

  /**
   * Returns the underlying elements.
   * 
   * @return the content will never be {@literal null}.
   */
  @XmlAnyElement
  @XmlElementWrapper
  @JsonProperty("items")
  public Collection<T> getContent() {
    return Collections.unmodifiableCollection(items);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Iterable#iterator()
   */
  @Override
  public Iterator<T> iterator() {
    return items.iterator();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.hateoas.ResourceSupport#toString()
   */
  @Override
  public String toString() {
    return String.format("Resources { content: %s, %s }", getContent(), super.toString());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.hateoas.ResourceSupport#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {

    if (obj == this) {
      return true;
    }

    if (obj == null || !obj.getClass().equals(getClass())) {
      return false;
    }

    EntityModelCustom<?> that = (EntityModelCustom<?>) obj;

    boolean contentEqual = this.items == null ? that.items == null : this.items.equals(that.items);
    return contentEqual ? super.equals(obj) : false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.hateoas.ResourceSupport#hashCode()
   */
  @Override
  public int hashCode() {

    int result = super.hashCode();
    result += items == null ? 0 : 17 * items.hashCode();

    return result;
  }
}
