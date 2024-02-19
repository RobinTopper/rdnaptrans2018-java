package io.github.rdnaptrans2018;

import io.github.rdnaptrans2018.transformation.Transformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "local")
public class Runner implements ApplicationRunner {

  @Autowired
  private Transformer transformer;

  @Override
  public void run(ApplicationArguments args) throws Exception {

    // Geschreven naar aanleiding van:
    // https://geoforum.nl/t/kleine-afwijking-bij-rdnaptrans2018-in-php/7443

    // Input
    var lat = 51.728601274;
    var lon = 4.712120126;
    var height = 301.7981;

    log.info("ETRS89 input coords: ({}, {}, {})", lon, lat, height);

    var rdCoords = transformer.transformEtrs89ToRdNap(lat, lon, height);
    log.info("ETRS89 -> RDNAP transformation. RDNAP coords: {}", rdCoords);

    var etrs89Coords = transformer.transformRdNapToEtrs89(rdCoords.getX(), rdCoords.getY(), rdCoords.getZ());
    log.info("RDNAP -> ETRS89 transformation. ETRS89 coords: {}", etrs89Coords);
  }

}
