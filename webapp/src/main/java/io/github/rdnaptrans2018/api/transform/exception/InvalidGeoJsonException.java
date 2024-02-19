package io.github.rdnaptrans2018.api.transform.exception;

public class InvalidGeoJsonException extends RuntimeException {

  private static final long serialVersionUID = 3367942815511607757L;

  private static final String EXCEPTION_MESSAGE = "The GeoJSON in the requestbody is not valid.";

  public InvalidGeoJsonException(Throwable cause) {
    super(EXCEPTION_MESSAGE, cause);
  }

}
