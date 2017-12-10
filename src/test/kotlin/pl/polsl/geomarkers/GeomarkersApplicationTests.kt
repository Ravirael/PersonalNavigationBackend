package pl.polsl.geomarkers

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import pl.polsl.geomarkers.marker.DefaultGeoPoint
import pl.polsl.geomarkers.marker.JpaMarker
import pl.polsl.geomarkers.marker.MarkerRepository
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class GeomarkersApplicationTests {

	@Autowired
	lateinit var markerRepository: MarkerRepository

	@Test
	fun contextLoads() {

	}

}
