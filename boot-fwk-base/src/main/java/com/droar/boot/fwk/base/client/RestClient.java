package com.droar.boot.fwk.base.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.droar.boot.fwk.base.exception.ExceptionResponse;
import com.droar.boot.fwk.base.exception.ResponseStatusException;
import com.droar.boot.fwk.base.model.ClientResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 
 * @author droar
 *
 */
@Slf4j
public class RestClient {

  /** The origin. */
  private static String ORIGIN = "rest_inv";

  /** The token id. */
  private static String TOKEN_ID = "us_id";

  /** The token role. */
  private static String TOKEN_ROLE = "us_role";


  /** The webClient. */
  private static WebClient webClient = WebClient.builder().filter(errorHandlingFilter())
      .defaultHeader(HttpHeaders.ACCEPT_LANGUAGE, "es").defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .defaultHeader(HttpHeaders.ORIGIN, ORIGIN).build();


  /**
   * Generic GET invocation through webclient
   * 
   * @param <T>
   * @param completeRestUrl
   * @param token
   * @param pathParams
   * @param mpFilters
   * @param resultClassDto
   * @return
   */
  public static <T> ClientResponse<T> invokeGet(String completeRestUrl, String token, Map<String, String> pathParams,
      MultiValueMap<String, String> mpFilters, Class<T> resultClassDto) {
    ClientResponse<T> responseObject = new ClientResponse<>();

    if (StringUtils.isNoneBlank(completeRestUrl)) {
      try {
        // We append the parameters
        UriComponentsBuilder uriBuilder = appendBuilderParameters(completeRestUrl, pathParams, mpFilters);

        log.info("Requesting [GET] : " + uriBuilder.toUriString());

        // We try to do the connection, this will return the defined DTO
        responseObject.setResponseObject(webClient.get().uri(uriBuilder.toUriString())
            .header(HttpHeaders.AUTHORIZATION, StringUtils.isEmpty(token) ? "" : token).retrieve()
            .bodyToMono(resultClassDto).block());

      } catch (Exception e) {
        log.error("Error when trying to do the request to URI: " + completeRestUrl + " \nDetail: " + e.getMessage());
        if (e instanceof ResponseStatusException) {
          responseObject.setResponseException((ResponseStatusException) e);
        } else {
          responseObject.setResponseException(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
        return responseObject;
      }
    }

    return responseObject;
  }

  /**
   * Invoke Post.
   *
   * @param <T>
   * @param completeRestUrl
   * @param token
   * @param pathParams
   * @param mpFilters
   * @param resultClassDto
   * @return
   */
  public static <T> ClientResponse<T> invokePost(String completeRestUrl, String token, String jsonPost,
      Map<String, String> pathParams, MultiValueMap<String, String> mpFilters, Class<T> resultClassDto) {
    ClientResponse<T> responseObject = new ClientResponse<>();

    if (StringUtils.isNoneBlank(completeRestUrl, token)) {
      try {
        // We append the parameters
        UriComponentsBuilder uriBuilder = appendBuilderParameters(completeRestUrl, pathParams, mpFilters);

        log.info("Requesting [POST] : " + uriBuilder.toUriString());

        // We try to do the connection, this will return the defined DTO
        responseObject.setResponseObject(webClient.post().uri(uriBuilder.toUriString()).header(HttpHeaders.AUTHORIZATION, token)
            .body(BodyInserters.fromValue(jsonPost)).retrieve().bodyToMono(resultClassDto).block());

      } catch (Exception e) {
        log.error("Error when trying to do the request to URI: " + completeRestUrl + " \nDetail: " + e.getMessage());
        if (e instanceof ResponseStatusException) {
          responseObject.setResponseException((ResponseStatusException) e);
        } else {
          responseObject.setResponseException(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
        return responseObject;
      }
    }
    return responseObject;
  }

  /**
   * Invoke Patch.
   *
   * @param <T>
   * @param completeRestUrl
   * @param token
   * @param pathParams
   * @param mpFilters
   * @param resultClassDto
   * @return
   */
  public static <T> ClientResponse<T> invokePatch(String completeRestUrl, String token, String jsonPatch,
      Map<String, String> pathParams, MultiValueMap<String, String> mpFilters, Class<T> resultClassDto) {
    ClientResponse<T> responseObject = new ClientResponse<>();

    if (StringUtils.isNoneBlank(completeRestUrl, token)) {
      try {
        // We append the parameters
        UriComponentsBuilder uriBuilder = appendBuilderParameters(completeRestUrl, pathParams, mpFilters);

        log.info("Requesting [PATCH] : " + uriBuilder.toUriString());

        // We try to do the connection, this will return the defined DTO
        responseObject.setResponseObject(webClient.patch().uri(uriBuilder.toUriString()).header(HttpHeaders.AUTHORIZATION, token)
            .body(BodyInserters.fromValue(jsonPatch)).retrieve().bodyToMono(resultClassDto).block());
      } catch (Exception e) {
        log.error("Error when trying to do the request to URI: " + completeRestUrl + " \nDetail: " + e.getMessage());
        if (e instanceof ResponseStatusException) {
          responseObject.setResponseException((ResponseStatusException) e);
        } else {
          responseObject.setResponseException(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
        return responseObject;
      }
    }

    return responseObject;
  }

  /**
   * Invoke Delete.
   *
   * @param baseRestUri the baseRestUri
   * @param token the token
   * @param patchJSON the patchJSON
   * @return the responseDTO
   */
  public static <T> ClientResponse<T> invokeDelete(String completeRestUrl, String token, Map<String, String> pathParams,
      MultiValueMap<String, String> mpFilters, Class<T> resultClassDto) {
    ClientResponse<T> responseObject = new ClientResponse<>();

    if (StringUtils.isNoneBlank(completeRestUrl, token)) {
      try {

        // We append the parameters
        UriComponentsBuilder uriBuilder = appendBuilderParameters(completeRestUrl, pathParams, mpFilters);

        log.info("Requesting [DELETE] : " + uriBuilder.toUriString());

        // We try to do the connection, this will return the defined DTO
        responseObject.setResponseObject(webClient.delete().uri(uriBuilder.toUriString()).header(HttpHeaders.AUTHORIZATION, token)
            .retrieve().bodyToMono(resultClassDto).block());
      } catch (ResponseStatusException e) {
        log.error("Error when trying to do the request to URI: " + completeRestUrl + " \nDetail: " + e.getMessage());
        if (e instanceof ResponseStatusException) {
          responseObject.setResponseException((ResponseStatusException) e);
        } else {
          responseObject.setResponseException(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
        return responseObject;
      }
    }

    return responseObject;
  }

  /**
   * Invoke put.
   *
   * @param <T> the generic type
   * @param completeRestUrl the complete rest url
   * @param token the token
   * @param jsonPut the json put
   * @param pathParams the path params
   * @param mpFilters the mp filters
   * @param resultClassDto the result class dto
   * @return the client response
   */
  public static <T> ClientResponse<T> invokePut(String completeRestUrl, String token, String jsonPut,
      Map<String, String> pathParams, MultiValueMap<String, String> mpFilters, Class<T> resultClassDto) {
    ClientResponse<T> responseObject = new ClientResponse<>();

    if (StringUtils.isNoneBlank(completeRestUrl, token)) {
      try {
        // We append the parameters
        UriComponentsBuilder uriBuilder = appendBuilderParameters(completeRestUrl, pathParams, mpFilters);

        log.info("Requesting [POST] : " + uriBuilder.toUriString());

        // We try to do the connection, this will return the defined DTO
        responseObject.setResponseObject(webClient.put().uri(uriBuilder.toUriString()).header(HttpHeaders.AUTHORIZATION, token)
            .body(BodyInserters.fromValue(jsonPut)).retrieve().bodyToMono(resultClassDto).block());

      } catch (Exception e) {
        log.error("Error when trying to do the request to URI: " + completeRestUrl + " \nDetail: " + e.getMessage());
        if (e instanceof ResponseStatusException) {
          responseObject.setResponseException((ResponseStatusException) e);
        } else {
          responseObject.setResponseException(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
        return responseObject;
      }
    }
    return responseObject;
  }

  /******************************************************/
  /**                                                  **/
  /** ASYNC CALLS **/
  /**                                                  **/
  /******************************************************/

  /**
   * Invoke Get Async
   * 
   * @param <T>
   * @param completeRestUrl
   * @param token
   * @param pathParams
   * @param mapaFiltros
   * @param resultClassDto
   * @return responseMonoT
   */
  public static <T> Mono<T> invokeGetAsync(String completeRestUrl, String token, Map<String, String> pathParams,
      MultiValueMap<String, String> mapaFiltros, Class<T> resultClassDto) {
    // Variable asincrona, por lo cual habrá que suscribirse a ella.
    Mono<T> responseMonoT = null;

    if (StringUtils.isNoneBlank(completeRestUrl, token)) {
      try {
        // We append the parameters
        UriComponentsBuilder uriBuilder = appendBuilderParameters(completeRestUrl, pathParams, mapaFiltros);

        log.info("Requesting [GET-ASYNC] : " + uriBuilder.toUriString());

        // We try to do the connection, this will return the defined DTO
        responseMonoT = webClient.get().uri(uriBuilder.toUriString()).header(HttpHeaders.AUTHORIZATION, token).retrieve().bodyToMono(resultClassDto);

      } catch (Exception e) {
        log.error("Error when trying to do the request to URI: " + completeRestUrl + " \nDetail: " + e.getMessage());
        return responseMonoT;
      }
    }

    return responseMonoT;
  }

  /**
   * Invoke Post Async.
   *
   * @param <T>
   * @param completeRestUrl
   * @param token
   * @param pathParams
   * @param mpFilters
   * @param resultClassDto
   * @return responseMonoT
   */
  public static <T> Mono<T> invokePostAsync(String completeRestUrl, String token, String jsonPost, Map<String, String> pathParams,
      MultiValueMap<String, String> mpFilters, Class<T> resultClassDto) {
    Mono<T> responseMonoT = null;

    if (StringUtils.isNoneBlank(completeRestUrl)) {
      try {
        // We append the parameters
        UriComponentsBuilder uriBuilder = appendBuilderParameters(completeRestUrl, pathParams, mpFilters);

        log.info("Requesting [POST-ASYNC] : " + uriBuilder.toUriString());

        // We try to do the connection, this will return the defined DTO
        responseMonoT = webClient.post().uri(uriBuilder.toUriString())
            .header(HttpHeaders.AUTHORIZATION, StringUtils.isEmpty(token) ? "" : token).body(BodyInserters.fromValue(jsonPost))
            .retrieve().bodyToMono(resultClassDto);

      } catch (Exception e) {
        log.error("Error when trying to do the request to URI: " + completeRestUrl + " \nDetail: " + e.getMessage());
        return responseMonoT;
      }
    }

    return responseMonoT;
  }

  /**
   * Invoke Patch Async.
   *
   * @param <T>
   * @param completeRestUrl
   * @param token
   * @param pathParams
   * @param mpFilters
   * @param resultClassDto
   * @return responseMonoT
   */
  public static <T> Mono<T> invokePatchAsync(String completeRestUrl, String token, String jsonPatch,
      Map<String, String> pathParams, MultiValueMap<String, String> mpFilters, Class<T> resultClassDto) {
    Mono<T> responseMonoT = null;

    if (StringUtils.isNoneBlank(completeRestUrl, token)) {
      try {
        // We append the parameters
        UriComponentsBuilder uriBuilder = appendBuilderParameters(completeRestUrl, pathParams, mpFilters);

        log.info("Requesting [PATCH-ASYNC] : " + uriBuilder.toUriString());

        // We try to do the connection, this will return the defined DTO
        responseMonoT = webClient.patch().uri(uriBuilder.toUriString()).header(HttpHeaders.AUTHORIZATION, token)
            .body(BodyInserters.fromValue(jsonPatch)).retrieve().bodyToMono(resultClassDto);
      } catch (Exception e) {
        log.error("Error when trying to do the request to URI: " + completeRestUrl + " \nDetail: " + e.getMessage());
        return responseMonoT;
      }
    }

    return responseMonoT;
  }

  /**
   * Invoke Delete Async.
   *
   * @param baseRestUri the baseRestUri
   * @param token the token
   * @param patchJSON the patchJSON
   * @return the responseMonoT
   */
  public static <T> Mono<T> invokeDeleteAsync(String completeRestUrl, String token, Map<String, String> pathParams,
      Class<T> resultClassDto) {
    Mono<T> responseMonoT = null;

    if (StringUtils.isNoneBlank(completeRestUrl, token)) {
      try {
        // We append the parameters
        UriComponentsBuilder uriBuilder = appendBuilderParameters(completeRestUrl, pathParams, null);

        log.info("Requesting [DELETE-ASYNC] : " + uriBuilder.toUriString());

        // We try to do the connection, this will return the defined DTO
        responseMonoT = webClient.delete().uri(uriBuilder.toUriString()).header(HttpHeaders.AUTHORIZATION, token).retrieve().bodyToMono(resultClassDto);
      } catch (Exception e) {
        log.error("Error when trying to do the request to URI: " + completeRestUrl + " \nDetail: " + e.getMessage());
        return responseMonoT;
      }
    }

    return responseMonoT;
  }


  /**
   * Generates a token with an id and role (id and role for example).
   *
   * @param id
   * @param role
   * @return
   */
  public static String generateToken(String id, String role) {
    String token = "";

    if (StringUtils.isNoneBlank(id, role)) {
      try {
        // Secret Algorithm -> secret
        String SECRET_KEY = "2bb80d537b1da3e38bd30361aa855686bde0eacd7162fef6a25fe97bf527a25b";

        // Simple 512 algorithm
        Algorithm algorithm = Algorithm.HMAC512(SECRET_KEY);
        
        // we generate the claims
        JWTCreator.Builder builder = JWT.create()
            .withClaim(TOKEN_ID, id)
            .withClaim(TOKEN_ROLE, role);
               
        // Then we sign it.
        token = builder.sign(algorithm);

      } catch (Exception exception) {
        log.error("error generating the token");
        return token;
      }
    }

    return token;
  }

  /**
   * Error handling filter.
   *
   * @return the exchange filter function
   */
  public static ExchangeFilterFunction errorHandlingFilter() {
    return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
      if (clientResponse.statusCode() != null
          && (clientResponse.statusCode().is5xxServerError() || clientResponse.statusCode().is4xxClientError())) {
        return clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
          ExceptionResponse e = getExceptionResponse(errorBody, clientResponse.statusCode().getReasonPhrase());
          return Mono.error(new ResponseStatusException(clientResponse.statusCode(), e.getMensaje(), e.getCodigo()));
        });
      } else {
        return Mono.just(clientResponse);
      }
    });
  }

  /**
   * Gets the exception response.
   *
   * @param body the body
   * @param status the status
   * @param reason the reason
   * @return the exception response
   */
  private static ExceptionResponse getExceptionResponse(String body, String reason) {

    List<ExceptionResponse> errorList = new ArrayList<ExceptionResponse>();
    ExceptionResponse exceptionResponse = new ExceptionResponse(0, reason);

    // If the response is not blank, we parse it
    if (StringUtils.isNotBlank(body)) {
      try {
        // We get the embedded and items if any
        JSONObject oJsonResponse = new JSONObject(body);
        JSONArray jsonErrorList =
            (oJsonResponse != null && oJsonResponse.has("errorList") ? oJsonResponse.getJSONArray("errorList") : null);

        if (jsonErrorList != null) {
          // We user mapper jackson, GSON gives problems with @JsonProperty
          ObjectMapper oMapper = new ObjectMapper();
          oMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
          errorList = oMapper.readValue(jsonErrorList.toString(), new TypeReference<ArrayList<ExceptionResponse>>() {});

          if (CollectionUtils.isNotEmpty(errorList)) {
            exceptionResponse = errorList.get(0);
          }
        }
      } catch (Exception e) {
        log.error("Error when trying to parse response JSON: " + e.getMessage());
      }
    }

    return exceptionResponse;
  }

  /**
   * Append builder parameters
   * 
   * @param completeRestUrl
   * @param mpPathParams
   * @param mpFilters
   * @return
   */
  private static UriComponentsBuilder appendBuilderParameters(String completeRestUrl, Map<String, String> mpPathParams,
      MultiValueMap<String, String> mpFilters) {
    // We transform the whole URL to a valid one
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(completeRestUrl);

    // Regex to validate path parameters
    String pathRegex = "\\{(.*?)\\}";

    Pattern pattern = Pattern.compile(pathRegex);
    Matcher matcher = pattern.matcher(completeRestUrl);

    // If we have a match, we might search for the path parameters
    if (matcher.find(0)) {
      if (MapUtils.isNotEmpty(mpPathParams)) {
        // We replace them on the URI
        uriBuilder.uri(uriBuilder.buildAndExpand(mpPathParams).toUri());
      } else {
        // If they havent informed the path parameters we might give an error.
        log.error("The request needs to have path parameters");
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "The request needs to have path parameters, they havent been informed");
      }
    }

    // If we have filters we aswell add them to the query parameters
    if (MapUtils.isNotEmpty(mpFilters))
      uriBuilder.queryParams(mpFilters);
    return uriBuilder;
  }

}
