package io.github.rdnaptrans2018.api.transform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import io.github.rdnaptrans2018.api.transform.exception.InvalidGeoJsonException;
import io.github.rdnaptrans2018.api.transform.exception.InvalidJsonException;
import io.github.rdnaptrans2018.api.transform.exception.UnsupportedAcceptException;
import io.github.rdnaptrans2018.api.transform.exception.UnsupportedCrsException;
import io.github.rdnaptrans2018.api.transform.exception.UnsupportedTransformationException;
import io.github.rdnaptrans2018.transformation.Transformer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFilter;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.locationtech.jts.io.geojson.GeoJsonWriter;
import org.locationtech.jts.precision.GeometryPrecisionReducer;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class TransformController {

  private static final MediaType SUPPORTED_ACCEPT_MEDIATYPE = MediaType.APPLICATION_JSON;

  private static final String ETRS89_ETRF2000_EPSG = Crs.ETRS89_ETRF2000.getEpsgCode()
      .toUpperCase();

  private static final String RD_NAP_EPSG = Crs.RD_NAP.getEpsgCode()
      .toUpperCase();

  private static final Set<String> SUPPORTED_CRS_EPSGCODES = Set.of(RD_NAP_EPSG, ETRS89_ETRF2000_EPSG);

  private static final String SUPPORTED_CRS_EPSGCODES_STRING = String.join(", ", SUPPORTED_CRS_EPSGCODES);

  private static final Map<String, Set<String>> SUPPORTED_TRANSFORMATIONS =
      Map.of(RD_NAP_EPSG, Set.of(ETRS89_ETRF2000_EPSG), ETRS89_ETRF2000_EPSG, Set.of(RD_NAP_EPSG));

  private static final String SUPPORTED_TRANSFORMATIONS_STRING = SUPPORTED_TRANSFORMATIONS.entrySet()
      .stream()
      .map(entry -> entry.getKey()
          .concat(" to ")
          .concat(String.join(" or ", entry.getValue())))
      .collect(Collectors.joining(", "));

  private final ObjectMapper objectMapper;

  private final Transformer transformer;

  @PostMapping(value = "/transform", consumes = MediaType.APPLICATION_JSON_VALUE)
  public Mono<ResponseEntity<TransformationResult>> transform(
      @RequestHeader(value = TransformHeaders.ACCEPT) String accept,
      @RequestHeader(value = TransformHeaders.CONTENT_CRS) String contentCrs,
      @RequestHeader(value = TransformHeaders.ACCEPT_CRS) String acceptCrs, @RequestBody DataBuffer body) {

    try {
      var upperCaseContentCrs = contentCrs.toUpperCase();
      var upperCaseAcceptCrs = acceptCrs.toUpperCase();

      validateHeaders(accept, upperCaseContentCrs, upperCaseAcceptCrs);
      validateRequestedTransformation(upperCaseContentCrs, upperCaseAcceptCrs);

      var bodyJsonNode = objectMapper.readTree(body.toString(StandardCharsets.UTF_8));
      var geoJsonNode = bodyJsonNode.get("data");

      if (geoJsonNode == null || geoJsonNode instanceof MissingNode) {
        throw new InvalidJsonException();
      }

      var geoJsonReader = new GeoJsonReader();
      var geometry = geoJsonReader.read(geoJsonNode.toString());

      PrecisionModel precisionModel;
      int decimals;
      if (contentCrs.equals(RD_NAP_EPSG)) {
        geometry.apply(new RdNapToEtrs89Transformation());
        precisionModel = new PrecisionModel3D(Crs.ETRS89_ETRF2000.getScale());
        decimals = Crs.ETRS89_ETRF2000.getDecimals();
      } else {
        geometry.apply(new Etrs89ToRdNapTransformation());
        precisionModel = new PrecisionModel3D(Crs.RD_NAP.getScale());
        decimals = Crs.RD_NAP.getDecimals();
      }

      var result = GeometryPrecisionReducer.reducePointwise(geometry, precisionModel);

      var transformationResult = TransformationResult.builder()
          .data(new GeoJsonWriter(decimals).write(result))
          .build();

      return Mono.just(ResponseEntity.ok()
          .header(TransformHeaders.CONTENT_CRS, upperCaseAcceptCrs)
          .body(transformationResult));

    } catch (ParseException ex) {
      throw new InvalidGeoJsonException(ex);
    } catch (JsonProcessingException ex) {
      throw new InvalidJsonException(ex);
    }
  }

  private void validateHeaders(String accept, String contentCrs, String acceptCrs) {
    var requestedMediaTypes = MediaType.parseMediaTypes(accept);
    MediaType.sortByQualityValue(requestedMediaTypes);

    requestedMediaTypes.stream()
        .filter(mt -> mt.isCompatibleWith(SUPPORTED_ACCEPT_MEDIATYPE))
        .findFirst()
        .orElseThrow(() -> new UnsupportedAcceptException(SUPPORTED_ACCEPT_MEDIATYPE.toString()));

    var uppercaseContentCrs = contentCrs.toUpperCase();
    if (!SUPPORTED_CRS_EPSGCODES.contains(uppercaseContentCrs)) {
      throw new UnsupportedCrsException(TransformHeaders.CONTENT_CRS, uppercaseContentCrs,
          SUPPORTED_CRS_EPSGCODES_STRING);
    }

    var uppercaseAcceptCrs = acceptCrs.toUpperCase();
    if (!SUPPORTED_CRS_EPSGCODES.contains(uppercaseAcceptCrs)) {
      throw new UnsupportedCrsException(TransformHeaders.ACCEPT_CRS, uppercaseAcceptCrs,
          SUPPORTED_CRS_EPSGCODES_STRING);
    }
  }

  private void validateRequestedTransformation(String contentCrs, String acceptCrs) {
    var uppercaseContentCrs = contentCrs.toUpperCase();
    var uppercaseAcceptCrs = acceptCrs.toUpperCase();
    if (!SUPPORTED_TRANSFORMATIONS.containsKey(uppercaseContentCrs)
        || !SUPPORTED_TRANSFORMATIONS.get(uppercaseContentCrs)
            .contains(uppercaseAcceptCrs)) {
      throw new UnsupportedTransformationException(uppercaseContentCrs, uppercaseAcceptCrs,
          SUPPORTED_TRANSFORMATIONS_STRING);
    }

  }

  private class RdNapToEtrs89Transformation implements CoordinateSequenceFilter {

    @Override
    public void filter(CoordinateSequence sequence, int i) {
      var coordinate = sequence.getCoordinate(i);
      var etrs89Coordinate =
          transformer.transformRdNapToEtrs89(coordinate.getX(), coordinate.getY(), coordinate.getZ());
      coordinate.setX(etrs89Coordinate.getX());
      coordinate.setY(etrs89Coordinate.getY());
      coordinate.setZ(etrs89Coordinate.getZ());
    }

    @Override
    public boolean isDone() {
      return false;
    }

    @Override
    public boolean isGeometryChanged() {
      return true;
    }
  }


  private class Etrs89ToRdNapTransformation implements CoordinateSequenceFilter {

    @Override
    public void filter(CoordinateSequence sequence, int i) {
      var coordinate = sequence.getCoordinate(i);
      var rdNapCoordinate = transformer.transformEtrs89ToRdNap(coordinate.getY(), coordinate.getX(), coordinate.getZ());
      coordinate.setX(rdNapCoordinate.getX());
      coordinate.setY(rdNapCoordinate.getY());
      coordinate.setZ(rdNapCoordinate.getZ());
    }

    @Override
    public boolean isDone() {
      return false;
    }

    @Override
    public boolean isGeometryChanged() {
      return true;
    }
  }


  private class TransformHeaders extends HttpHeaders {
    private static final long serialVersionUID = -4423011262431302092L;

    public static final String ACCEPT_CRS = "Accept-Crs";
    public static final String CONTENT_CRS = "Content-Crs";
  }
}
