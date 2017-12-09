package pl.polsl.geomarkers.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
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
import java.util.*


@RunWith(SpringRunner::class)
@SpringBootTest
class AuthenticationTest {
    @Resource
    lateinit var webApplicationContext: WebApplicationContext

    private lateinit var mock: MockMvc

    @Before
    fun setUp() {
        mock = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply<DefaultMockMvcBuilder>(springSecurity())
                .build()
    }

    @Test
    fun `registering and logging with retrieved credentials should succeed`() {

        val registerResponse = mock
                .perform(
                        MockMvcRequestBuilders
                                .post("/authentication")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                )
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andReturn()
                .response

        val authentication = ObjectMapper().readValue(registerResponse.contentAsString, AuthenticationData::class.java)

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
                .perform(
                        MockMvcRequestBuilders.post("/authenticate")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(
                                        ObjectMapper().writeValueAsString(
                                                AuthenticationData(
                                                    id = 0,
                                                    key = UUID.randomUUID()
                                                )
                                        )
                                )
                )
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
    }

}