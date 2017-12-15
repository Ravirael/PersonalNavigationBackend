package pl.polsl.geomarkers.marker

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpSession
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import pl.polsl.geomarkers.*
import pl.polsl.geomarkers.authentication.AuthenticationData
import javax.servlet.http.HttpSession

@RunWith(SpringRunner::class)
@SpringBootTest
class MarkerEndpointTest {
    @Autowired
    lateinit var mockMvcFactory: MockMvcFactory

    private lateinit var mock: MockMvc

    @Before
    fun setUp() {
        mock = mockMvcFactory.createMockMvc()

    }

    fun registerAndLogin(): Pair<AuthenticationData, MockHttpSession> {

        val authentication = mock
                .postEmpty("/authentication")
                .andGetSuccessfullJson<AuthenticationData>()

        val session = mock
                .perform(
                        MockMvcRequestBuilders
                                .post("/authenticate")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(ObjectMapper().writeValueAsString(authentication))
                )
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andReturn()
                .request
                .session as MockHttpSession

        return authentication to session

    }

    @Test
    fun `inserted markers should be retrievable by id`() {
        val (authentication, session) = registerAndLogin()

        val marker = DefaultMarker("name", DefaultGeoPoint(25.0, 50.0, 100.0))

        mock
                .postJson("/markers", marker, session)
                .andExpectSuccessfulStatus()

        val retrievedMarker = mock
                .get("/marker/${authentication.id}")
                .andGetSuccessfullJson<JpaMarker>()

        Assert.assertEquals(marker.name, retrievedMarker.name)
        Assert.assertEquals(marker.position, retrievedMarker.position)
    }

    @Test
    fun `all markers should contain inserted marker`() {
        val (authentication, session) = registerAndLogin()

        val marker = DefaultMarker("name", DefaultGeoPoint(25.0, 50.0, 100.0))

        mock
                .postJson("/markers", marker, session)
                .andExpectSuccessfulStatus()

        val retrievedMarkers = mock
                .get("/markers")
                .andGetSuccessfullJson<Array<JpaMarker>>()


        val retrievedMarker = retrievedMarkers.find { it.id == authentication.id }!!

        Assert.assertEquals(marker.name, retrievedMarker.name)
        Assert.assertEquals(marker.position, retrievedMarker.position)
    }

    @Test
    fun `markers within valid bound box should contain inserted marker`() {
        val (authentication, session) = registerAndLogin()

        val marker = DefaultMarker("name", DefaultGeoPoint(25.0, 50.0, 100.0))

        val boundingBox = DefaultBoundingBox(
                east = 30.0,
                west = 20.0,
                north = 60.0,
                south = 40.0
        )

        val boundingBoxJson = ObjectMapper().writeValueAsString(boundingBox)

        mock
                .postJson("/markers", marker, session)
                .andExpectSuccessfulStatus()

        val retrievedMarkers = mock
                .perform(
                        MockMvcRequestBuilders
                                .get("/markers")
                                .param("boundingBox", boundingBoxJson)
                )
                .andGetSuccessfullJson<Array<JpaMarker>>()

        val retrievedMarker = retrievedMarkers.find { it.id == authentication.id }!!

        Assert.assertEquals(marker.name, retrievedMarker.name)
        Assert.assertEquals(marker.position, retrievedMarker.position)
    }

    @Test
    fun `markers within invalid bound box should not contain inserted marker`() {
        val (authentication, session) = registerAndLogin()

        val marker = DefaultMarker("name", DefaultGeoPoint(25.0, 50.0, 100.0))

        val boundingBox = DefaultBoundingBox(
                east = 30.0,
                west = 26.0,
                north = 60.0,
                south = 40.0
        )

        val boundingBoxJson = ObjectMapper().writeValueAsString(boundingBox)

        mock
                .postJson("/markers", marker, session)
                .andExpectSuccessfulStatus()

        val retrievedMarkers = mock
                .perform(
                        MockMvcRequestBuilders
                                .get("/markers")
                                .param("boundingBox", boundingBoxJson)
                )
                .andGetSuccessfullJson<Array<JpaMarker>>()

        Assert.assertNull(retrievedMarkers.find { it.id == authentication.id })
    }
}