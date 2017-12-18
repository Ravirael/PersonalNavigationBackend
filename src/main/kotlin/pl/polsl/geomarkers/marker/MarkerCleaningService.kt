package pl.polsl.geomarkers.marker

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock

@Service
@Transactional
class MarkerCleaningService(
        private val markerRepository: MarkerRepository,

        @Value("\${marker.validity_seconds}")
        private val markerValidityTime: Long,

        private val clock: Clock
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Scheduled(fixedRate = 60*1000, initialDelay = 0)
    fun removeOutdatedMarkers() {
        logger.info("Finding outdated markers...")

        val now = clock.instant()
        val outdatedMarkers = markerRepository
                .findAll()
                .filter{ it.updateTimestamp.plusSeconds(markerValidityTime).isBefore(now) }

        logger.info("Removing ${outdatedMarkers.size} markers...")

        markerRepository.delete(outdatedMarkers)
    }
}