package pl.polsl.geomarkers.marker

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.After
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

@RunWith(SpringRunner::class)
@SpringBootTest
class MarkerEndpointTest {
    @Autowired
    lateinit var mockMvcFactory: MockMvcFactory

    @Autowired
    lateinit var markerRepository: MarkerRepository

    private lateinit var mock: MockMvc


    @Before
    fun setUp() {
        mock = mockMvcFactory.createMockMvc()
    }

    @After
    fun tearDown() {
        markerRepository.deleteAll()
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

    private fun createMarker(
            name: String = "name",
            position: DefaultGeoPoint = DefaultGeoPoint(25.0, 50.0, 100.0),
            gender: Gender = Gender.Female,
            skill: Skill = Skill.Low
    ) = DefaultMarker(name, position, gender, skill)

    private fun addMarker(
            name: String = "name",
            position: DefaultGeoPoint = DefaultGeoPoint(25.0, 50.0, 100.0),
            gender: Gender = Gender.Female,
            skill: Skill = Skill.Low
    ) {
        val (authentication, session) = registerAndLogin()
        val marker = DefaultMarker(name, position, gender, skill)
        mock
                .postJson("/markers", marker, session)
                .andExpectSuccessfulStatus()
    }

    @Test
    fun `inserted markers should be retrievable by id`() {
        val (authentication, session) = registerAndLogin()

        val marker = createMarker()

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

        val marker = createMarker()

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

        val marker = createMarker()

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

        val marker = createMarker()

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

    @Test
    fun `gender filter should filter out wrong markers`() {
        addMarker(gender = Gender.Male)
        addMarker(gender = Gender.Female)

        val retrievedMarkers = mock
                .perform(
                        MockMvcRequestBuilders
                                .get("/markers")
                                .param("genders", "Female")
                )
                .andGetSuccessfullJson<Array<JpaMarker>>()

        Assert.assertEquals(1, retrievedMarkers.size)
        Assert.assertTrue(retrievedMarkers.all { it.gender == Gender.Female })
    }

}