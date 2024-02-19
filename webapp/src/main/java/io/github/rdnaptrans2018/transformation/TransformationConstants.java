package io.github.rdnaptrans2018.transformation;

import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;
import io.github.rdnaptrans2018.transformation.grid.NLGEO2018;
import io.github.rdnaptrans2018.transformation.grid.RDCORR2018;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;

public final class TransformationConstants {

  private TransformationConstants() {};

  public static final double GRS80_A = 6378137;
  public static final double GRS80_F = 1. / 298.257222101;
  public static final double GRS80_EPSILON = 2e-11;

  public static final double ETRS89_H0 = 43;

  public static final double TX = -565.7346;
  public static final double TY = -50.4058;
  public static final double TZ = -465.2895;
  public static final double ALPHA = -1.9151255475293e-6;
  public static final double BETA = 1.6036473018269e-6;
  public static final double GAMMA = -9.0954585716021e-6;
  public static final double DELTA = -4.07242e-6;

  public static final double TX_INV = 565.7381;
  public static final double TY_INV = 50.4018;
  public static final double TZ_INV = 465.2904;
  public static final double ALPHA_INV = 1.9151400919398e-6;
  public static final double BETA_INV = -1.6036279092796e-6;
  public static final double GAMMA_INV = 9.0954634197389e-6;
  public static final double DELTA_INV = 4.07244e-6;

  public static final double BESSEL1841_A = 6377397.155;
  public static final double BESSEL1841_F = 1. / 299.1528128;
  public static final double BESSEL1841_EPSILON = 2e-11;

  public static final double PHI_MIN = 50;
  public static final double PHI_MAX = 56;
  public static final double LAMBDA_MIN = 2;
  public static final double LAMBDA_MAX = 8;
  public static final double DELTA_PHI = 0.0125;
  public static final double DELTA_LAMBDA = 0.02;

  public static final double RD_EPSILON = 1e-9;

  public static final double AMERSFOORT_PHI0 = 0.91029672689324;
  public static final double AMERSFOORT_LAMBDA0 = 0.0940320375196;
  public static final double AMERSFOORT_K = 0.9999079;
  public static final double AMERSFOORT_X0 = 155000;
  public static final double AMERSFOORT_Y0 = 463000;

  public static final double RD_BESSEL_H0 = 0;

  // Derived constants
  public static final double E_SQUARE_GRS80 = GRS80_F * (2 - GRS80_F);

  public static final double S = 1. + DELTA;
  public static final double R11 = cos(GAMMA) * cos(BETA);
  public static final double R12 = cos(GAMMA) * sin(BETA) * sin(ALPHA) + sin(GAMMA) * cos(ALPHA);
  public static final double R13 = -1. * cos(GAMMA) * sin(BETA) * cos(ALPHA) + sin(GAMMA) * sin(ALPHA);
  public static final double R21 = -1. * sin(GAMMA) * cos(BETA);
  public static final double R22 = -1. * sin(GAMMA) * sin(BETA) * sin(ALPHA) + cos(GAMMA) * cos(ALPHA);
  public static final double R23 = sin(GAMMA) * sin(BETA) * cos(ALPHA) + cos(GAMMA) * sin(ALPHA);
  public static final double R31 = sin(BETA);
  public static final double R32 = -1. * cos(BETA) * sin(ALPHA);
  public static final double R33 = cos(BETA) * cos(ALPHA);

  public static final double E_SQUARE_B1841 = BESSEL1841_F * (2. - BESSEL1841_F);

  public static final double NLAMBDA = 1. + ((LAMBDA_MAX - LAMBDA_MIN) / DELTA_LAMBDA);

  public static final double E_B1841 = sqrt(E_SQUARE_B1841);
  public static final double Q0 = log(tan((AMERSFOORT_PHI0 + PI / 2.) / 2.))
      - (E_B1841 / 2.) * (log((1. + E_B1841 * sin(AMERSFOORT_PHI0)) / (1. - E_B1841 * sin(AMERSFOORT_PHI0))));
  public static final double RN = BESSEL1841_A / sqrt(1. - (pow(E_B1841, 2) * (pow(sin(AMERSFOORT_PHI0), 2))));
  public static final double RM =
      (RN * (1. - pow(E_B1841, 2))) / (1. - (pow(E_B1841, 2) * (pow(sin(AMERSFOORT_PHI0), 2))));
  public static final double R_SPHERE = sqrt(RM * RN);
  public static final double PHI0_C = atan((sqrt(RM) / sqrt(RN)) * tan(AMERSFOORT_PHI0));
  public static final double LAMBDA0_C = AMERSFOORT_LAMBDA0;
  public static final double W0 = log(tan((PHI0_C + PI / 2.) / 2.));
  public static final double N =
      sqrt(1. + ((pow(E_B1841, 2) * (pow(cos(AMERSFOORT_PHI0), 4))) / (1 - pow(E_B1841, 2))));
  public static final double M = W0 - N * Q0;

  public static final double S_INV = 1. + DELTA_INV;
  public static final double R11_INV = cos(GAMMA_INV) * cos(BETA_INV);
  public static final double R12_INV =
      cos(GAMMA_INV) * sin(BETA_INV) * sin(ALPHA_INV) + sin(GAMMA_INV) * cos(ALPHA_INV);
  public static final double R13_INV =
      -1. * cos(GAMMA_INV) * sin(BETA_INV) * cos(ALPHA_INV) + sin(GAMMA_INV) * sin(ALPHA_INV);
  public static final double R21_INV = -1. * sin(GAMMA_INV) * cos(BETA_INV);
  public static final double R22_INV =
      -1. * sin(GAMMA_INV) * sin(BETA_INV) * sin(ALPHA_INV) + cos(GAMMA_INV) * cos(ALPHA_INV);
  public static final double R23_INV =
      sin(GAMMA_INV) * sin(BETA_INV) * cos(ALPHA_INV) + cos(GAMMA_INV) * sin(ALPHA_INV);
  public static final double R31_INV = sin(BETA_INV);
  public static final double R32_INV = -1. * cos(BETA_INV) * sin(ALPHA_INV);
  public static final double R33_INV = cos(BETA_INV) * cos(ALPHA_INV);


  // Grid files: NLGEO2018 = ETRS89 quasi-geoid height; RDCORR2018 = RD Bessel Correction
  public static final List<NLGEO2018> NLGEO2018_GRID;
  public static final List<RDCORR2018> RDCORR2018_GRID;

  static {
    try {
      var nlgeoPath = new ClassPathResource("transformation/grid/nlgeo2018.txt").getFile()
          .toPath();

      NLGEO2018_GRID = Files.lines(nlgeoPath)
          .skip(1L)
          .map(line -> {
            var values = line.split("\\s+");
            return new NLGEO2018(Double.parseDouble(values[0]), Double.parseDouble(values[1]),
                Double.parseDouble(values[2]));
          })
          .collect(Collectors.toList());

      var rdcorrPath = new ClassPathResource("transformation/grid/rdcorr2018.txt").getFile()
          .toPath();

      RDCORR2018_GRID = Files.lines(rdcorrPath)
          .skip(1L)
          .map(line -> {
            var values = line.split("\\s+");
            return new RDCORR2018(Double.parseDouble(values[0]), Double.parseDouble(values[1]),
                Double.parseDouble(values[2]), Double.parseDouble(values[3]));
          })
          .collect(Collectors.toList());
    } catch (Exception ex) {
      throw new ExceptionInInitializerError(ex);
    }
  }
}
