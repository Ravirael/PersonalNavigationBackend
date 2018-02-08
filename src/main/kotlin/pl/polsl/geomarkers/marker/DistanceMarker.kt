package pl.polsl.geomarkers.marker

import pl.polsl.geomarkers.GenerateNoArg

@GenerateNoArg
class DistanceMarker(marker: IdentifiableMarker, val distance: Double): IdentifiableMarker by marker