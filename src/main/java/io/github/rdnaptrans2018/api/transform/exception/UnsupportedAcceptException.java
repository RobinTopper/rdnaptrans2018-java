package io.github.rdnaptrans2018.api.transform.exception;

public class UnsupportedAcceptException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private static final String EXCEPTION_MESSAGE_FORMAT =
      "Unsupported Accept header provided. Supported media types: %s";

  public UnsupportedAcceptException(String acceptedMediaTypes) {
    super(String.format(EXCEPTION_MESSAGE_FORMAT, acceptedMediaTypes));

  }

}
