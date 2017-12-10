package pl.polsl.geomarkers

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpSession
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import javax.annotation.Resource

@Component
class MockMvcFactory(
        @Resource
        private val webApplicationContext: WebApplicationContext
) {
    @Bean
    fun createMockMvc(): MockMvc =
            MockMvcBuilders
                    .webAppContextSetup(webApplicationContext)
                    .apply<DefaultMockMvcBuilder>(springSecurity())
                    .build()
}

fun MockMvc.postJson(uri: String, data: Any, session: MockHttpSession? = null): ResultActions {
    var builder = MockMvcRequestBuilders
            .post(uri)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(ObjectMapper().writeValueAsString(data))

    if (session != null) {
        builder = builder.session(session)
    }

    return this.perform(builder)
}

fun MockMvc.getWithJsonParam(uriWithParamName: String, data: Any) =
        this.perform(
                MockMvcRequestBuilders
                        .get(uriWithParamName + ObjectMapper().writeValueAsString(data))
        )

fun MockMvc.get(uri: String) =
        this.perform(
                MockMvcRequestBuilders
                        .get(uri)
        )

fun MockMvc.postEmpty(uri: String) =
        this.perform(
                MockMvcRequestBuilders
                        .post(uri)
        )

inline fun <reified T> ResultActions.andGetSuccessfullJson(): T {
    val response = this
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
            .andReturn()
            .response
    return ObjectMapper().readValue<T>(response.contentAsString, T::class.java)
}

fun ResultActions.andExpectSuccessfulStatus() =
        this
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)