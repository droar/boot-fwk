package com.droar.boot.fwk.base.config;

import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class AuditAspect.
 */
@Aspect
@Component
@Slf4j
public class AuditAspect {

  /** The Constant PARAM_TOKEN. */
  public static final String PARAM_TOKEN = "token";

  /** The Constant PKG_CONTROLLER. */
  public static final String PKG_CONTROLLER = "com.droar.restex_svc.controller";

  /**
   * Before advice.
   *
   * @param jp the jp
   */
  @Before(value = "execution(public * " + PKG_CONTROLLER + ".*.post*(..)) || "
      + "execution(public * " + PKG_CONTROLLER + ".*.patch*(..)) || " + "execution(public * "
      + PKG_CONTROLLER + ".*.put*(..))"

  )
  public void beforeAdvice(JoinPoint jp) {
    // TODO: Rehacer logica aspect usuarios.
    log.debug("Aspect fired");
  }

  /**
   * Metodo que obtiene los parametros del aspect por nombre.
   * 
   * @param proceedingJoinPoint
   * @param parameterName
   * @return
   */
  public Object getParameterByName(JoinPoint proceedingJoinPoint, String parameterName) {
    Object oReturn = null;

    CodeSignature methodSig = (CodeSignature) proceedingJoinPoint.getSignature();
    Object[] args = proceedingJoinPoint.getArgs();
    String[] parametersName = methodSig.getParameterNames();

    try {
      // Obtenemos el idx parameter
      int idx = Arrays.asList(parametersName).indexOf(parameterName);

      if (args.length > idx) { // parameter exist
        oReturn = args[idx];
      } // otherwise your parameter does not exist by given name
    } catch (Exception e) {
      return oReturn;
    }
    return oReturn;
  }

}
