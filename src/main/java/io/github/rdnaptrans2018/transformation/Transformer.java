package io.github.rdnaptrans2018.transformation;

import static io.github.rdnaptrans2018.transformation.TransformationConstants.AMERSFOORT_K;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.AMERSFOORT_LAMBDA0;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.AMERSFOORT_X0;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.AMERSFOORT_X0_BIGD;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.AMERSFOORT_Y0;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.BESSEL1841_A;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.BESSEL1841_EPSILON;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.DELTA_LAMBDA;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.DELTA_PHI;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.ETRS89_H0;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.E_B1841;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.E_SQUARE_B1841;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.E_SQUARE_GRS80;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.GRS80_A;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.GRS80_EPSILON;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.LAMBDA0_C;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.LAMBDA0_C_BIGD;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.LAMBDA_MAX_BIGD;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.LAMBDA_MIN;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.LAMBDA_MIN_BIGD;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.M;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.N;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.NLAMBDA;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.NLGEO2018_GRID;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.PHI0_C;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.PHI0_C_NEGATIVE_BIGD;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.PHIO_C_BIGD;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.PHI_MAX_BIGD;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.PHI_MIN;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.PHI_MIN_BIGD;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.PI_DIVIDED_BY_2;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.PI_DIVIDED_BY_2_NEGATIVE;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.PI_MINUS_LAMBDA0_C_BIGD;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.R11;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.R11_INV;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.R12;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.R12_INV;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.R13;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.R13_INV;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.R21;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.R21_INV;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.R22;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.R22_INV;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.R23;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.R23_INV;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.R31;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.R31_INV;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.R32;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.R32_INV;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.R33;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.R33_INV;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.RDCORR2018_GRID;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.RD_BESSEL_H0;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.RD_EPSILON;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.R_SPHERE;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.S;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.S_INV;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.TX;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.TX_INV;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.TY;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.TY_INV;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.TZ;
import static io.github.rdnaptrans2018.transformation.TransformationConstants.TZ_INV;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.asin;
import static java.lang.Math.atan;
import static java.lang.Math.ceil;
import static java.lang.Math.cos;
import static java.lang.Math.exp;
import static java.lang.Math.floor;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;
import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Component;

@Component
public class Transformer {

  public Coordinate transformEtrs89ToRdNap(double lat, double lon, double height) {

    // TODO: waarden af ronden voor '=='/'>='/'<=' vergelijkingen met BigDecimals

    // Section 2.2.1
    var phi = lat * PI / 180.;
    var lambda = lon * PI / 180.;

    var RN = GRS80_A / sqrt(1. - (E_SQUARE_GRS80 * pow(sin(phi), 2)));

    var X1geo = (RN + ETRS89_H0) * cos(phi) * cos(lambda);
    var Y1geo = (RN + ETRS89_H0) * cos(phi) * sin(lambda);
    var Z1geo = ((RN * (1. - E_SQUARE_GRS80)) + ETRS89_H0) * sin(phi);

    // Section 2.2.2
    var X2geo = S * (R11 * X1geo + R12 * Y1geo + R13 * Z1geo) + TX;
    var Y2geo = S * (R21 * X1geo + R22 * Y1geo + R23 * Z1geo) + TY;
    var Z2geo = S * (R31 * X1geo + R32 * Y1geo + R33 * Z1geo) + TZ;

    // Section 2.2.3
    var phi1 = phi;

    do {
      phi = phi1;
      RN = BESSEL1841_A / sqrt(1. - (E_SQUARE_B1841 * pow(sin(phi), 2)));
      if (X2geo > 0.) {
        phi1 = atan((Z2geo + E_SQUARE_B1841 * RN * sin(phi)) / sqrt(pow(X2geo, 2) + pow(Y2geo, 2)));
      } else if (BigDecimal.valueOf(X2geo)
          .setScale(11, RoundingMode.HALF_EVEN)
          .compareTo(ZERO) == 0
          && BigDecimal.valueOf(Y2geo)
              .setScale(11, RoundingMode.HALF_EVEN)
              .compareTo(ZERO) == 0
          && BigDecimal.valueOf(Z2geo)
              .setScale(11, RoundingMode.HALF_EVEN)
              .compareTo(ZERO) >= 0) {
        phi1 = PI_DIVIDED_BY_2;
      } else {
        phi1 = PI_DIVIDED_BY_2_NEGATIVE;
      }
    } while (abs(phi1 - phi) > BESSEL1841_EPSILON);

    phi = phi1;

    if (X2geo > 0.) {
      lambda = atan(Y2geo / X2geo);
    } else if (X2geo < 0. && BigDecimal.valueOf(Y2geo)
        .compareTo(ZERO) >= 0) {
      lambda = atan(Y2geo / X2geo) + PI;
    } else if (X2geo < 0. && Y2geo < 0.) {
      lambda = atan(Y2geo / X2geo) - PI;
    } else if (BigDecimal.valueOf(X2geo)
        .compareTo(ZERO) == 0 && Y2geo > 0.) {
      lambda = PI_DIVIDED_BY_2;
    } else if (BigDecimal.valueOf(X2geo)
        .compareTo(ZERO) == 0 && Y2geo < 0.) {
      lambda = PI_DIVIDED_BY_2_NEGATIVE;
    } else if (BigDecimal.valueOf(X2geo)
        .compareTo(ZERO) == 0
        && BigDecimal.valueOf(Y2geo)
        .compareTo(ZERO) == 0) {
      lambda = 0.;
    }

    // Section 2.3.1
    phi = phi * 180. / PI;
    lambda = lambda * 180. / PI;

    var phi0 = phi;
    phi1 = phi;
    var lambda0 = lambda;
    var lambda1 = lambda;

    var insideBoundsCorrectionGrid = false;
    var phiThresholdReached = false;
    var lambdaThresholdReached = false;

    double nw_phi = 0., ne_phi = 0., sw_phi = 0., se_phi = 0.;
    double nw_lambda = 0., ne_lambda = 0., sw_lambda = 0., se_lambda = 0.;

    do {
      phi = phi1;
      lambda = lambda1;

      var phinorm = (phi - PHI_MIN) / DELTA_PHI;
      var lambdanorm = (lambda - LAMBDA_MIN) / DELTA_LAMBDA;

      if (BigDecimal.valueOf(phi)
          .compareTo(PHI_MIN_BIGD) >= 0
          && BigDecimal.valueOf(phi)
              .compareTo(PHI_MAX_BIGD) <= 0
          && BigDecimal.valueOf(lambda)
              .compareTo(LAMBDA_MIN_BIGD) >= 0
          && BigDecimal.valueOf(lambda)
              .compareTo(LAMBDA_MAX_BIGD) <= 0) {

        insideBoundsCorrectionGrid = true;

        var i_nw = (int) round(ceil(phinorm) * NLAMBDA + floor(lambdanorm));
        var i_ne = (int) round(ceil(phinorm) * NLAMBDA + ceil(lambdanorm));
        var i_sw = (int) round(floor(phinorm) * NLAMBDA + floor(lambdanorm));
        var i_se = (int) round(floor(phinorm) * NLAMBDA + ceil(lambdanorm));


        nw_phi = RDCORR2018_GRID.get(i_nw)
            .getLat_corr();
        ne_phi = RDCORR2018_GRID.get(i_ne)
            .getLat_corr();
        sw_phi = RDCORR2018_GRID.get(i_sw)
            .getLat_corr();
        se_phi = RDCORR2018_GRID.get(i_se)
            .getLat_corr();
        nw_lambda = RDCORR2018_GRID.get(i_nw)
            .getLon_corr();
        ne_lambda = RDCORR2018_GRID.get(i_ne)
            .getLon_corr();
        sw_lambda = RDCORR2018_GRID.get(i_sw)
            .getLon_corr();
        se_lambda = RDCORR2018_GRID.get(i_se)
            .getLon_corr();

      } else {
        insideBoundsCorrectionGrid = false;
      }

      if (!phiThresholdReached) {
        var RDcorrLat = 0.;
        if (insideBoundsCorrectionGrid) {
          RDcorrLat = (phinorm - floor(phinorm))
              * ((nw_phi * (floor(lambdanorm) + 1. - lambdanorm)) + ne_phi * (lambdanorm - floor(lambdanorm)))
              + (floor(phinorm) + 1. - phinorm)
              * ((sw_phi * (floor(lambdanorm) + 1. - lambdanorm)) + se_phi * (lambdanorm - floor(lambdanorm)));
        }
        phi1 = phi0 - RDcorrLat;
        if (abs(phi1 - phi) < RD_EPSILON) {
          phiThresholdReached = true;
        }
      }

      if (!lambdaThresholdReached) {
        var RDcorrLon = 0.;
        if (insideBoundsCorrectionGrid) {
          RDcorrLon = (phinorm - floor(phinorm))
              * ((nw_lambda * (floor(lambdanorm) + 1. - lambdanorm)) + ne_lambda * (lambdanorm - floor(lambdanorm)))
              + (floor(phinorm) + 1. - phinorm) * ((sw_lambda * (floor(lambdanorm) + 1. - lambdanorm))
              + se_lambda * (lambdanorm - floor(lambdanorm)));
        }
        lambda1 = lambda0 - RDcorrLon;
        if (abs(lambda1 - lambda) < RD_EPSILON) {
          lambdaThresholdReached = true;
        }
      }
    } while (!phiThresholdReached && !lambdaThresholdReached);

    phi = phi1;
    lambda = lambda1;

    // Section 2.4.1

    phi = phi * PI / 180.;
    lambda = lambda * PI / 180.;

    var q = log(tan((phi + PI_DIVIDED_BY_2) / 2.)) - (E_B1841 / 2) * (log((1 + E_B1841 * sin(phi)) / (1 - E_B1841 * sin(phi))));
    var w = N * q + M;
    var phiC = 2. * atan(exp(w)) - PI_DIVIDED_BY_2;
    var lambdaC = LAMBDA0_C + N * (lambda - AMERSFOORT_LAMBDA0);

    var sinPsi2 =
        sqrt(pow(sin((phiC - PHI0_C) / 2), 2) + ((pow(sin((lambdaC - LAMBDA0_C) / 2), 2)) * cos(phiC) * cos(PHI0_C)));
    var cosPsi2 = sqrt(1 - pow(sinPsi2, 2));
    var tanPsi2 = sinPsi2 / cosPsi2;
    var sinAlpha = (sin(lambdaC - LAMBDA0_C) * cos(phiC)) / (2 * sinPsi2 * cosPsi2);
    var cosAlpha =
        (sin(phiC) - sin(PHI0_C) + 2 * sin(PHI0_C) * (pow(sinPsi2, 2))) / (2 * cos(PHI0_C) * sinPsi2 * cosPsi2);
    var rDistance = 2 * AMERSFOORT_K * R_SPHERE * tanPsi2;

    double RD_x = Double.NaN, RD_y = Double.NaN;

    var phicBigDecimal = BigDecimal.valueOf(phiC);
    if (phicBigDecimal
        .compareTo(PHIO_C_BIGD) == 0
        && BigDecimal.valueOf(lambdaC)
            .compareTo(LAMBDA0_C_BIGD) == 0) {
      RD_x = AMERSFOORT_X0;
      RD_y = AMERSFOORT_Y0;
    } else if ((phicBigDecimal
        .compareTo(PHIO_C_BIGD) != 0
        || BigDecimal.valueOf(lambdaC)
            .compareTo(LAMBDA0_C_BIGD) != 0)
        && (phicBigDecimal
            .compareTo(PHI0_C_NEGATIVE_BIGD) != 0
            || BigDecimal.valueOf(lambdaC)
                .compareTo(PI_MINUS_LAMBDA0_C_BIGD) != 0)) {
      RD_x = rDistance * sinAlpha + AMERSFOORT_X0;
      RD_y = rDistance * cosAlpha + AMERSFOORT_Y0;
    } else if (phicBigDecimal
        .compareTo(PHI0_C_NEGATIVE_BIGD) == 0
        && BigDecimal.valueOf(lambdaC)
            .compareTo(TransformationConstants.LAMBDA0_C_MINUS_PI_BIGD) == 0) {
      RD_x = Double.NaN;
      RD_y = Double.NaN;
    }

    // Section 2.5.1 (height)

    phi = lat;
    lambda = lon;

    var phinorm = (phi - PHI_MIN) / DELTA_PHI;
    var lambdanorm = (lambda - LAMBDA_MIN) / DELTA_LAMBDA;

    var RD_z = Double.NaN;

    if (BigDecimal.valueOf(phi)
        .compareTo(PHI_MIN_BIGD) >= 0
        && BigDecimal.valueOf(phi)
            .compareTo(PHI_MAX_BIGD) <= 0
        && BigDecimal.valueOf(lambda)
            .compareTo(LAMBDA_MIN_BIGD) >= 0
        && BigDecimal.valueOf(lambda)
            .compareTo(LAMBDA_MAX_BIGD) <= 0) {

      var i_nw = (int) round(ceil(phinorm) * NLAMBDA + floor(lambdanorm));
      var i_ne = (int) round(ceil(phinorm) * NLAMBDA + ceil(lambdanorm));
      var i_sw = (int) round(floor(phinorm) * NLAMBDA + floor(lambdanorm));
      var i_se = (int) round(floor(phinorm) * NLAMBDA + ceil(lambdanorm));

      var nw_height = NLGEO2018_GRID.get(i_nw)
          .getNap_height();
      var ne_height = NLGEO2018_GRID.get(i_ne)
          .getNap_height();
      var sw_height = NLGEO2018_GRID.get(i_sw)
          .getNap_height();
      var se_height = NLGEO2018_GRID.get(i_se)
          .getNap_height();

      var etrs89_quasi_height = (phinorm - floor(phinorm))
          * ((nw_height * (floor(lambdanorm) + 1. - lambdanorm)) + ne_height * (lambdanorm - floor(lambdanorm)))
          + (floor(phinorm) + 1. - phinorm)
              * ((sw_height * (floor(lambdanorm) + 1. - lambdanorm)) + se_height * (lambdanorm - floor(lambdanorm)));
      RD_z = height - etrs89_quasi_height;
    }

    return new Coordinate(RD_x, RD_y, RD_z);
  }

  public Coordinate transformRdNapToEtrs89(double x, double y, double z) {

    // Section 3.1.1

    var rDistance = sqrt(pow(x - AMERSFOORT_X0, 2) + pow(y - AMERSFOORT_Y0, 2));
    var sinAlpha = (x - AMERSFOORT_X0) / rDistance;
    var cosAlpha = (y - AMERSFOORT_Y0) / rDistance;
    var psi = 2 * atan(rDistance / (2 * AMERSFOORT_K * R_SPHERE));

    double xNorm, yNorm, zNorm;

    if (x != AMERSFOORT_X0 && y != AMERSFOORT_Y0) {
      xNorm = cos(PHI0_C) * cos(psi) - cosAlpha * sin(PHI0_C) * sin(psi);
      yNorm = sinAlpha * sin(psi);
      zNorm = cosAlpha * cos(PHI0_C) * sin(psi) + sin(PHI0_C) * cos(psi);
    } else {
      xNorm = cos(PHI0_C);
      yNorm = 0.;
      zNorm = sin(PHI0_C);
    }
    var phiC = asin(zNorm);

    double lambdaC;

    if (xNorm > 0.) {
      lambdaC = LAMBDA0_C + atan(yNorm / xNorm);
    } else {
      var bigDecimalOfX = BigDecimal.valueOf(x);
      if (xNorm < 0. && bigDecimalOfX
          .compareTo(AMERSFOORT_X0_BIGD) >= 0) {
        lambdaC = LAMBDA0_C + atan(yNorm / xNorm) + PI;
      } else if (xNorm < 0. && x < AMERSFOORT_X0) {
        lambdaC = LAMBDA0_C + atan(yNorm / xNorm) - PI;
      } else {
        var bigDecimalOfxNorm = BigDecimal.valueOf(xNorm);
        if (bigDecimalOfxNorm
            .compareTo(ZERO) == 0 && x > AMERSFOORT_X0) {
          lambdaC = LAMBDA0_C + atan(yNorm / xNorm) + PI / 2;
        } else if (bigDecimalOfxNorm
            .compareTo(ZERO) == 0 && x < AMERSFOORT_X0) {
          lambdaC = LAMBDA0_C + atan(yNorm / xNorm) - PI / 2;
        } else if (bigDecimalOfxNorm
            .compareTo(ZERO) == 0
            && bigDecimalOfX
                .compareTo(AMERSFOORT_X0_BIGD) == 0) {
          lambdaC = LAMBDA0_C;
        } else {
          throw new IllegalStateException("Unknown combination of xNorm and x (Section 3.1.1)");
        }
      }
    }

    // Section 3.1.2

    var w = log(tan((phiC + PI_DIVIDED_BY_2) / 2.));
    var q = (w - M) / N;

    var phi = phiC;
    var phi1 = phiC;
    do {
      phi = phi1;
      if (phiC > PI_DIVIDED_BY_2_NEGATIVE && phiC < PI_DIVIDED_BY_2) {
        phi1 =
            2. * atan(exp(q + (E_B1841 / 2.) * log((1. + E_B1841 * sin(phi)) / (1. - E_B1841 * sin(phi))))) - PI_DIVIDED_BY_2;
      } else {
        phi1 = phiC;
      }
    } while (abs(phi1 - phi) > BESSEL1841_EPSILON);

    phi = phi1;
    var lambdaN = ((lambdaC - LAMBDA0_C) / N) + AMERSFOORT_LAMBDA0;
    var lambda = lambdaN + 2. * PI * floor((PI - lambdaN) / (2. * PI));


    // Section 3.2.1

    phi = phi * 180. / PI;
    lambda = lambda * 180. / PI;

    var inside_bounds_correction_grid = false;

    double nw_phi = 0, ne_phi = 0, sw_phi = 0, se_phi = 0;
    double nw_lambda = 0, ne_lambda = 0, sw_lambda = 0, se_lambda = 0;

    var phinorm = (phi - PHI_MIN) / DELTA_PHI;
    var lambdanorm = (lambda - LAMBDA_MIN) / DELTA_LAMBDA;

    var bigDecimalPhi = BigDecimal.valueOf(phi);
    if (bigDecimalPhi
        .compareTo(PHI_MIN_BIGD) >= 0
        && bigDecimalPhi
            .compareTo(PHI_MAX_BIGD) <= 0
        && BigDecimal.valueOf(lambda)
            .compareTo(LAMBDA_MIN_BIGD) >= 0
        && BigDecimal.valueOf(lambda)
            .compareTo(LAMBDA_MAX_BIGD) <= 0) {
      inside_bounds_correction_grid = true;

      var i_nw = (int) round(ceil(phinorm) * NLAMBDA + floor(lambdanorm));
      var i_ne = (int) round(ceil(phinorm) * NLAMBDA + ceil(lambdanorm));
      var i_sw = (int) round(floor(phinorm) * NLAMBDA + floor(lambdanorm));
      var i_se = (int) round(floor(phinorm) * NLAMBDA + ceil(lambdanorm));


      nw_phi = RDCORR2018_GRID.get(i_nw)
          .getLat_corr();
      ne_phi = RDCORR2018_GRID.get(i_ne)
          .getLat_corr();
      sw_phi = RDCORR2018_GRID.get(i_sw)
          .getLat_corr();
      se_phi = RDCORR2018_GRID.get(i_se)
          .getLat_corr();
      nw_lambda = RDCORR2018_GRID.get(i_nw)
          .getLon_corr();
      ne_lambda = RDCORR2018_GRID.get(i_ne)
          .getLon_corr();
      sw_lambda = RDCORR2018_GRID.get(i_sw)
          .getLon_corr();
      se_lambda = RDCORR2018_GRID.get(i_se)
          .getLon_corr();

    } else {
      inside_bounds_correction_grid = false;
    }

    var RDcorrLat = 0.;
    if (inside_bounds_correction_grid) {
      RDcorrLat = (phinorm - floor(phinorm))
          * ((nw_phi * (floor(lambdanorm) + 1. - lambdanorm)) + ne_phi * (lambdanorm - floor(lambdanorm)))
          + (floor(phinorm) + 1. - phinorm)
              * ((sw_phi * (floor(lambdanorm) + 1. - lambdanorm)) + se_phi * (lambdanorm - floor(lambdanorm)));
    }
    phi = phi + RDcorrLat;

    var RDcorrLon = 0.;
    if (inside_bounds_correction_grid) {
      RDcorrLon = (phinorm - floor(phinorm))
          * ((nw_lambda * (floor(lambdanorm) + 1. - lambdanorm)) + ne_lambda * (lambdanorm - floor(lambdanorm)))
          + (floor(phinorm) + 1. - phinorm)
              * ((sw_lambda * (floor(lambdanorm) + 1. - lambdanorm)) + se_lambda * (lambdanorm - floor(lambdanorm)));
    }
    lambda = lambda + RDcorrLon;

    // Section 3.3

    phi = phi * PI / 180.;
    lambda = lambda * PI / 180.;

    var RN = BESSEL1841_A / sqrt(1 - (E_SQUARE_B1841 * pow(sin(phi), 2)));
    var X1geo = (RN + RD_BESSEL_H0) * cos(phi) * cos(lambda);
    var Y1geo = (RN + RD_BESSEL_H0) * cos(phi) * sin(lambda);
    var Z1geo = ((RN * (1 - E_SQUARE_B1841)) + RD_BESSEL_H0) * sin(phi);

    var X2geo = S_INV * (R11_INV * X1geo + R12_INV * Y1geo + R13_INV * Z1geo) + TX_INV;
    var Y2geo = S_INV * (R21_INV * X1geo + R22_INV * Y1geo + R23_INV * Z1geo) + TY_INV;
    var Z2geo = S_INV * (R31_INV * X1geo + R32_INV * Y1geo + R33_INV * Z1geo) + TZ_INV;

    phi1 = phiC;
    do {
      phi = phi1;
      RN = GRS80_A / sqrt(1. - (E_SQUARE_GRS80 * pow(sin(phi), 2)));
      if (X2geo > 0.) {
        phi1 = atan((Z2geo + E_SQUARE_GRS80 * RN * sin(phi)) / sqrt(pow(X2geo, 2) + pow(Y2geo, 2)));
      } else if (new BigDecimal(X2geo).setScale(11, RoundingMode.HALF_EVEN)
          .compareTo(BigDecimal.ZERO) == 0
          && new BigDecimal(Y2geo).setScale(11, RoundingMode.HALF_EVEN)
              .compareTo(BigDecimal.ZERO) == 0
          && new BigDecimal(Z2geo).setScale(11, RoundingMode.HALF_EVEN)
              .compareTo(BigDecimal.ZERO) >= 0) {
        phi1 = PI_DIVIDED_BY_2;
      } else {
        phi1 = PI_DIVIDED_BY_2_NEGATIVE;
      }
    } while (abs(phi1 - phi) > GRS80_EPSILON);

    phi = phi1;

    if (X2geo > 0.) {
      lambda = atan(Y2geo / X2geo);
    } else if (X2geo < 0. && BigDecimal.valueOf(Y2geo)
        .compareTo(ZERO) >= 0) {
      lambda = atan(Y2geo / X2geo) + PI;
    } else if (X2geo < 0. && Y2geo < 0.) {
      lambda = atan(Y2geo / X2geo) - PI;
    } else if (BigDecimal.valueOf(X2geo)
        .compareTo(ZERO) == 0 && Y2geo > 0) {
      lambda = PI_DIVIDED_BY_2;
    } else if (BigDecimal.valueOf(X2geo)
        .compareTo(ZERO) == 0 && Y2geo < 0) {
      lambda = PI_DIVIDED_BY_2_NEGATIVE;
    } else if (BigDecimal.valueOf(X2geo)
        .compareTo(ZERO) == 0
        && BigDecimal.valueOf(Y2geo)
            .compareTo(ZERO) == 0) {
      lambda = 0.;
    }

    var ETRS89_lat = phi * 180. / PI;
    var ETRS89_lon = lambda * 180. / PI;

    // Section 3.5

    phi = ETRS89_lat;
    lambda = ETRS89_lon;

    phinorm = (phi - PHI_MIN) / DELTA_PHI;
    lambdanorm = (lambda - LAMBDA_MIN) / DELTA_LAMBDA;

    var ETRS89_h = Double.NaN;

    if (BigDecimal.valueOf(phi)
        .compareTo(PHI_MIN_BIGD) >= 0
        && BigDecimal.valueOf(phi)
            .compareTo(PHI_MAX_BIGD) <= 0
        && BigDecimal.valueOf(lambda)
            .compareTo(LAMBDA_MIN_BIGD) >= 0
        && BigDecimal.valueOf(lambda)
            .compareTo(LAMBDA_MAX_BIGD) <= 0) {

      var i_nw = (int) round(ceil(phinorm) * NLAMBDA + floor(lambdanorm));
      var i_ne = (int) round(ceil(phinorm) * NLAMBDA + ceil(lambdanorm));
      var i_sw = (int) round(floor(phinorm) * NLAMBDA + floor(lambdanorm));
      var i_se = (int) round(floor(phinorm) * NLAMBDA + ceil(lambdanorm));

      var nw_height = NLGEO2018_GRID.get(i_nw)
          .getNap_height();
      var ne_height = NLGEO2018_GRID.get(i_ne)
          .getNap_height();
      var sw_height = NLGEO2018_GRID.get(i_sw)
          .getNap_height();
      var se_height = NLGEO2018_GRID.get(i_se)
          .getNap_height();

      var etrs89_quasi_height = (phinorm - floor(phinorm))
          * ((nw_height * (floor(lambdanorm) + 1. - lambdanorm)) + ne_height * (lambdanorm - floor(lambdanorm)))
          + (floor(phinorm) + 1. - phinorm)
              * ((sw_height * (floor(lambdanorm) + 1. - lambdanorm)) + se_height * (lambdanorm - floor(lambdanorm)));
      ETRS89_h = z + etrs89_quasi_height;
    }

    return new Coordinate(ETRS89_lon, ETRS89_lat, ETRS89_h);
  }

}
