package pl.polsl.geomarkers

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class GeomarkersApplicationTests {

	@Autowired
	lateinit var markerRepository: MarkerRepository

	@Test
	fun contextLoads() {
		val marker = JpaMarker(
				id = UUID.randomUUID(),
				name = "Rafał",
				position = DefaultGeoPoint(
						54.4,
						54.2,
						0.0
				)
		)

		markerRepository.save(marker)
	}

}
