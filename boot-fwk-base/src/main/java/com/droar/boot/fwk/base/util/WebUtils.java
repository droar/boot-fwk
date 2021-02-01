package com.droar.boot.fwk.base.util;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.internal.oxm.conversion.Base64;
import org.json.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class CifradoUtils.
 */
@Slf4j
public class WebUtils {

  /**
   * Encode password.
   *
   * @param password the password
   * @return the byte[]
   * @throws UnsupportedEncodingException the unsupported encoding exception
   */
  public static byte[] encodePassword(String password) {
    byte pwdArray[] = null;

    try {
      final String quotedPassword = '"' + password + '"';
      pwdArray = quotedPassword.getBytes("UTF-16LE");
    } catch (final UnsupportedEncodingException uee) {
    }
    return pwdArray;
  }

  /**
   * Returns a valid token in json.
   *
   * @param token the token
   * @return the json token object
   */
  public static JSONObject getDecodedJsonObject(String token) {
    JSONObject oJsonResponse = null;

    if (StringUtils.isNotBlank(token)) {
      String[] splitToken = token.split("\\.");

      // String base64EncodedHeader = splitToken[0];
      String base64EncodedBody = splitToken[1];
      // String base64EncodedSignature = splitToken[2];

      String body = new String(Base64.base64Decode(base64EncodedBody.getBytes()));
      // String header = new String(base64Url.decode(base64EncodedHeader));

      ObjectMapper oMapper = new ObjectMapper();
      oMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      oJsonResponse = new JSONObject(body);
    }

    return oJsonResponse;
  }


  /**
   * Gets an encoded String
   * 
   * @param initialString
   * @return
   */
  public static String getEncodedString(String initialString) {
    byte[] encodedString = Base64.base64Encode(initialString.getBytes());
    return new String(encodedString);
  }

  /**
   * Gets the string of the local context path.
   * 
   * @return
   */
  public static String getLocalContextPath() {
    String localContextPath = "";

    try {
      // Get get the request initial
      HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

      if (request != null) {
        // We get the url
        localContextPath = ServletUriComponentsBuilder.fromServletMapping(request).path(request.getServletPath()).toUriString();
      }

    } catch (Exception e) {
      log.warn("There isnt a context path to recover, ¿Is this method being called from a web context?");
    }
    return localContextPath;
  }

}
