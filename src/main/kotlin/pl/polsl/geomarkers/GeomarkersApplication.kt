package pl.polsl.geomarkers

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class GeomarkersApplication

fun main(args: Array<String>) {
    SpringApplication.run(GeomarkersApplication::class.java, *args)
}
