package io.github.rdnaptrans2018.api;

import io.github.rdnaptrans2018.api.transform.exception.InvalidGeoJsonException;
import io.github.rdnaptrans2018.api.transform.exception.InvalidJsonException;
import io.github.rdnaptrans2018.api.transform.exception.UnsupportedAcceptException;
import io.github.rdnaptrans2018.api.transform.exception.UnsupportedCrsException;
import io.github.rdnaptrans2018.api.transform.exception.UnsupportedTransformationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.webflux.advice.ProblemHandling;
import reactor.core.publisher.Mono;

@Slf4j
@ControllerAdvice
public class ProblemHandler implements ProblemHandling {

  private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error encountered";

  @ExceptionHandler({InvalidGeoJsonException.class, UnsupportedCrsException.class,
      UnsupportedTransformationException.class, InvalidJsonException.class})
  public Mono<ResponseEntity<Problem>> handleValidationException(Exception exception, ServerWebExchange request) {
    log.info(exception.getMessage());
    return getProblem(Status.BAD_REQUEST, request, exception.getMessage());
  }

  @ExceptionHandler(UnsupportedAcceptException.class)
  public Mono<ResponseEntity<Problem>> handleUnsupportedAcceptException(Exception exception,
      ServerWebExchange request) {
    log.info(exception.getMessage());
    return getProblem(Status.NOT_ACCEPTABLE, request, exception.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<Problem>> handleOtherExceptions(Exception exception, ServerWebExchange request) {
    log.error(exception.getMessage(), exception);
    return getProblem(Status.INTERNAL_SERVER_ERROR, request, INTERNAL_SERVER_ERROR_MESSAGE);
  }

  @SuppressWarnings("null")
  private Mono<ResponseEntity<Problem>> getProblem(Status status, ServerWebExchange request, String detail) {
    var problem = Problem.builder()
        .withTitle(status.getReasonPhrase())
        .withStatus(status)
        .withDetail(detail)
        .build();
    return create(problem, request);
  }

}
