package com.droar.boot.fwk.base.client;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

/**
 * Soap generic connector
 * 
 * @author Damian
 *
 */
public class SOAPClient extends WebServiceGatewaySupport {

  /**
   *  Call generic webservice url, and unmarshal it
   * @param url
   * @param request
   * @return
   */
  public Object callWebService(String url, Object request) {
    return getWebServiceTemplate().marshalSendAndReceive(url, request);
  }
}
