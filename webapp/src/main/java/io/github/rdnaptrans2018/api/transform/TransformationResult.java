package io.github.rdnaptrans2018.api.transform;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Builder;

@Builder
public class TransformationResult {

  @JsonRawValue
  private String data;

}
