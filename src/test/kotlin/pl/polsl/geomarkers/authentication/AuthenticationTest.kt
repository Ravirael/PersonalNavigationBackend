package pl.polsl.geomarkers.authentication

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
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import javax.annotation.Resource
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import pl.polsl.geomarkers.MockMvcFactory
import pl.polsl.geomarkers.andGetSuccessfullJson
import pl.polsl.geomarkers.postEmpty
import pl.polsl.geomarkers.postJson
import java.util.*


@RunWith(SpringRunner::class)
@SpringBootTest
class AuthenticationTest {
    @Autowired
    lateinit var mockMvcFactory: MockMvcFactory

    private lateinit var mock: MockMvc

    @Before
    fun setUp() {
        mock = mockMvcFactory.createMockMvc()
    }

    @Test
    fun `registering and logging with retrieved credentials should succeed`() {

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
                .session

        Assert.assertNotNull(session)
        Assert.assertNotNull(session as? MockHttpSession)

    }

    @Test
    fun `logging with invalid credentials should fail`() {
        mock
                .postJson(
                        "/authenticate",
                        AuthenticationData(
                            id = 0,
                            key = UUID.randomUUID().toString()
                        )
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

}