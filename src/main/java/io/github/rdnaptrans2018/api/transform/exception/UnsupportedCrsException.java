package io.github.rdnaptrans2018.api.transform.exception;

public class UnsupportedCrsException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private static final String EXCEPTION_MESSAGE_FORMAT =
      "Unsupported %s header provided: '%s'. Supported values are: %s";

  public UnsupportedCrsException(String header, String headerValue, String supportedValues) {
    super(String.format(EXCEPTION_MESSAGE_FORMAT, header, headerValue, supportedValues));
  }

}
