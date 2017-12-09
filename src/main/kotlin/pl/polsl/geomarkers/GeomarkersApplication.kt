package pl.polsl.geomarkers

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class GeomarkersApplication

fun main(args: Array<String>) {
    SpringApplication.run(GeomarkersApplication::class.java, *args)
}
