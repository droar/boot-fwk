/**
 * 
 */
package com.droar.boot.fwk.base.config;

import javax.servlet.http.HttpServletRequest;
import org.hibernate.envers.RevisionListener;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * The listener interface for receiving Revision events. The class that is interested in
 * processing a abstractRevision event implements this interface, and the object created with that class
 * is registered with a component using the component's <code>addabstractRevisionListener<code> method.
 * When the abstractRevision event occurs, that object's appropriate method is invoked.
 * 
 * @author droar
 *
 */
@Component
public class AbstractRevisionListener implements RevisionListener, ApplicationContextAware {

  /** The Constant TOKEN. */
  public static final String TOKEN = "token";

  /**
   * New revision.
   *
   * @param revisionEntity the revision entity
   */
  @Override
  public void newRevision(Object revisionEntity) {

    @SuppressWarnings("unused")
    final HttpServletRequest currentRequest =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    // TODO: Set here to save the audit data.

  }

  /**
   * Sets the application context.
   *
   * @param applicationContext the new application context
   * @throws BeansException the beans exception
   */
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

  }

}
