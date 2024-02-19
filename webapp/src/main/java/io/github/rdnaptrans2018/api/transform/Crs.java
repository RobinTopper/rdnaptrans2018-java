package io.github.rdnaptrans2018.api.transform;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Crs {

  RD_NAP(7415, "epsg:7415", 1e4, 4), ETRS89_ETRF2000(7931, "epsg:7931", 1e9, 9);

  private final int srid;

  private final String epsgCode;

  private final double scale;

  private final int decimals;

}
