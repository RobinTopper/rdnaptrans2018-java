package io.github.rdnaptrans2018.api.transform.exception;

public class UnsupportedTransformationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private static final String EXCEPTION_MESSAGE_FORMAT =
      "Unsupported transformation from %s to %s requested. Supported transformations are: %s";

  public UnsupportedTransformationException(String from, String to, String supportedValues) {
    super(String.format(EXCEPTION_MESSAGE_FORMAT, from, to, supportedValues));
  }
}
