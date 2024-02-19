package io.github.rdnaptrans2018.transform;

import static org.junit.jupiter.api.Assertions.fail;
import io.github.rdnaptrans2018.transformation.Transformer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

class TransformerTest {

  private Transformer transformer = new Transformer();;

  @Test
  void validationRdNapToEtrs89Transformation() throws Exception {

    // Arrange
    var etrs89InputPath = new ClassPathResource("002_ETRS89.txt").getFile()
        .toPath();

    var writer = new BufferedWriter(new FileWriter("RD_RESULTS.txt", false));
    writer.write("point_id\tx\ty\tz\n");

    // Act
    Files.lines(etrs89InputPath)
        .skip(1L)
        .forEach(line -> {
          var values = line.split("\\s+");
          var rdCoords = transformer.transformEtrs89ToRdNap(Double.parseDouble(values[1]),
              Double.parseDouble(values[2]), Double.parseDouble(values[3]));
          try {
            writer
                .write(String.format("%s\t%s\t%s\t%s\n", values[0], rdCoords.getX(), rdCoords.getY(), rdCoords.getZ()));
          } catch (IOException ex) {
            fail();
          }
        });

    writer.close();

    // Assert

    // Upload result file on
    // https://www.nsgi.nl/geodetische-infrastructuur/producten/programma-rdnaptrans/validatieservice
  }

  @Test
  void validationEtrs89ToRdNapTransformation() throws Exception {
    // Arrange
    var etrs89InputPath = new ClassPathResource("002_RDNAP.txt").getFile()
        .toPath();

    var writer = new BufferedWriter(new FileWriter("ETRS89_RESULTS.txt", false));
    writer.write("point_id\tlatitude\tlongitude\theight\n");

    // Act
    Files.lines(etrs89InputPath)
        .skip(1L)
        .forEach(line -> {
          var values = line.split("\\s+");
          var rdCoords = transformer.transformRdNapToEtrs89(Double.parseDouble(values[1]),
              Double.parseDouble(values[2]), Double.parseDouble(values[3]));
          try {
            writer
                .write(String.format("%s\t%s\t%s\t%s\n", values[0], rdCoords.getY(), rdCoords.getX(), rdCoords.getZ()));
          } catch (IOException ex) {
            fail();
          }
        });

    writer.close();

    // Assert

    // Upload result file on
    // https://www.nsgi.nl/geodetische-infrastructuur/producten/programma-rdnaptrans/validatieservice
  }


}
