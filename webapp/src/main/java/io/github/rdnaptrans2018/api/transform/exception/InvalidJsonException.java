package io.github.rdnaptrans2018.api.transform.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class InvalidJsonException extends RuntimeException {

  private static final long serialVersionUID = 6996783436837797808L;

  private static final String EXCEPTION_MESSAGE = "The JSON in the requestbody is not valid.";

  public InvalidJsonException() {
    super(EXCEPTION_MESSAGE);
  }

  public InvalidJsonException(JsonProcessingException cause) {
    super(EXCEPTION_MESSAGE, cause);
  }
}
