package io.github.rdnaptrans2018.api.transform;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.PrecisionModel;

public class PrecisionModel3D extends PrecisionModel {

  private static final long serialVersionUID = 1109201215509016649L;

  public PrecisionModel3D(double scale) {
    super(scale);
  }

  @Override
  public void makePrecise(Coordinate coord) {
    coord.x = makePrecise(coord.x);
    coord.y = makePrecise(coord.y);
    coord.z = makePrecise(coord.z);
  }

}
