# RDNAPTRANS2018-Java

A Java implementation of RDNAPTRANS™2018 (the RD + NAP to/from ETRS89 transformation) embedded in a simple REST API.

This project was inspired by [a discussion](https://geoforum.nl/t/kleine-afwijking-bij-rdnaptrans2018-in-php/7443) about a PHP RDNAPTRANS™2018 implementation on a Dutch forum for geodata users. The RDNAPTRANS™2018 implementation is based on [this](https://github.com/FVellinga/gm_rdnaptrans2018/blob/main/gm_rdnaptrans2018.sas) SAS RDNAPTRANS™2018 implementation.

The transformation within the bounds of the correction grids (latitude 50-56°, longitude 2-8°) meets the RDNAPTRANS™2018 specifications as tested by the Validation Service of RDNAPTRANS™2018 (permission pending).

## Known issues

* Several parts in the Transformer code compare coordinates, i.e. floating point values, to determine whether the point of interest is an edge-case (exactly the "central point Amersfoort", or a point on the opposite side of the sphere). A tolerance should be used to do a correct comparison, but this has not been implemented yet.

## License

[MIT](https://choosealicense.com/licenses/mit/)